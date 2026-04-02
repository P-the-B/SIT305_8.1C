package com.example.llmchatbot.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// email must be unique — used as the login identifier
@Entity(tableName = "users", indices = {@Index(value = "email", unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String email;
    public String passwordHash;
    public long createdAt;

    public User(String name, String email, String passwordHash, long createdAt) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }
}