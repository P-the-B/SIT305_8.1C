package com.example.llmchatbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.llmchatbot.ui.login.LoginActivity;
import com.example.llmchatbot.ui.threads.ThreadListActivity;
import com.example.llmchatbot.util.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);

        // skip login screen entirely if session is still valid
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, ThreadListActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish(); // remove MainActivity from back stack immediately
    }
}