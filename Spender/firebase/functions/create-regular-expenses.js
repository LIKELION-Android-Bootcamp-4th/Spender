const functions = require("firebase-functions/v1");

functions.firestore
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
        const todayDay = today.getDate();
        const paymentDay = data.day;

        firstPaymentDate.setHours(0, 0, 0, 0);
        today.setHours(0, 0, 0, 0);

        if (firstPaymentDate.getTime() <= today.getTime() && todayDay == paymentDay) {
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