package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.Conversation
import org.lgv.chatbot.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository


interface ConversationDAO : JpaRepository<Conversation, Long> {
    fun findAllByUtilisateurOrderByDateModificationDesc(utilisateur: Utilisateur): List<Conversation>

    fun findByIdAndUtilisateur(id:Long,utilisateur: Utilisateur):Conversation?

}
