package org.lgv.chatbot.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping("/exemple")
    fun exemple(): String {
        return "exemple"
    }

    @GetMapping("/login")
    fun login(): String {
        return "auth/login"
    }
}
