package org.lgv.chatbot.model.entity

import jakarta.persistence.*

@Entity
class CleAPI(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var nom: String,

    @Column(nullable = false)
    var cleChiffree: String,

    var plafondConsommation: Double,

    @Column(nullable = false)
    var active: Boolean,

    @ManyToOne
    @JoinColumn(name = "id_workspace", nullable = false)
    var workspace: Workspace,

    @ManyToOne
    @JoinColumn(name = "id_fournisseur", nullable = false)
    var fournisseur: FournisseurIA,

    @OneToMany(mappedBy = "cleApi")
    var conversations: MutableList<Conversation> = mutableListOf()
)