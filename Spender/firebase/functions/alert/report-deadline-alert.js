const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
const { sendWithNotification, payloadReportDeadline } = require("../send-fcm");
const { addNotification } = require("../notification-store");

function nowInSeoul() {
  return new Date(new Date().toLocaleString("en-US", { timeZone: "Asia/Seoul" }));
}

/** 말일 여부 판단 */
function isLastDayInSeoul(d = nowInSeoul()) {
  const y = d.getFullYear();
  const m = d.getMonth(); // 0-11
  const lastDay = new Date(y, m + 1, 0).getDate(); // 이번 달 말일
  return d.getDate() === lastDay;
}

function currentMonthYYYYMM(d = nowInSeoul()) {
  const y = d.getFullYear();
  const m = d.getMonth(); // 0-11
  return `${y}-${String(m + 1).padStart(2, "0")}`;
}

module.exports = functions.pubsub
  .schedule("0 20 28-31 * *") // 매달 28-31일 8시에 마지막날인지 판단
  .timeZone("Asia/Seoul")
  .onRun(async () => {
    if (!isLastDayInSeoul()) return null;  // 말일이 아니면 종료

    const month = currentMonthYYYYMM();

    const usersSnap = await admin.firestore()
      .collection("users")
      .where("notificationSettings.reportDeadlineAlert", "==", true)
      .get();

    const sendJobs = [];
    const saveJobs = [];

    usersSnap.forEach(userDoc => {
      const u = userDoc.data();
      const uid = userDoc.id;
      const token = u.fcmToken;
      if (!token) return;

      // 1) FCM 전송
      sendJobs.push(
        sendWithNotification(token, payloadReportDeadline(month)).catch(err => {
          console.error("REPORT DEADLINE send error", uid, err);
          // 토큰 만료 처리
          if (err?.errorInfo?.code === "messaging/registration-token-not-registered") {
            return userDoc.ref.update({ fcmToken: admin.firestore.FieldValue.delete() });
          }
        })
      );

      // 2) 알림 문서 저장
      saveJobs.push(
        addNotification(uid, {
          type: "REPORT_DEADLINE_ALERT",
          title: "리포트 반영 마감 임박!",
          content: "오늘까지 입력한 지출만 이번 달 리포트에 반영돼요. 오늘 자정 전까지 입력해주세요!",
          isRead: false,
          extra: { route: `add_expense`, month }
        }).catch(err => console.error("REPORT DEADLINE save error", uid, err))
      );
    });

    await Promise.allSettled(sendJobs);
    await Promise.allSettled(saveJobs);
    return null;
  });
