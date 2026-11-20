package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.Conversation
import org.springframework.data.jpa.repository.JpaRepository


interface ConversationDAO : JpaRepository<Conversation, Long> {
    fun findAllByOrderByDateModificationDesc(): List<Conversation>

}
