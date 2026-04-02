package com.example.llmchatbot.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SessionManager {

    private static final String PREF_NAME = "chatbot_session";
    private static final String KEY_USER_ID = "userId";
    private static final long NO_USER = -1L;
    private static final String TAG = "SessionManager";

    private final SharedPreferences prefs;
    private final Context context;

    public SessionManager(Context context) {
        this.context = context.getApplicationContext();
        SharedPreferences temp;
        try {
            MasterKey masterKey = new MasterKey.Builder(this.context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            temp = EncryptedSharedPreferences.create(
                    this.context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            temp = this.context.getSharedPreferences(PREF_NAME + "_fallback", Context.MODE_PRIVATE);
        }
        prefs = temp;
    }

    public void saveUserId(long userId) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply();
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, NO_USER);
    }

    public boolean isLoggedIn() {
        return getUserId() != NO_USER;
    }

    public void logout() {
        try {
            // remove the specific key rather than clear() — clear() causes a decrypt
            // crash on EncryptedSharedPreferences when the keystore key has rotated
            prefs.edit().remove(KEY_USER_ID).apply();
        } catch (Exception e) {
            // nuclear option — delete the encrypted prefs file entirely
            context.deleteSharedPreferences(PREF_NAME);
        }
    }
}