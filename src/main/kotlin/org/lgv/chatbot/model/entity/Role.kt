package org.lgv.chatbot.model.entity

import jakarta.persistence.*

@Entity
class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var nom: String,

    @Column(nullable = false)
    var nomLogique: String,

    @OneToMany(mappedBy = "role")
    var utilisateurs: MutableList<Utilisateur> = mutableListOf()
)