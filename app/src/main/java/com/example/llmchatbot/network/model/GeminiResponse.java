package com.example.llmchatbot.network.model;

import java.util.List;

// only mapping the fields we actually use — Gemini returns a lot more
public class GeminiResponse {

    public List<Candidate> candidates;

    public static class Candidate {
        public Content content;
    }

    public static class Content {
        public List<Part> parts;
    }

    public static class Part {
        public String text;
    }

    // convenience so ChatActivity doesn't have to drill through nulls
    public String getFirstText() {
        if (candidates == null || candidates.isEmpty()) return null;
        if (candidates.get(0).content == null) return null;
        if (candidates.get(0).content.parts == null || candidates.get(0).content.parts.isEmpty()) return null;
        return candidates.get(0).content.parts.get(0).text;
    }
}