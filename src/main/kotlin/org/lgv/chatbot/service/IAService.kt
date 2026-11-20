package org.lgv.chatbot.service

import org.lgv.chatbot.model.entity.ModeleIA

interface IAService {

    // Identifie le fournisseur pris en charge
    fun getFournisseur(): String

    // Envoie un prompt au modèle choisi
    fun envoyerPrompt(prompt: String, modele: ModeleIA): Any
}
