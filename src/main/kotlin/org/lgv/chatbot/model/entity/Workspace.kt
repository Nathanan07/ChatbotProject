package org.lgv.chatbot.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Workspace(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var nom: String,

    var dateCreation: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    var utilisateur: Utilisateur,

    @OneToMany(mappedBy = "workspace")
    var clesApi: MutableList<CleAPI> = mutableListOf()
)