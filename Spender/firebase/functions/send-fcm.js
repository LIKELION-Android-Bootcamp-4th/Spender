const admin = require("firebase-admin");

// 공용 FCM 전송 함수
async function sendWithNotification(token, payload) {
  const { title, body, ...rest } = payload; // title, body 제외

  return admin.messaging().send({
    token,
    notification: {
      title,
      body,
    },
    data: rest, // route, month 등만 들어감
  });
}

// 리포트 알림 페이로드
function payloadReport(month) {
  return {
    title: `${month.split("-")[1]}월 지출 리포트가 생성되었어요!`,
    body: "이번 달 리포트를 확인해보세요.",
    route: `report_detail/${month}`,
    month,
  };
}

// 예산 알림 페이로드
function payloadBudget(title, body) {
  return {
    title,
    body,
    route: "home"
  };
}

// 정기지출 알림 페이로드
function payloadReminder(title, body) {
  return {
    title,
    body,
    route: "analysis"
  };
}

// 리포트 마감 임박 알림 페이로드
function payloadReportDeadline(month) {
  return {
    title: "리포트 마감 임박!",
    body: "오늘까지 입력한 지출만 이번 달 리포트에 반영돼요. 오늘 자정 전까지 입력해주세요!",
    route: "add_expense",
    month,
  };
}

module.exports = {
  sendWithNotification,
  payloadReport,
  payloadBudget,
  payloadReminder,
  payloadReportDeadline
};
