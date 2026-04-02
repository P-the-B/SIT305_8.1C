package com.example.llmchatbot.ui.threads;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.llmchatbot.R;
import com.example.llmchatbot.data.repository.ChatRepository;
import com.example.llmchatbot.data.repository.UserRepository;
import com.example.llmchatbot.databinding.ActivityThreadListBinding;
import com.example.llmchatbot.ui.chat.ChatActivity;
import com.example.llmchatbot.ui.login.LoginActivity;
import com.example.llmchatbot.util.SessionManager;
import com.example.llmchatbot.util.ThemeManager;

public class ThreadListActivity extends AppCompatActivity {

    private ActivityThreadListBinding binding;
    private ChatRepository chatRepository;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private ThemeManager themeManager;
    private ThreadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThreadListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        sessionManager = new SessionManager(this);
        chatRepository = new ChatRepository(this);
        userRepository = new UserRepository(this);
        themeManager = new ThemeManager(this);

        if (!sessionManager.isLoggedIn()) {
            goToLogin();
            return;
        }

        setupDarkModeSwitch();
        setupRecyclerView();
        observeThreads();
        loadUserName();

        binding.fabNewChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_THREAD_ID, -1L);
            startActivity(intent);
        });
    }

    private void setupDarkModeSwitch() {
        SwitchCompat toggle = binding.switchDarkMode;
        // set state before attaching listener to avoid firing on programmatic check
        toggle.setChecked(themeManager.isDarkMode());
        toggle.setOnCheckedChangeListener((btn, isChecked) ->
                themeManager.setDarkMode(isChecked));
    }

    private void setupRecyclerView() {
        adapter = new ThreadAdapter(
                thread -> {
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(ChatActivity.EXTRA_THREAD_ID, thread.id);
                    startActivity(intent);
                },
                thread -> new AlertDialog.Builder(this)
                        .setTitle("Delete conversation")
                        .setMessage("This will permanently delete this chat. Continue?")
                        .setPositiveButton("Delete", (d, w) -> chatRepository.deleteThread(thread.id))
                        .setNegativeButton("Cancel", null)
                        .show()
        );
        binding.rvThreads.setLayoutManager(new LinearLayoutManager(this));
        binding.rvThreads.setAdapter(adapter);
    }

    private void observeThreads() {
        long userId = sessionManager.getUserId();
        chatRepository.getThreadsForUser(userId).observe(this, threads -> {
            adapter.submitList(threads);
            binding.tvEmpty.setVisibility(
                    threads.isEmpty()
                            ? android.view.View.VISIBLE
                            : android.view.View.GONE
            );
        });
    }

    private void loadUserName() {
        userRepository.getUserById(sessionManager.getUserId(), user ->
                runOnUiThread(() -> {
                    if (user != null) {
                        binding.tvToolbarTitle.setText("Hi, " + user.name);
                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thread_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            sessionManager.logout();
            goToLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        // clear the back stack so the user can't navigate back into the app after logout
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}