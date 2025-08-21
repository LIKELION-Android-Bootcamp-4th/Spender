const { onDocumentUpdated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");
const db = admin.firestore();

// 기본 프로필 이미지
const DEFAULT_PHOTO_URL =
  "https://firebasestorage.googleapis.com/v0/b/spender-5f3c4.firebasestorage.app/o/spender.png?alt=media&token=30002d1f-9c06-44d6-9492-e8521f2a94f2";

exports.syncUserProfileToFriends = onDocumentUpdated("users/{userId}", async (event) => {
    const change = event.data;
    const userId = event.params.userId;
    const beforeData = change.before.data();
    const afterData = change.after.data();
    
    // 닉네임, 프로필 사진 (없으면 기본 이미지로)
    const newNickname = afterData.nickname;
    const newPhotoUrl = afterData.photoUrl || DEFAULT_PHOTO_URL;
    const beforeNickname = beforeData.nickname;
    const beforePhotoUrl = beforeData.photoUrl || DEFAULT_PHOTO_URL;
    
    // 둘 다 변경이 없으면 종료
    if (beforeNickname === newNickname && beforePhotoUrl === newPhotoUrl) {
      console.log(`No profile changes for user ${userId}`);
      return null;
    }
    
    console.log(`Updating profile for user ${userId}: ${beforeNickname} -> ${newNickname}`);
    
    try {
      // 방법 1: collectionGroup을 사용한 효율적인 방법
      const friendsQuery = await db.collectionGroup("friends")
        .where(admin.firestore.FieldPath.documentId(), "==", userId)
        .get();
        
      if (friendsQuery.empty) {
        console.log(`No friend references found for user ${userId}`);
        return null;
      }
      
      const batch = db.batch();
      let updateCount = 0;
      
      // 존재하는 친구 문서들만 업데이트
      friendsQuery.forEach(friendDoc => {
        if (friendDoc.exists) {
          batch.update(friendDoc.ref, {
            nickname: newNickname,
            photoUrl: newPhotoUrl
          });
          updateCount++;
        }
      });
      
      if (updateCount > 0) {
        await batch.commit();
        console.log(`Updated ${updateCount} friend references for user ${userId}`);
      }
      
      return null;
      
    } catch (error) {
      console.error(`Error updating friend references for user ${userId}:`, error);
      
      // collectionGroup이 실패한 경우 기존 방법으로 fallback
      return await fallbackUpdate(userId, newNickname, newPhotoUrl);
    }
});

// Fallback 함수: 기존 방식으로 처리
async function fallbackUpdate(userId, newNickname, newPhotoUrl) {
  console.log(`Using fallback method for user ${userId}`);
  
  try {
    const usersSnap = await db.collection("users").get();
    const batch = db.batch();
    let updateCount = 0;
    
    // 존재하는 친구 문서들만 배치에 추가
    const existingFriendRefs = [];
    
    // 먼저 존재하는 친구 문서들을 찾기
    for (const userDoc of usersSnap.docs) {
      const friendRef = userDoc.ref.collection("friends").doc(userId);
      
      try {
        const friendDoc = await friendRef.get();
        if (friendDoc.exists) {
          existingFriendRefs.push(friendRef);
        }
      } catch (docError) {
        console.log(`Skipping friend ref for user ${userDoc.id}: ${docError.message}`);
        continue;
      }
    }
    
    // 존재하는 문서들만 배치 업데이트
    existingFriendRefs.forEach(friendRef => {
      batch.update(friendRef, {
        nickname: newNickname,
        photoUrl: newPhotoUrl
      });
      updateCount++;
    });
    
    if (updateCount > 0) {
      await batch.commit();
      console.log(`Fallback: Updated ${updateCount} friend references for user ${userId}`);
    } else {
      console.log(`Fallback: No friend references found for user ${userId}`);
    }
    
    return null;
    
  } catch (error) {
    console.error(`Fallback method also failed for user ${userId}:`, error);
    return null;
  }
}