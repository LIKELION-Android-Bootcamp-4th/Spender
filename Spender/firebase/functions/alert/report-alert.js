const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendDataOnly, payloadReport } = require("../send-fcm");
const { addNotification } = require("../notification-store");

function prevMonthYYYYMM() {
  const d = new Date(new Date().toLocaleString("en-US", { timeZone: "Asia/Seoul" }));
  let y = d.getFullYear();
  let m = d.getMonth(); // 현재월(0-11)
  m -= 1;
  if (m < 0) { m = 11; y -= 1; }
  return `${y}-${String(m + 1).padStart(2, "0")}`;
}

module.exports = functions.pubsub
  .schedule("0 9 1 * *") // 매월 1일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const month = prevMonthYYYYMM();

    const usersSnap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.reportAlert", "==", true)
      .get();

    const sendJobs = [];
    const saveJobs = [];

    usersSnap.forEach(userDoc => {
      const u = userDoc.data();
      const uid = userDoc.id;
      const token = u.fcmToken;
      if (!token) return;

      // 1) 푸시 전송
      sendJobs.push(
        sendDataOnly(token, payloadReport(month)).catch(err => {
          console.error("REPORT send error", uid, err);
          if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
            return userDoc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
          }
        })
      );

      // 2) 알림 문서 저장 (모아보기 + TTL)
      saveJobs.push(
        addNotification(uid, {
          type: "REPORT_ALERT",
          title: `${month.split("-")[1]}월 지출 리포트가 도착했어요!`,
          content: `눌러서 이번 달 리포트를 확인해보세요`,
          isRead: false,
           extra: { route: `report_detail/${month}`, month }
        }).catch(err => console.error("REPORT save error", uid, err))
      );
    });

    await Promise.allSettled(sendJobs);
    await Promise.allSettled(saveJobs);
    return null;
  });
