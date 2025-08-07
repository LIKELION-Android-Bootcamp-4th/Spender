const admin = require("firebase-admin");
const axios = require("axios");
const {mapKakaoUser} = require("./utils/social-mapper");
const {createUserDocManually} = require("./create-user-doc");

module.exports = async (data) => {
  try {
    const accessToken = data.accessToken;
    if (!accessToken) {
      throw new Error("accessToken is required");
    }

    const res = await axios.get("https://kapi.kakao.com/v2/user/me", {
      headers: {Authorization: `Bearer ${accessToken}`},
    });

    console.log("kakao API response", res.status, res.data);

    const user = mapKakaoUser(res.data);
    await createUserDocManually(user);
    const customToken = await admin.auth().createCustomToken(user.uid, user);

    return {token: customToken};
  } catch (error) {
    console.error("kakaoCustomAuth error:", error?.response?.data || error);
    throw new Error("Failed to authenticate with Kakao");
  }
};
