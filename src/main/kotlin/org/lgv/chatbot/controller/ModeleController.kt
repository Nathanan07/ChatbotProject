package org.lgv.chatbot.controller

import org.lgv.chatbot.model.dao.FournisseurIADAO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ModeleController(
    private val fournisseurIADAO: FournisseurIADAO
) {

    // Page de présentation des IA disponibles (accessible à tous les utilisateurs connectés)
    @GetMapping("/modeles")
    fun presentation(model: Model): String {
        // On ne garde que les fournisseurs qui ont au moins un modèle actif
        val fournisseurs = fournisseurIADAO.findAll()
            .filter { fournisseur -> fournisseur.modelesIA.any { it.actif } }

        model.addAttribute("fournisseurs", fournisseurs)
        return "utilisateur/modeles"
    }
}
