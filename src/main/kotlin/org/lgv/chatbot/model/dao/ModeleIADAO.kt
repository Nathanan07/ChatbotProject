package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.ModeleIA
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ModeleIADAO : JpaRepository<ModeleIA, Long> {
    fun findByNom(nom: String): ModeleIA?
}