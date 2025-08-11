package com.example.chatroom.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.chatroom.Room
import com.example.chatroom.RoomViewModel

@Composable
fun ChatRoomListScreen(
    navController: NavController,
    roomViewModel: RoomViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    val rooms by roomViewModel.rooms.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Chat Rooms", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Display rooms
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rooms) { room ->
                RoomItem(room = room, onJoin = {})
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Room")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // <- corrected to close
                title = { Text("Create a new room") },
                text = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            if (name.isNotBlank()) {
                                roomViewModel.createRoom(name.trim())
                                name = ""
                                showDialog = false
                            }
                        }) { Text("Add") }

                        Button(onClick = { showDialog = false }) { Text("Cancel") }
                    }
                }
            )
        }
    }
}

@Composable
fun RoomItem(room: Room, onJoin: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = room.name)
        Button(onClick = {
            navController.navigate("${Screen.ChatScreen.route}/${room.id}")
        }) {
            Text("Join")
        }
    }
}
