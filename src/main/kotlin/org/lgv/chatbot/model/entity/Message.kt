package org.lgv.chatbot.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Message(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(columnDefinition = "TEXT", nullable = false)
    var contenu: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: TypeMessage,

    var dateEnvoi: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "id_conversation", nullable = false)
    var conversation: Conversation,

    @OneToMany(mappedBy = "message")
    var signalements: MutableList<Signalement> = mutableListOf()
)