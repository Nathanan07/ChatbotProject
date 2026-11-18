package org.lgv.chatbot.model.entity

import jakarta.persistence.*

@Entity
class ModeleIA(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var nom: String,

    @Column(nullable = false)
    var identifiantModele: String,

    @Column(nullable = false)
    var actif: Boolean,

    @ManyToOne
    @JoinColumn(name = "id_fournisseur", nullable = false)
    var fournisseur: FournisseurIA,

    @OneToMany(mappedBy = "modeleIA")
    var conversations: MutableList<Conversation> = mutableListOf()
)