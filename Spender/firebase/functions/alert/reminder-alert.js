const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendDataOnly, payloadReminder } = require("../send-fcm");
const { addNotification } = require("../notification-store");

module.exports = functions.pubsub
  .schedule("0 9 * * *") // 매일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const today = new Date(new Date().toLocaleString("en-US", { timeZone: "Asia/Seoul" }));
    const day = today.getDate();

    const usersSnap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.reminderAlert", "==", true)
      .get();

    const sendJobs = [];
    const saveJobs = [];

    for (const userDoc of usersSnap.docs) {
      const u = userDoc.data();
      const uid = userDoc.id;
      const token = u.fcmToken;
      if (!token) continue;

      // 유저의 정기지출 중 오늘(day)이 결제일인 것만
      const regSnap = await userDoc.ref
        .collection("regular_expenses")
        .where("day", "==", day)
        .get();

      if (regSnap.empty) continue;

      regSnap.forEach(reDoc => {
        const re = reDoc.data();
        const name = re.title || re.name || "정기지출";

        // 1) 푸시 전송
        sendJobs.push(
          sendDataOnly(token, payloadReminder(name)).catch(err => {
            console.error("REMINDER send error", uid, err);
            if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
              return userDoc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
            }
          })
        );

        // 2) 알림 문서 저장 (모아보기 + TTL)
        saveJobs.push(
          addNotification(uid, {
            type: "REMINDER_ALERT",
            title: `오늘은 ${name} 정기지출이 있는 날이에요!`,
            content: "정기지출 내역을 확인해보세요.",
            extra: { route: "stats", regularExpenseName: name }
          }).catch(err => console.error("REMINDER save error", uid, err))
        );
      });
    }

    await Promise.allSettled(sendJobs);
    await Promise.allSettled(saveJobs);
    return null;
  });
