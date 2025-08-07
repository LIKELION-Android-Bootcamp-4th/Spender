/* eslint-disable no-console */
const admin = require("firebase-admin");
const axios = require("axios");
const {mapNaverUser} = require("./utils/social-mapper");
const {createUserDocManually} = require("./create-user-doc");

module.exports = async (data) => {
  try {
    const accessToken = data.accessToken;
    if (!accessToken) {
      throw new Error("accessToken is required");
    }

    const res = await axios.get("https://openapi.naver.com/v1/nid/me", {
      headers: {Authorization: `Bearer ${accessToken}`},
    });

    const user = mapNaverUser(res.data);
    await createUserDocManually(user);
    const customToken = await admin.auth().createCustomToken(user.uid, user);

    return {token: customToken};
  } catch (error) {
    console.error("naverCustomAuth error:", error);
    throw new Error("Failed to authenticate with Naver");
  }
};
