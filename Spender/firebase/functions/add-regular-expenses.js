const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");

module.exports = functions.pubsub
    .schedule('0 0 * * *')
    .timeZone('Asia/Seoul')
    .onRun(async () => {
        const db = admin.firestore();

        const usersSnap = await db.collection('users').get();

        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const todayDay = today.getDate() + 1;

        for (const userDoc of usersSnap.docs) {
            const userId = userDoc.id;
            const regularSnap = await db.collection(`users/${userId}/regular_expenses`).get();

            for (const regularDoc of regularSnap.docs) {
                const data = regularDoc.data();

                if (data.day == todayDay) {
                    await db.collection(`users/${userId}/expenses`).add({
                        date: admin.firestore.FieldValue.serverTimestamp(),
                        createdAt: admin.firestore.FieldValue.serverTimestamp(),
                        amount: data.amount,
                        title: data.title,
                        categoryId: data.categoryId
                    });
                    console.log(`Added expense for user ${userId}: ${data.title}`);
                } else {
                    console.log(`Skipping for user ${userId}: day ${data.day} != ${todayDay}`);
                }
            }
        }

        console.log("Daily expenses process completed.");
        return null;
    });