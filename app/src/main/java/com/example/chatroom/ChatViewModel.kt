package com.example.chatroom

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatViewModel(
    private val repo: MessageRepository = MessageRepository(Injection.instance()),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    private var listener: ListenerRegistration? = null
    private var currentUserFirstName: String = ""

    /**
     * Call when screen starts. This starts the realtime listener.
     */
    fun start(roomId: String) {
        // fetch cached user first name once (non-blocking)
        viewModelScope.launch {
            currentUserFirstName = fetchCurrentUserFirstName()
        }

        listener?.remove()
        listener = repo.observeMessages(roomId) { list ->
            val uid = auth.currentUser?.uid
            val mapped = list.map { it.copy(isSentByCurrentUser = it.senderId == uid) }
            _messages.postValue(mapped)
        }
    }

    fun stop() {
        listener?.remove()
        listener = null
    }

    fun sendMessage(roomId: String, text: String) {
        val uid = auth.currentUser?.uid ?: return
        val name = currentUserFirstName.ifBlank { auth.currentUser?.displayName ?: "User" }
        val message = Message(
            text = text,
            senderId = uid,
            senderFirstName = name,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repo.sendMessage(roomId, message)
        }
    }

    private suspend fun fetchCurrentUserFirstName(): String {
        val email = auth.currentUser?.email ?: return ""
        return try {
            val doc = Injection.instance()
                .collection("users")
                .document(email)
                .get()
                .await()
            doc.getString("firstName") ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }
}
