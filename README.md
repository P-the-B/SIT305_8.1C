# LLMChatBot — Android AI Chat Application

An Android chatbot application built with Java that integrates Google's Gemini AI API to deliver intelligent, context-aware conversations. Features user authentication, persistent chat history, dark mode, and a clean Material Design UI.

---

## Features

- **User Authentication** — Sign up and log in with email and password
- **AI-Powered Chat** — Powered by Gemini 2.5 Flash with full conversation context
- **Persistent Chat History** — All threads and messages stored locally via Room database
- **Deletable Threads** — Long press any conversation to delete it
- **Dark Mode** — Toggle switch in the toolbar, persists across sessions
- **Timestamps** — Every message bubble displays a timestamp
- **Inline Validation** — Real-time field validation on login and signup screens
- **Secure Storage** — Encrypted session storage via `EncryptedSharedPreferences`
- **Salted Password Hashing** — SHA-256 with email salt, no plaintext passwords stored

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | Material Design 3, ViewBinding, RecyclerView |
| Database | Room (SQLite) |
| Networking | Retrofit 3, OkHttp |
| AI Backend | Google Gemini 2.5 Flash API |
| Security | EncryptedSharedPreferences, SHA-256 hashing, Network Security Config |
| Architecture | Repository pattern, LiveData, ExecutorService |

---

## Screenshots

> Add your screenshots to a `/screenshots` folder in the repo root and update the filenames below.

| Login | Sign Up | Conversations |
|:---:|:---:|:---:|
| ![Login](screenshots/login.png) | ![Sign Up](screenshots/signup.png) | ![Conversations](screenshots/threads.png) |
| **Chat (Light)** | **Chat (Dark)** | **Dark Mode Toggle** |
| ![Chat Light](screenshots/chat_light.png) | ![Chat Dark](screenshots/chat_dark.png) | ![Dark Toggle](screenshots/dark_toggle.png) |
| **Inline Validation** | **Delete Thread** | **New Conversation** |
| ![Validation](screenshots/validation.png) | ![Delete](screenshots/delete.png) | ![New Chat](screenshots/new_chat.png) |

---

## Getting Started

### Prerequisites

- Android Studio (latest stable)
- Android SDK 26+
- A Google Gemini API key (see below)

### Obtaining a Gemini API Key

1. Go to [Google AI Studio](https://aistudio.google.com)
2. Sign in with your Google account
3. Click **Get API Key** → **Create API key**
4. Select or create a Google Cloud project
5. Copy the generated key — you will need it in the next step

> **Note:** The free tier supports Gemini 2.5 Flash with up to 10 requests per minute and 250 requests per day. No credit card is required to get started.

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/P-the-B/LLMChatBot.git
   cd LLMChatBot
   ```

2. **Add your API key**

   Open `local.properties` in the project root and add:
   ```
   GEMINI_API_KEY=your_api_key_here
   ```

   > `local.properties` is listed in `.gitignore` and will never be committed. Never hardcode your key in source files.

3. **Open in Android Studio**

   Open Android Studio → **File → Open** → select the project folder.

4. **Sync Gradle**

   Android Studio will prompt you to sync. Click **Sync Now**.

5. **Run the app**

   Select an emulator or connected device and click **Run**.

---

## Project Structure

```
app/src/main/java/com/example/llmchatbot/
├── data/
│   ├── db/          # Room DAOs and AppDatabase
│   ├── model/       # Entity classes (User, ChatThread, Message)
│   └── repository/  # UserRepository, ChatRepository
├── network/
│   ├── model/       # GeminiRequest, GeminiResponse POJOs
│   ├── ApiClient.java
│   └── GeminiService.java
├── ui/
│   ├── chat/        # ChatActivity, MessageAdapter
│   ├── login/       # LoginActivity
│   ├── signup/      # SignUpActivity
│   └── threads/     # ThreadListActivity, ThreadAdapter
├── util/
│   ├── HashUtil.java
│   ├── SessionManager.java
│   └── ThemeManager.java
├── LLMChatBotApp.java
└── MainActivity.java
```

---

## Security

- API key loaded from `local.properties` at build time via `BuildConfig` — never stored in source
- Passwords hashed with SHA-256 salted with the user's email
- Session stored in AES-256 encrypted SharedPreferences
- All network traffic forced over HTTPS via `network_security_config.xml`
- Manifest backup disabled to prevent ADB extraction of app data
- All non-launcher activities set to `exported="false"`

---

## AI Usage Declaration

This project was developed with the assistance of Claude (Anthropic) as a coding collaborator. Claude was used for code generation, debugging, and architectural decisions throughout the development process. The student acted as Project Manager, Chief Design Architect, and code reviewer and tester. All AI usage has been declared in accordance with Deakin University's academic integrity policy.

---

## Legal

This project was created for educational purposes as part of Deakin University's SIT305 unit. <br> 
All rights reserved. Reuse, redistribution, or reproduction of any part of this codebase requires explicit written permission from the author
