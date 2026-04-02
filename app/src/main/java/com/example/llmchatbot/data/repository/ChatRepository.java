package com.example.llmchatbot.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.llmchatbot.BuildConfig;
import com.example.llmchatbot.data.db.AppDatabase;
import com.example.llmchatbot.data.db.ChatThreadDAO;
import com.example.llmchatbot.data.db.MessageDAO;
import com.example.llmchatbot.model.ChatThread;
import com.example.llmchatbot.model.Message;
import com.example.llmchatbot.network.ApiClient;
import com.example.llmchatbot.network.GeminiService;
import com.example.llmchatbot.network.model.GeminiRequest;
import com.example.llmchatbot.network.model.GeminiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {

    private static final String TAG = "ChatRepository";

    private final ChatThreadDAO threadDao;
    private final MessageDAO messageDao;
    private final GeminiService geminiService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface ResultCallback<T> {
        void onResult(T result);
    }

    public ChatRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        threadDao = db.chatThreadDao();
        messageDao = db.messageDao();
        geminiService = ApiClient.getInstance().getGeminiService();
    }

    public void createThread(long userId, String firstMessage, ResultCallback<Long> callback) {
        executor.execute(() -> {
            long now = System.currentTimeMillis();
            String title = firstMessage.length() > 40
                    ? firstMessage.substring(0, 40) + "…"
                    : firstMessage;
            ChatThread thread = new ChatThread(userId, title, now, now);
            long threadId = threadDao.insert(thread);
            callback.onResult(threadId);
        });
    }

    public LiveData<List<ChatThread>> getThreadsForUser(long userId) {
        return threadDao.getThreadsForUser(userId);
    }

    public void deleteThread(long threadId) {
        executor.execute(() -> threadDao.delete(threadId));
    }

    public LiveData<List<Message>> getMessagesForThread(long threadId) {
        return messageDao.getMessagesForThread(threadId);
    }

    public void saveMessage(Message message, ResultCallback<Long> callback) {
        executor.execute(() -> {
            long id = messageDao.insert(message);
            threadDao.updateTimestamp(message.threadId, message.timestamp);
            if (callback != null) callback.onResult(id);
        });
    }

    public void markMessageFailed(long messageId) {
        executor.execute(() -> messageDao.updateStatus(messageId, Message.STATUS_FAILED));
    }

    public void deleteMessage(long messageId) {
        executor.execute(() -> messageDao.deleteById(messageId));
    }

    public void sendToGemini(long threadId, long userMessageId, ResultCallback<String> callback) {
        executor.execute(() -> {
            List<Message> history = messageDao.getMessagesForThreadSync(threadId);
            // exclude failed messages — they should never be sent as context
            List<GeminiRequest.Content> contents = buildContents(history);
            GeminiRequest request = new GeminiRequest(contents);

            geminiService.generateContent(BuildConfig.GEMINI_API_KEY, request)
                    .enqueue(new Callback<GeminiResponse>() {
                        @Override
                        public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String text = response.body().getFirstText();
                                callback.onResult(text != null ? text : "Sorry, I could not generate a response.");
                            } else {
                                // mark the user message as failed so it shows the retry button
                                markMessageFailed(userMessageId);
                                callback.onResult(null);
                            }
                        }

                        @Override
                        public void onFailure(Call<GeminiResponse> call, Throwable t) {
                            markMessageFailed(userMessageId);
                            callback.onResult(null);
                        }
                    });
        });
    }

    private List<GeminiRequest.Content> buildContents(List<Message> messages) {
        List<GeminiRequest.Content> contents = new ArrayList<>();
        for (Message m : messages) {
            // skip failed messages — they must not pollute the conversation history
            if (m.status == Message.STATUS_FAILED) continue;
            String role = m.isFromUser ? "user" : "model";
            List<GeminiRequest.Part> parts = new ArrayList<>();
            parts.add(new GeminiRequest.Part(m.content));
            contents.add(new GeminiRequest.Content(role, parts));
        }
        return contents;
    }
}