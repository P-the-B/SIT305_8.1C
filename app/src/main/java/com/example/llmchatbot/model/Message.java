package com.example.llmchatbot.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "messages",
        foreignKeys = @ForeignKey(
                entity = ChatThread.class,
                parentColumns = "id",
                childColumns = "threadId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("threadId")}
)
public class Message {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_FAILED = 1;

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long threadId;
    public String content;
    public boolean isFromUser;
    public long timestamp;
    // tracks whether the API call for this message succeeded
    public int status;

    public Message(long threadId, String content, boolean isFromUser, long timestamp) {
        this.threadId = threadId;
        this.content = content;
        this.isFromUser = isFromUser;
        this.timestamp = timestamp;
        this.status = STATUS_NORMAL;
    }
}