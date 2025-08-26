require('dotenv').config();

const admin = require("firebase-admin");
admin.initializeApp();

const {onCall} = require("firebase-functions/v1/https");
const auth = require("firebase-functions/v1/auth");

const onRegularExpenseCreate = require("./create-regular-expenses");
const addRegularExpenses = require("./add-regular-expenses");
const handleUserCreate = require("./create-user-doc-trigger");
const handleKakao = require("./kakao-auth");
const handleNaver = require("./naver-auth");
const generateMonthlyReports = require("./report-generator");
const scheduleReportAlerts = require("./alert/report-alert");
const scheduleBudgetAlerts = require("./alert/budget-alert");
const scheduleReminderAlerts = require("./alert/reminder-alert");
const scheduleReportDeadlineAlerts = require("./alert/report-deadline-alert");
const updateProfile = require("./update-profile");

exports.addDailyExpenses = addRegularExpenses;
exports.onRegularExpenseCreate = onRegularExpenseCreate;
exports.kakaoCustomAuth = onCall(handleKakao);
exports.naverCustomAuth = onCall(handleNaver);
exports.createUserDoc = auth.user().onCreate(handleUserCreate);
exports.generateMonthlyReports = generateMonthlyReports;
exports.scheduleReportAlerts = scheduleReportAlerts;
exports.scheduleBudgetAlerts = scheduleBudgetAlerts;
exports.scheduleReminderAlerts = scheduleReminderAlerts;
exports.scheduleReportDeadlineAlerts = scheduleReportDeadlineAlerts;
exports.updateProfile  = updateProfile;
