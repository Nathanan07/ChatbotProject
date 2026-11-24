package org.lgv.chatbot.dto.gemini

data class GeminiChatResponse(
    val candidates: List<GeminiCandidate>?
)