package com.e1i3.spender.feature.mypage.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.util.Date

class FriendRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")
    private val codesCollection = FirebaseFirestore.getInstance().collection("codes")

    suspend fun getCode(): Pair<String, Timestamp> {
        val uid = auth.currentUser?.uid
        val snapshot = codesCollection.whereEqualTo("userId", uid).get().await()
        if (snapshot.isEmpty) {
            val newCode = generateCode()
            val created = Timestamp.now()
            val expired = Timestamp(Date(created.toDate().time + 30 * 60 * 1000))
            val newDoc = mapOf(
                "userId" to uid,
                "createdAt" to created,
                "expiredAt" to expired
            )
            codesCollection.document(newCode).set(newDoc).await()
            return newCode to expired
        } else {
            val data = snapshot.documents[0]
            val expiredAt = data.getTimestamp("expiredAt")
            return if (expiredAt != null && expiredAt.toDate().after(Date())) {
                data.id to expiredAt
            } else {
                val newCode = generateCode()
                val created = Timestamp.now()
                val expired = Timestamp(Date(created.toDate().time + 30 * 60 * 1000))
                val newDoc = mapOf(
                    "userId" to uid,
                    "createdAt" to created,
                    "expiredAt" to expired
                )
                data.reference.delete().await()
                codesCollection.document(newCode).set(newDoc).await()
                return newCode to expired
            }
        }
    }

    suspend fun refresh(): Pair<String, Timestamp> {
        val uid = auth.currentUser?.uid
        val snapshot = codesCollection.whereEqualTo("userId", uid).get().await()
        val newCode = generateCode()
        val created = Timestamp.now()
        val expired = Timestamp(Date(created.toDate().time + 30 * 60 * 1000))
        val newDoc = mapOf(
            "userId" to uid,
            "createdAt" to created,
            "expiredAt" to expired
        )
        snapshot.documents[0].reference.delete().await()
        codesCollection.document(newCode).set(newDoc).await()
        return newCode to expired
    }

    suspend fun addFriend(code: String): String {
        val uid = auth.currentUser?.uid
        val codeSearch = codesCollection.document(code).get().await()
        if (!codeSearch.exists()) return "코드가 잘못되었습니다. 다시 확인해주세요."
        if ((codeSearch.data?.get("expiredAt") as Timestamp).toDate().before(Timestamp.now().toDate())) return "코드가 만료되었습니다."
        val friendId = codeSearch.data?.get("userId").toString()
        if (friendId.isEmpty()) return "사용자를 찾을 수 없습니다. 코드를 확인해주세요."
        if (friendId == uid) return "자신은 친구로 추가할 수 없습니다."
        val friendDoc = usersCollection.document(friendId).get().await()
        if (!friendDoc.exists()) return "사용자를 찾을 수 없습니다. 코드를 확인해주세요."
        val myDoc = usersCollection.document(uid!!).collection("friends").document(friendId).get().await()
        if (myDoc.exists()) return "이미 친구인 유저입니다."

        val myUserInfo = usersCollection.document(uid).get().await()
        val myNickName = myUserInfo.data?.get("nickname").toString()
        val myProfileUrl = myUserInfo.data?.get("photoUrl").toString()
        val friendNickName = friendDoc.data?.get("nickname").toString()
        val friendProfileUrl = friendDoc.data?.get("photoUrl").toString()

        usersCollection.document(friendId).collection("friends").document(uid).set(
            mapOf(
                "status" to "FRIENDS",
                "connectedAt" to Timestamp.now(),
                "nickname" to myNickName,
                "photoUrl" to myProfileUrl
            )
        ).await()

        usersCollection.document(uid).collection("friends").document(friendId).set(
            mapOf(
                "status" to "FRIENDS",
                "connectedAt" to Timestamp.now(),
                "nickname" to friendNickName,
                "photoUrl" to friendProfileUrl
            )
        ).await()
        return "친구 추가에 성공했습니다."
    }

    private fun generateCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1 .. 8)
            .map { chars.random() }
            .joinToString("")
    }
}