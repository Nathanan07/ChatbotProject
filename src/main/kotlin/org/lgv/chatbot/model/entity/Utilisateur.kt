package org.lgv.chatbot.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Utilisateur(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var prenom: String,

    @Column(nullable = false)
    var nom: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var motDePasse: String,

    var dateCreation: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    var role: Role,

    @OneToMany(mappedBy = "utilisateur")
    var workspaces: MutableList<Workspace> = mutableListOf(),

//    @OneToMany(mappedBy = "utilisateur")
//    var conversations: MutableList<Conversation> = mutableListOf(),

    @OneToMany(mappedBy = "utilisateur", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var conversations: MutableList<Conversation> = mutableListOf(),

    @OneToMany(mappedBy = "utilisateur")
    var signalements: MutableList<Signalement> = mutableListOf()
)