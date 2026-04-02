package com.example.llmchatbot.network;

import com.example.llmchatbot.network.model.GeminiRequest;
import com.example.llmchatbot.network.model.GeminiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiService {

    // updated to 2.5-flash — 2.0-flash was retired March 3 2026
    @POST("v1beta/models/gemini-2.5-flash-lite:generateContent")
    Call<GeminiResponse> generateContent(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}