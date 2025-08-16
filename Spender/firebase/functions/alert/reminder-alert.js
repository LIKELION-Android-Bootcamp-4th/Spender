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

      // 정기지출 title 리스트 수집
      const titles = regSnap.docs.map(doc => doc.data().title ?? "정기지출");

      // 최대 3개까지만 노출 + 나머지는 요약
      const MAX_VISIBLE = 3;
      const visibleTitles = titles.slice(0, MAX_VISIBLE);
      const hiddenCount = titles.length - visibleTitles.length;

      let titleList = visibleTitles.join(", ");
      if (hiddenCount > 0) {
        titleList += ` 외 ${hiddenCount}건`;
      }

      // 알림 제목/내용 생성
      const title = `오늘은 정기지출 ${titles.length}건이 있어요`;
      const content = `오늘 정기지출: ${titleList}`;

      // 1) FCM 발송
      sendJobs.push(
        sendDataOnly(token, payloadReminder(title, content)).catch(err => {
            console.error("REMINDER send error", uid, err);
            if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
             return userDoc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
            }
        })
      );

      // 2) 알림 저장
      saveJobs.push(
       addNotification(uid, {
         type: "REMINDER_ALERT",
         title,
         content,
         extra: { route: "stats" }
       }).catch(err => console.error("REMINDER save error", uid, err))
     );
    }

    await Promise.allSettled(sendJobs);
    await Promise.allSettled(saveJobs);
    return null;
  });
