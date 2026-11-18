package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageDAO : JpaRepository<Message, Long> {
}
