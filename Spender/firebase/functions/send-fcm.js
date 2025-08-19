const admin = require("firebase-admin");

// 공용 FCM 전송 함수
async function sendDataOnly(token, payload) {
  return admin.messaging().send({
    token,
    data: payload, // 데이터 페이로드
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

module.exports = {
  sendDataOnly,
  payloadReport,
  payloadBudget,
  payloadReminder
};
