require('dotenv').config();

const admin = require("firebase-admin");
admin.initializeApp();

const {onCall} = require("firebase-functions/v1/https");
const auth = require("firebase-functions/v1/auth");

const handleUserCreate = require("./create-user-doc-trigger");
const handleKakao = require("./kakao-auth");
const handleNaver = require("./naver-auth");
const generateMonthlyReports = require("./report-generator");
const scheduleReportAlerts = require("./alert/report-alert");
const scheduleBudgetAlerts = require("./alert/budget-alert");
const scheduleReminderAlerts = require("./alert/reminder-alert");

exports.kakaoCustomAuth = onCall(handleKakao);
exports.naverCustomAuth = onCall(handleNaver);
exports.createUserDoc = auth.user().onCreate(handleUserCreate);
exports.generateMonthlyReports = generateMonthlyReports;
exports.scheduleReportAlerts = scheduleReportAlerts;
exports.scheduleBudgetAlerts = scheduleBudgetAlerts;
exports.scheduleReminderAlerts = scheduleReminderAlerts;

const functions = require("firebase-functions/v1");
exports.onRegularExpenseCreate = functions.firestore
    .document(`users/{userId}/regular_expenses/{regularExpenseId}`)
    .onCreate(async (snap, context) => {
        const data = snap.data();
        const { userId } = context.params;
        if (!data.first_payment_date) {
            console.log(`No firstpaymentdate found, skipping. . .`);
            return null;
        }

        const firstPaymentDate = data.first_payment_date.toDate();
        const today = new Date();

        firstPaymentDate.setHours(0, 0, 0, 0);
        today.setHours(0, 0, 0, 0);

        if (firstPaymentDate.getTime() <= today.getTime()) {
            await admin.firestore()
                .collection(`users/${userId}/expenses`)
                .add({
                    date: data.first_payment_date,
                    createdAt: admin.firestore.FieldValue.serverTimestamp(),
                    amount: data.amount,
                    title: data.title,
                    categoryId: data.categoryId
                });
            console.log(`Expense added for user ${userId}`)
        } else {
            console.log(`first payment date is in the future. skipping. . .`)
        }

        return null;
    });

exports.addDailyExpenses = functions.pubsub
    .schedule('0 0 * * *')
    .timeZone('Asia/Seoul')
    .onRun(async (context) => {
        const db = admin.firestore();

        const usersSnap = await db.collection('users').get();

        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const todayTimestamp = admin.firestore.Timestamp.fromDate(today);

        for (const userDoc of usersSnap.docs) {
            const userId = userDoc.id;
            const regularSnap = await db.collection(`users/${userId}/regular_expenses`).get();

            for (const regularDoc of regularSnap.docs) {
                const data = regularDoc.data();

                await db.collection(`users/${userId}/expenses`).add({
                    date: todayTimestamp,
                    createdAt: admin.firestore.FieldValue.serverTimestamp(),
                    amount: data.amount,
                    title: data.title,
                    categoryId: data.categoryId
                });
            }
        }

        console.log("Daily expenses added for all users");
        return null;
    });