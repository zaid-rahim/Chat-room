package com.example.chatroom

import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageRepository = MessageRepository(Injection.instance())
    private val userRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is SnapshotApplyResult.Success -> _currentUser.value = result.data
                is Error -> { /* handle error */ }
            }
        }
    }

    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }

    fun loadMessages() {
        viewModelScope.launch {
            _roomId.value?.let {
                messageRepository.getChatMessages(it).collect { messagesList ->
                    _messages.value = messagesList
                }
            }
        }
    }

    fun sendMessage(text: String) {
        _currentUser.value?.let { user ->
            val message = Message(
                senderFirstName = user.firstName,
                senderId = user.email,
                text = text,
                timestamp = System.currentTimeMillis(),
                isSentByCurrentUser = true
            )
            viewModelScope.launch {
                messageRepository.sendMessage(_roomId.value ?: "", message)
            }
        }
    }
}
