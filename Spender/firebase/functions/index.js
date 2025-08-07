const {onCall} = require("firebase-functions/v1/https");
const auth = require("firebase-functions/v1/auth");
const admin = require("firebase-admin");

const handleUserCreate = require("./create-user-doc-trigger");
const handleKakao = require("./kakao-auth");
const handleNaver = require("./naver-auth");

admin.initializeApp();

exports.kakaoCustomAuth = onCall(handleKakao);
exports.naverCustomAuth = onCall(handleNaver);
exports.createUserDoc = auth.user().onCreate(handleUserCreate);
