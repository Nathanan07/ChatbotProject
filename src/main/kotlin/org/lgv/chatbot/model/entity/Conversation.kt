package org.lgv.chatbot.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Conversation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var titre: String,

    var dateCreation: LocalDateTime,

    var dateModification: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    var utilisateur: Utilisateur,

    @ManyToOne
    @JoinColumn(name = "id_modele", nullable = false)
    var modeleIA: ModeleIA,

    @ManyToOne
    @JoinColumn(name = "id_cle", nullable = true)
    var cleApi: CleAPI? = null,

    @OneToMany(mappedBy = "conversation")
    var messages: MutableList<Message> = mutableListOf(),

)