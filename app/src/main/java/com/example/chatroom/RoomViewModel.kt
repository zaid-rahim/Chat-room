package com.example.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroom.Room
import com.example.chatroom.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val repository = RoomRepository()

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    // Load rooms from Firestore
    fun loadRooms() {
        viewModelScope.launch {
            try {
                val roomList = repository.getRooms()
                _rooms.value = roomList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Create new room and reload list
    fun createRoom(name: String, createdBy: String) {
        viewModelScope.launch {
            try {
                repository.createRoom(name, createdBy)
                loadRooms() // Refresh list after adding
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
