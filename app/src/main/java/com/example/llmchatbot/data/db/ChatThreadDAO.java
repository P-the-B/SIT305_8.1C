package com.example.llmchatbot.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.llmchatbot.model.ChatThread;

import java.util.List;

@Dao
public interface ChatThreadDAO {

    @Insert
    long insert(ChatThread thread);

    // most recently active thread first
    @Query("SELECT * FROM chat_threads WHERE userId = :userId ORDER BY updatedAt DESC")
    LiveData<List<ChatThread>> getThreadsForUser(long userId);

    @Query("UPDATE chat_threads SET updatedAt = :time WHERE id = :threadId")
    void updateTimestamp(long threadId, long time);

    @Query("UPDATE chat_threads SET title = :title WHERE id = :threadId")
    void updateTitle(long threadId, String title);

    @Query("DELETE FROM chat_threads WHERE id = :threadId")
    void delete(long threadId);
}