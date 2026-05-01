# TheChatRoom

TheChatRoom is a Firebase chat-room Android app built with Kotlin and Jetpack Compose. It supports authentication, room listing, room creation, and chat messages stored in Firestore.

## Features

- Sign up screen
- Login screen
- Firebase Authentication
- Firestore chat rooms
- Firestore chat messages
- Chat-room list screen
- Chat screen by room ID
- ViewModels for auth, rooms, and messages
- Compose Navigation

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Compose Navigation
- Firebase Auth
- Cloud Firestore
- Firebase Analytics
- AndroidX Lifecycle ViewModel
- Compose LiveData runtime
- Android Gradle Plugin
- Compile SDK 34
- Target SDK 33
- Minimum SDK 24

## Project Structure

```text
app/src/main/java/eu/tutorials/chatroomapp/
├── Injection.kt
├── MainActivity.kt
├── Screen.kt
├── data/
│   ├── Message.kt
│   ├── MessageRepository.kt
│   ├── Result.kt
│   ├── Room.kt
│   ├── RoomRepository.kt
│   ├── User.kt
│   └── UserRepository.kt
├── screen/
│   ├── SignUp.kt
│   ├── chat.kt
│   ├── chatrooms.kt
│   └── login.kt
└── viewmodel/
    ├── AuthViewModel.kt
    ├── MessageViewModel.kt
    └── RoomViewModel.kt
```

## Setup

1. Open this folder in Android Studio.
2. Add your Firebase configuration file:

```text
app/google-services.json
```

3. Enable Firebase Authentication and Cloud Firestore in the Firebase console.
4. Sync Gradle.
5. Run the `app` configuration.

## Build

```bash
./gradlew assembleDebug
```

## Run Tests

```bash
./gradlew test
```

## Notes

- The app ID is `eu.tutorials.chatroomapp`.
- The app label is `Chat Room App`.
- This project requires Firebase setup before auth and chat features can work.
