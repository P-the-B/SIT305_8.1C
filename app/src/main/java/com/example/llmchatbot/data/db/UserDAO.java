package com.example.llmchatbot.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.llmchatbot.model.User;

@Dao
public interface UserDAO {

    @Insert
    long insert(User user);

    // login lookup — email+hash lets us avoid fetching the hash separately
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :hash LIMIT 1")
    User findByEmailAndHash(String email, String hash);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User findById(long id);
}