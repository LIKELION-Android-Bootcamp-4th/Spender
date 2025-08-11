const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendDataOnly, payloadReport } = require("../send-fcm");

function prevMonthYYYYMM() {
  const d = new Date(new Date().toLocaleString("en-US", { timeZone: "Asia/Seoul" }));
  let y = d.getFullYear();
  let m = d.getMonth(); // 현재월
  m -= 1;
  if (m < 0) { m = 11; y -= 1; }
  return `${y}-${String(m + 1).padStart(2, "0")}`;
}

module.exports = functions.pubsub
  .schedule("0 9 1 * *") // 매월 1일 09:00
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    const month = prevMonthYYYYMM();
    const snap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.reportAlert", "==", true)
      .get();

    const jobs = [];
    snap.forEach(doc => {
      const u = doc.data();
      if (!u.fcmToken) return;
      jobs.push(sendDataOnly(u.fcmToken, payloadReport(month))
        .catch(err => {
          console.error("REPORT send error", doc.id, err);
          if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
            return doc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
          }
        })
      );
    });

    await Promise.allSettled(jobs);
    return null;
  });
