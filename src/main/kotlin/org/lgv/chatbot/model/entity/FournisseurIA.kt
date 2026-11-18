package org.lgv.chatbot.model.entity

import jakarta.persistence.*

@Entity
class FournisseurIA(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var nom: String,
    var cheminLogo: String? = null,
    var description: String? = null,

    @OneToMany(mappedBy = "fournisseur")
    var clesApi: MutableList<CleAPI> = mutableListOf(),

    @OneToMany(mappedBy = "fournisseur")
    var modelesIA: MutableList<ModeleIA> = mutableListOf(),

)