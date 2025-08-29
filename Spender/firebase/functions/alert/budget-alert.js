const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendWithNotification, payloadBudget } = require("../send-fcm");
const { addNotification } = require("../notification-store");

const formatMonth = (date) => {
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, "0");
  return `${year}-${month}`;
};

module.exports = functions.pubsub
  .schedule("0 9 21 * *") // 매월 21일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const now = new Date();
    const yyyyMM = formatMonth(now);
    const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1);
    const endOfPeriod = new Date(now.getFullYear(), now.getMonth(), 20, 23, 59, 59);

    const snap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.budgetAlert", "==", true)
      .get();

    const sendJobs = [];
    const saveJobs = [];

    for (const doc of snap.docs) {
          const uid = doc.id;
          const user = doc.data();
          const token = user.fcmToken;
          if (!token) continue;

          // 1. 예산 가져오기
          const budgetSnap = await doc.ref.collection("budgets").doc(yyyyMM).get();
          const budgetData = budgetSnap.data();
          const budgetAmount = budgetData?.amount ?? 0;
          if (!budgetAmount) continue; // 예산이 없으면 알림 생략

          // 2. 이번 달 지출 총합 계산
          const expensesSnap = await doc.ref.collection("expenses")
            .where("date", ">=", startOfMonth)
            .where("date", "<=", endOfPeriod)
            .get();

          const totalExpense = expensesSnap.docs.reduce((sum, d) => {
            const amount = d.data().amount ?? 0;
            return sum + amount;
          }, 0);

          // 3. 퍼센트 계산
          let usage = Math.round((totalExpense / budgetAmount) * 100);
          usage = Math.min(usage, 999); // 이상치 방지

          // 4. 메시지 구성
          const title = `${now.getMonth() + 1}월 예산의 ${usage}%를 사용 중이에요!`;
          let content = "이번 달 지출을 확인하고 계획을 조정해보세요.";
          if (usage >= 70) {
            content = "예산 초과에 주의하세요! 계획된 지출을 다시 확인해보세요.";
          } else if (usage <= 50) {
            content = "좋은 흐름이에요! 예산을 잘 관리하고 있어요.";
          }

          // 5. FCM 전송
          sendJobs.push(
            sendWithNotification(token, payloadBudget(title, content)).catch(err => {
              console.error("BUDGET send error", uid, err);
              if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
                return doc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
              }
            })
          );

          // 6. 알림 저장
          saveJobs.push(
            addNotification(uid, {
              type: "BUDGET_ALERT",
              title,
              content,
              isRead: false,
              extra: { route: "home" }
            }).catch(err => console.error("BUDGET save error", uid, err))
          );
        }

    await Promise.allSettled([...sendJobs, ...saveJobs]);
    return null;
  });
