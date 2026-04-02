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

| | | |
|:---:|:---:|:---:|
| <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/7f98e701-d992-4481-9e55-480d1e61b579" /> | <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/96086c52-cf09-4599-bce7-b92c163d180d" /> | <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/4f0d6e10-840b-48e0-bf23-dbb32b975aa2" /> |
| **Login** | **Create Account** | **Home Page - Chat History** |
| <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/7a8ccc66-f814-428e-94d1-ddc41f7e1179" /> | <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/450b7f4c-68a1-410d-a34a-8889d53c5991" /> | <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/181f24bc-9c6c-47c5-a5ce-debf521de8da" /> | 
| **Home Page <br> Chat History (Dark)** | **Chat (light)** | **Chat (Dark)** |
| <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/9e3cb21e-d5f7-42d6-a763-0e470d576f82" /> | <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/b8a4e687-f935-483c-9bd6-ffe903695930" /> | <img width="250" height="600" alt="image" src="https://github.com/user-attachments/assets/e285b6c8-0724-449a-86fb-988855d0b767" /> |
| **Long Press to Copy Prompt** | **Long Press to Delete Chat** | **Long Press to Copy Message** |

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
All rights reserved. Reuse, redistribution, or reproduction of any part of this codebase requires explicit written permission from the author.
