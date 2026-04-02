package com.example.llmchatbot.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.llmchatbot.data.repository.ChatRepository;
import com.example.llmchatbot.databinding.ActivityChatBinding;
import com.example.llmchatbot.model.Message;
import com.example.llmchatbot.util.SessionManager;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_THREAD_ID = "extra_thread_id";

    private ActivityChatBinding binding;
    private ChatRepository chatRepository;
    private SessionManager sessionManager;
    private MessageAdapter adapter;
    private long threadId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chat");
        }

        chatRepository = new ChatRepository(this);
        sessionManager = new SessionManager(this);
        threadId = getIntent().getLongExtra(EXTRA_THREAD_ID, -1L);

        setupRecyclerView();

        if (threadId != -1L) {
            observeMessages();
        }

        binding.btnSend.setOnClickListener(v -> handleSend());
    }

    private void setupRecyclerView() {
        adapter = new MessageAdapter(this::retryMessage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.rvMessages.setLayoutManager(layoutManager);
        binding.rvMessages.setAdapter(adapter);
    }

    private void retryMessage(Message failedMessage) {
        // delete the failed message then resend its content as a fresh message
        chatRepository.deleteMessage(failedMessage.id);
        binding.btnSend.setEnabled(false);
        saveUserMessageAndQuery(failedMessage.content, System.currentTimeMillis());
    }

    private void observeMessages() {
        chatRepository.getMessagesForThread(threadId).observe(this, messages -> {
            adapter.submitList(messages);
            if (!messages.isEmpty()) {
                binding.rvMessages.smoothScrollToPosition(messages.size() - 1);
            }
        });
    }

    private void handleSend() {
        String text = binding.etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        binding.etMessage.setText("");
        binding.btnSend.setEnabled(false);

        long now = System.currentTimeMillis();

        if (threadId == -1L) {
            chatRepository.createThread(sessionManager.getUserId(), text, newThreadId -> {
                threadId = newThreadId;
                runOnUiThread(this::observeMessages);
                saveUserMessageAndQuery(text, now);
            });
        } else {
            saveUserMessageAndQuery(text, now);
        }
    }

    private void saveUserMessageAndQuery(String text, long timestamp) {
        Message userMessage = new Message(threadId, text, true, timestamp);
        chatRepository.saveMessage(userMessage, userMessageId -> {
            // pass userMessageId so the repository can mark it failed if the API call fails
            chatRepository.sendToGemini(threadId, userMessageId, reply -> {
                runOnUiThread(() -> binding.btnSend.setEnabled(true));
                if (reply != null) {
                    long replyTime = System.currentTimeMillis();
                    Message aiMessage = new Message(threadId, reply, false, replyTime);
                    chatRepository.saveMessage(aiMessage, null);
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}