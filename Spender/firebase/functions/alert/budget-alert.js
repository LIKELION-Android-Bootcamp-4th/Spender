const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendDataOnly, payloadBudget } = require("../send-fcm");
const { addNotification } = require("../notification-store");

module.exports = functions.pubsub
  .schedule("0 9 20 * *") // 매월 20일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const snap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.budgetAlert", "==", true)
      .get();

    const sendJobs = [];
    const saveJobs = [];

    snap.forEach(doc => {
      const u = doc.data();
      const uid = doc.id;
      const token = u.fcmToken;
      if (!token) return;

      // 1) 푸시 발송
      sendJobs.push(
        sendDataOnly(token, payloadBudget()).catch(err => {
          console.error("BUDGET send error", uid, err);
          if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
            return doc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
          }
        })
      );

      // 2) 알림 문서 저장 (모아보기)
      saveJobs.push(
        addNotification(uid, {
          type: "BUDGET_ALERT",
          title: "예산 알림",
          content: "이번 달 예산 상황을 확인해보세요!",
          extra: { route: "home" }               // 필요시 라우팅 파라미터
        }).catch(err => console.error("BUDGET save error", uid, err))
      );
    });

    await Promise.allSettled([...sendJobs, ...saveJobs]);
    return null;
  });
