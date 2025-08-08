const functions = require("firebase-functions");
const { pubsub } = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { OpenAI } = require("openai");
const db = admin.firestore();
const openai = new OpenAI({
  apiKey: process.env.OPENAI_KEY
});

// 날짜 범위 계산 함수
function getMonthRange(monthStr) {
  const [year, month] = monthStr.split("-").map(Number);
  const start = new Date(year, month - 1, 1);
  const end = new Date(year, month, 1);
  return { start, end };
}

// 리포트 요약 계산 함수
async function calculateUserReport(userRef, monthStr) {
  const { start, end } = getMonthRange(monthStr);

  const [expensesSnap, budgetSnap, categoriesSnap] = await Promise.all([
    userRef.collection("expenses")
      .where("date", ">=", start)
      .where("date", "<", end)
      .get(),
    userRef.collection("budgets").doc(monthStr).get(),
    userRef.collection("categories").get(),
  ]);

  // 예산
  const totalBudget = budgetSnap.exists ? budgetSnap.data().amount : 0;

  // 지출 총합, 카테고리별, 감정별 초기화
  let totalExpense = 0;
  const categoryMap = {};
  const emotionMap = {};

  const categoryMeta = {};
  categoriesSnap.forEach((doc) => {
    const data = doc.data();
    if (data.type === "EXPENSE") {
      categoryMeta[doc.id] = {
        name: data.name,
        color: data.color,
      };
    }
  });

  expensesSnap.forEach((doc) => {
    const data = doc.data();

    const amount = data.amount;
    const categoryId = data.categoryId;
    const emotionId = data.emotion;

    totalExpense += amount;

    if (!categoryMeta[categoryId]) {
      if (!categoryMap["unknown"]) {
        categoryMap["unknown"] = {
          categoryId: "unknown",
          categoryName: "기타",
          color: "#CCCCCC",
          totalPrice: 0,
        };
      }
      categoryMap["unknown"].totalPrice += amount;
    } else {
      if (!categoryMap[categoryId]) {
        categoryMap[categoryId] = {
          categoryId: categoryId,
          categoryName: categoryMeta[categoryId].name,
          color: categoryMeta[categoryId].color,
          totalPrice: 0,
        };
      }
      categoryMap[categoryId].totalPrice += amount;
    }

    if (emotionId) {
      emotionMap[emotionId] = (emotionMap[emotionId] || 0) + 1;
    }
  });

  return {
    totalBudget,
    totalExpense,
    byCategory: Object.values(categoryMap),
    byEmotion: Object.entries(emotionMap).map(([emotionId, amount]) => ({
      emotionId,
      amount,
    })),
  };
}

// GPT 피드백 생성
async function generateFeedback(summary) {
  try {
    const { totalBudget, totalExpense, byCategory, byEmotion } = summary;

    const categoryText = byCategory
      .map((c) => `${c.categoryName}: ${c.totalPrice.toLocaleString()}원`)
      .join(", ");
    const emotionText = byEmotion
      .map((e) => `${e.emotionId}: ${e.amount}회`)
      .join(", ");

    const prompt = `
      이번 달 예산은 ${totalBudget.toLocaleString()}원이고,
      지출은 ${totalExpense.toLocaleString()}원이에요.
      카테고리별로는 ${categoryText}이고,
      감정 태그는 ${emotionText}예요.

      위 내용을 바탕으로, 귀여운 가계부 캐릭터 '지출이'가 사용자에게
      3~4문장 정도로 친근하게 피드백을 해줘.
    `;

    if (!prompt || prompt.trim().length < 10) {
      console.warn("생성된 prompt가 너무 짧음:", prompt);
    }

    const completion = await openai.chat.completions.create({
      model: "gpt-4.1-nano",
      messages: [
        {
          role: "system",
          content: "너는 지출이라는 캐릭터야. 사용자의 한 달 지출에 대해 귀엽고 친절하고 피드백을 해줘. 재정 전문가처럼 너무 딱딱하게 말하지 말고, 반말을 사용해서 친구에게 말하듯이 공감해주는 말투를 사용해줘. 그러나 바꿔야 할 부분이 있다면 말해줘. 금액은 ~만원, ~천원처럼 자연스럽게 말해줘. 숫자 그대로 쓰지 마. 문장은 자연스럽게 마침표로 끝내.",
        },
        { role: "user", content: prompt },
      ],
    });

    const message = completion?.choices?.[0]?.message?.content;

    if (!message) {
      console.error("GPT 응답이 비어 있음:", JSON.stringify(completion, null, 2));
      throw new Error("GPT 응답이 없습니다.");
    }

    return message;
  } catch (e) {
    console.error("generateFeedback 에러 발생:", e);
    return "이번 달 리포트를 생성하는 데 문제가 있었어. 다음 달엔 더 열심히 분석할게!";
  }
}

// 매월 1일 실행
const generateMonthlyReports = pubsub
  .schedule("0 0 1 * *")
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const usersSnap = await db.collection("users").get();

    const now = new Date();
    let target = new Date(now);

    if (now.getDate() === 1) {
      target.setMonth(target.getMonth() - 1);
    }

    const monthStr = `${target.getFullYear()}-${String(target.getMonth() + 1).padStart(2, "0")}`;

    for (const userDoc of usersSnap.docs) {
      const userRef = db.collection("users").doc(userDoc.id);
      try {
        const summary = await calculateUserReport(userRef, monthStr);

        const { totalBudget, totalExpense } = summary;

        if (totalBudget === 0 || totalExpense === 0) {
          console.log(`[${userDoc.id}] 예산 또는 지출이 0원이라 리포트 생략`);
          continue; // 리포트 생략
        }

        const feedback = await generateFeedback(summary);
        await userRef.collection("reports").doc(monthStr).set({
          ...summary,
          feedback,
          month: monthStr,
          created_at: admin.firestore.FieldValue.serverTimestamp(),
        });
        console.log(`[${userDoc.id}] 리포트 저장 완료`);
      } catch (err) {
        console.error(`[${userDoc.id}] 리포트 실패`, err);
      }
    }

  });

module.exports = generateMonthlyReports;