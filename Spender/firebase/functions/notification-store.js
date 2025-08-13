const admin = require("firebase-admin");
const db = admin.firestore();

function sevenDaysFromNow() {
  return admin.firestore.Timestamp.fromDate(
    new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)
  );
}

/**
 * 알림 문서를 저장 (알림 모아보기용)
 * @param {string} uid
 * @param {{
 *  type: "BUDGET_ALERT"|"REPORT_ALERT"|"REMINDER_ALERT",
 *  title: string,
 *  content: string,
 *  extra?: object // route, month, regularExpenseName 등
 * }} p
 */
async function addNotification(uid, p) {
  const ref = db.collection("users").doc(uid)
    .collection("notifications").doc();

  const data = {
    notificationType: p.type,
    title: p.title,
    content: p.content,
    createdAt: admin.firestore.FieldValue.serverTimestamp(),
    expireAt: sevenDaysFromNow(),
    ...(p.extra || {})
  };

  await ref.set(data);
  return ref.id;
}

module.exports = { addNotification };
