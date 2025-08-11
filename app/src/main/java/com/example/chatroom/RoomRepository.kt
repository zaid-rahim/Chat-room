package com.example.chatroom

import com.example.chatroom.Injection
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

// Data model for a room

class RoomRepository {

    private val firestore = Injection.provideFirestore()
    private val roomsCollection = firestore.collection("rooms")

    // Create a new room in Firestore
    suspend fun createRoom(name: String, createdBy: String) {
        val roomId = roomsCollection.document().id
        val room = Room(id = roomId, name = name, createdBy = createdBy)
        roomsCollection.document(roomId).set(room).await()
    }

    // Get all rooms from Firestore
    suspend fun getRooms(): List<Room> {
        val snapshot = roomsCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject<Room>() }
    }
}
