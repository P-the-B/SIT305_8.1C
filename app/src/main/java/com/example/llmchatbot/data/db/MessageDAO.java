package com.example.llmchatbot.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.llmchatbot.model.Message;

import java.util.List;

@Dao
public interface MessageDAO {

    @Insert
    long insert(Message message);

    @Query("SELECT * FROM messages WHERE threadId = :threadId ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesForThread(long threadId);

    @Query("SELECT * FROM messages WHERE threadId = :threadId ORDER BY timestamp ASC")
    List<Message> getMessagesForThreadSync(long threadId);

    // used to mark a message as failed without deleting it from the UI
    @Query("UPDATE messages SET status = :status WHERE id = :messageId")
    void updateStatus(long messageId, int status);

    // used when retrying — wipes the failed message before resending
    @Query("DELETE FROM messages WHERE id = :messageId")
    void deleteById(long messageId);
}