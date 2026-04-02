package com.example.llmchatbot.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// cascade delete so removing a user wipes their threads automatically
@Entity(
        tableName = "chat_threads",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("userId")}
)
public class ChatThread {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long userId;
    public String title;     // set from the first message so the list is readable
    public long createdAt;
    public long updatedAt;   // bumped on every new message so we can sort by recent

    public ChatThread(long userId, String title, long createdAt, long updatedAt) {
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}