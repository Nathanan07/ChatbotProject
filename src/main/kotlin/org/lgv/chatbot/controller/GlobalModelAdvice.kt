package org.lgv.chatbot.controller

import org.lgv.chatbot.model.dao.ConversationDAO
import org.lgv.chatbot.model.dao.UtilisateurDAO
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

// Ajoute l'utilisateur connecté et son historique à TOUTES les vues,
// pour que la barre latérale (navbar) puisse afficher les anciennes conversations partout.
@ControllerAdvice
class GlobalModelAdvice(
    private val utilisateurDAO: UtilisateurDAO,
    private val conversationDAO: ConversationDAO
) {

    @ModelAttribute
    fun ajouterDonneesCommunes(model: Model, authentication: Authentication?) {
        if (authentication == null ||
            authentication is AnonymousAuthenticationToken ||
            !authentication.isAuthenticated
        ) return

        val utilisateur = utilisateurDAO.findByEmailIgnoreCase(authentication.name) ?: return

        model.addAttribute("utilisateur", utilisateur)
        model.addAttribute(
            "conversations",
            conversationDAO.findAllByUtilisateurOrderByDateModificationDesc(utilisateur)
        )
    }
}
