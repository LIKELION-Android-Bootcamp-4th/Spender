const admin = require("firebase-admin");

/**
 * Firestore에 사용자 문서를 수동으로 생성합니다.
 * @param {Object} user 사용자 정보 객체
 */
async function createUserDocManually(user) {
  const userDocRef = admin.firestore().collection("users").doc(user.uid);
  console.log("createUserDocManually ->", user);
  await userDocRef.set(
      {
        email: user.email || "",
        name: user.name || "",
        photoUrl: user.profileImage || "",
        provider: user.provider || "unknown",
        createdAt: admin.firestore.FieldValue.serverTimestamp(),
      },
      {merge: true},
  );
}

module.exports = {createUserDocManually};
