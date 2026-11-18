package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleDAO : JpaRepository<Role, Long> {
    fun findByNom(nom: String): Role? // <-- C'est cette ligne qu'il manquait
}