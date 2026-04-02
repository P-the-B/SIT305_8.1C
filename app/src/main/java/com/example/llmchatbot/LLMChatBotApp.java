package com.example.llmchatbot;

import android.app.Application;

import com.example.llmchatbot.util.ThemeManager;

// applies persisted theme once at process start before any activity launches
public class LLMChatBotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new ThemeManager(this).applyTheme();
    }
}