package com.example.llmchatbot.network.model;

import java.util.List;

// matches the Gemini generateContent request body structure exactly
public class GeminiRequest {

    public List<Content> contents;

    public GeminiRequest(List<Content> contents) {
        this.contents = contents;
    }

    public static class Content {
        public String role;
        public List<Part> parts;

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }
    }

    public static class Part {
        public String text;

        public Part(String text) {
            this.text = text;
        }
    }
}