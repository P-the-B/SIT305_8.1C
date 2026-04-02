package com.example.llmchatbot.data.repository;

import android.content.Context;

import com.example.llmchatbot.data.db.AppDatabase;
import com.example.llmchatbot.data.db.UserDAO;
import com.example.llmchatbot.model.User;
import com.example.llmchatbot.util.HashUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDAO userDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        userDao = AppDatabase.getInstance(context).userDao();
    }

    public interface Callback<T> {
        void onResult(T result);
    }

    public void register(String name, String email, String password, Callback<Long> callback) {
        executor.execute(() -> {
            User existing = userDao.findByEmail(email.toLowerCase().trim());
            if (existing != null) {
                callback.onResult(-1L); // duplicate email
                return;
            }
            // salt hash with email so identical passwords don't produce identical hashes
            String hash = HashUtil.sha256WithSalt(password, email);
            User user = new User(name, email.toLowerCase().trim(), hash, System.currentTimeMillis());
            long id = userDao.insert(user);
            callback.onResult(id);
        });
    }

    public void login(String email, String password, Callback<User> callback) {
        executor.execute(() -> {
            String hash = HashUtil.sha256WithSalt(password, email);
            User user = userDao.findByEmailAndHash(email.toLowerCase().trim(), hash);
            callback.onResult(user);
        });
    }

    public void getUserById(long id, Callback<User> callback) {
        executor.execute(() -> callback.onResult(userDao.findById(id)));
    }
}