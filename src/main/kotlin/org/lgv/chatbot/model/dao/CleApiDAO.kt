package org.lgv.chatbot.model.dao

import org.lgv.chatbot.model.entity.CleAPI
import org.springframework.data.jpa.repository.JpaRepository

interface CleApiDAO : JpaRepository<CleAPI, Long> {
}
