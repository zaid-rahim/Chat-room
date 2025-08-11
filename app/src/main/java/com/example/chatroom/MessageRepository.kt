package com.example.chatroom

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepository(private val firestore: FirebaseFirestore) {

    suspend fun sendMessage(roomId: String, message: Message): Result<Unit> = try {
        firestore.collection("rooms").document(roomId)
            .collection("messages").add(message).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    fun getChatMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val subscription = firestore.collection("rooms").document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    trySend(it.documents.map { doc ->
                        doc.toObject(Message::class.java)!! })
                }
            }

        awaitClose { subscription.remove() }
    }
}


/*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class MessageRepository(private val firestore: FirebaseFirestore) {

    suspend fun sendMessage(roomId: String, message: Message): Result<Unit> = try {
        val map = mapOf(
            "text" to message.text,
            "senderId" to message.senderId,
            "senderFirstName" to message.senderFirstName,
            "timestamp" to message.timestamp
        )
        firestore.collection("rooms")
            .document(roomId)
            .collection("messages")
            .add(map).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    /**
     * Real-time observe messages for a room (ordered by timestamp asc).
     * Returns ListenerRegistration so caller can remove the listener.
     */
    fun observeMessages(roomId: String, callback: (List<Message>) -> Unit): ListenerRegistration {
        return firestore.collection("rooms")
            .document(roomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        // map fields manually to avoid Timestamp/type issues
                        val id = doc.id
                        val text = doc.getString("text") ?: ""
                        val senderId = doc.getString("senderId") ?: ""
                        val senderFirstName = doc.getString("senderFirstName") ?: ""
                        val timestamp = doc.getLong("timestamp") ?: 0L
                        Message(id = id, text = text, senderId = senderId, senderFirstName = senderFirstName, timestamp = timestamp)
                    } catch (e: Exception) { null }
                } ?: emptyList()
                callback(list)
            }
    }
}*/
