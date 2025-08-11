const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendDataOnly, payloadBudget } = require("../send-fcm");

module.exports = functions.pubsub
  .schedule("0 9 20 * *") // 매월 20일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const snap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.budgetAlert", "==", true)
      .get();

    const jobs = [];
    snap.forEach(doc => {
      const u = doc.data();
      if (!u.fcmToken) return;
      jobs.push(sendDataOnly(u.fcmToken, payloadBudget())
        .catch(err => {
          console.error("BUDGET send error", doc.id, err);
          if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
            return doc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
          }
        })
      );
    });

    await Promise.allSettled(jobs);
    return null;
  });
