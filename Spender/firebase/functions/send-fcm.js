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
    title: "지출 리포트가 생성되었어요!",
    body: "이번 달 리포트를 확인해보세요.",
    route: "reports",
    month: month,
  };
}

// 예산 알림 페이로드
function payloadBudget() {
  return {
    title: "예산 사용량을 확인하세요!",
    body: "이번 달이 곧 끝나요. 예산 대비 지출을 확인해보세요.",
    route: "home",
  };
}

// 정기지출 알림 페이로드
function payloadReminder(name) {
  return {
    title: `${name} 정기지출일입니다.`,
    body: "오늘 정기지출이 예정되어 있어요. 확인해보세요.",
    route: "analysis",
    regularExpenseName: name,
  };
}

module.exports = {
  sendDataOnly,
  payloadReport,
  payloadBudget,
  payloadReminder
};
