package org.lgv.chatbot.dto.gemini

data class GeminiChatRequest(
    val contents: List<GeminiContent>
)