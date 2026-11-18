package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.FournisseurIA
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FournisseurIADAO : JpaRepository<FournisseurIA, Long> {
    fun findByNom(nom: String): FournisseurIA?
}