/**
 * Kakao 응답 객체를 SocialUser DTO로 변환합니다.
 * @param {object} data Kakao API 응답 데이터
 * @return {object} SocialUser
 */
function mapKakaoUser(data) {
  return {
    uid: `kakao:${data.id}`,
    provider: "kakao",
    email: data.kakao_account?.email,
    name: data.properties?.nickname,
    profileImage: data.properties?.profile_image,
  };
}

/**
 * Naver 응답 객체를 SocialUser DTO로 변환합니다.
 * @param {object} data Naver API 응답 데이터
 * @return {object} SocialUser
 */
function mapNaverUser(data) {
  const res = data.response;
  return {
    uid: `naver:${res.id}`,
    provider: "naver",
    email: res.email,
    name: res.name,
    profileImage: res.profile_image,
  };
}

module.exports = {mapKakaoUser, mapNaverUser};
