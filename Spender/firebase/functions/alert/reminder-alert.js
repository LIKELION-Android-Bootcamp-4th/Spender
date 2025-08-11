const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendDataOnly, payloadReminder } = require("../send-fcm");

module.exports = functions.pubsub
  .schedule("0 9 * * *") // 매일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const today = new Date(new Date().toLocaleString("en-US", { timeZone: "Asia/Seoul" }));
    const day = today.getDate();

    const snap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.reminderAlert", "==", true)
      .get();

    const jobs = [];
    for (const doc of snap.docs) {
      const u = doc.data();
      if (!u.fcmToken) continue;

      // 정기지출 하위 컬렉션 조회
      const regSnap = await doc.ref.collection("regular_expenses")
        .where("day", "==", day)
        .get();

      regSnap.forEach(exp => {
        const expData = exp.data();
        jobs.push(sendDataOnly(u.fcmToken, payloadReminder(expData.name))
          .catch(err => {
            console.error("REMINDER send error", doc.id, err);
            if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
              return doc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
            }
          })
        );
      });
    }

    await Promise.allSettled(jobs);
    return null;
  });
