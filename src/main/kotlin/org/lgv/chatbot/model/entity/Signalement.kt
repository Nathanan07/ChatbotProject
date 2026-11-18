package org.lgv.chatbot.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Signalement(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var motif: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Column(nullable = false)
    var statut: String,

    var dateCreation: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    var utilisateur: Utilisateur,

    @ManyToOne
    @JoinColumn(name = "id_message", nullable = true)
    var message: Message? = null
)