require('dotenv').config();

const admin = require("firebase-admin");
admin.initializeApp();

const {onCall} = require("firebase-functions/v1/https");
const auth = require("firebase-functions/v1/auth");

const handleUserCreate = require("./create-user-doc-trigger");
const handleKakao = require("./kakao-auth");
const handleNaver = require("./naver-auth");
const generateMonthlyReports = require("./report-generator");

exports.kakaoCustomAuth = onCall(handleKakao);
exports.naverCustomAuth = onCall(handleNaver);
exports.createUserDoc = auth.user().onCreate(handleUserCreate);
exports.generateMonthlyReports = generateMonthlyReports;
