package org.lgv.chatbot.controller

import org.lgv.chatbot.model.dao.ConversationDAO
import org.lgv.chatbot.model.dao.MessageDAO
import org.lgv.chatbot.model.dao.ModeleIADAO
import org.lgv.chatbot.service.IAServiceFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatBotController {
    @GetMapping("/exemple")
    fun exemple(): String {
        return "exemple"
    }
}

