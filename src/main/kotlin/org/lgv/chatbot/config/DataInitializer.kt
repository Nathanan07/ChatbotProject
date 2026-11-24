package org.lgv.chatbot.config

import org.lgv.chatbot.model.dao.FournisseurIADAO
import org.lgv.chatbot.model.dao.ModeleIADAO
import org.lgv.chatbot.model.dao.RoleDAO
import org.lgv.chatbot.model.entity.FournisseurIA
import org.lgv.chatbot.model.entity.ModeleIA
import org.lgv.chatbot.model.entity.Role
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer(
    private val fournisseurIADAO: FournisseurIADAO,
    private val modeleIADAO: ModeleIADAO,
    private val roleDAO: RoleDAO
) {

    @Bean
    fun initData(): CommandLineRunner = CommandLineRunner {

        // 1. Initialisation des Rôles (pour les utilisateurs)
        val rolesDeBase = listOf("ROLE_USER", "ROLE_ADMIN")
        for (nomRole in rolesDeBase) {
            if (roleDAO.findByNom(nomRole) == null) {
                roleDAO.save(Role(nom = nomRole))
            }
        }

        // 2. Initialisation des Fournisseurs IA
        val mistral = fournisseurIADAO.findByNom("Mistral") ?: fournisseurIADAO.save(
            FournisseurIA(
                nom = "Mistral",
                cheminLogo = "/images/mistral.png",
                description = "Fournisseur français de modèles d'intelligence artificielle."
            )
        )

        val gemini = fournisseurIADAO.findByNom("Gemini") ?: fournisseurIADAO.save(
            FournisseurIA(
                nom = "Gemini",
                cheminLogo = "/images/gemini.png",
                description = "Modèles d'intelligence artificielle de Google."
            )
        )

        // 3. Initialisation des Modèles IA reliés à leurs fournisseurs
        val modeles = listOf(
            ModeleIA(
                nom = "Gemini 3.5 Flash",
                identifiantModele = "gemini-3.5-flash-lite",
                actif = true,
                fournisseur = gemini,
                urlAPI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent",

            ),
            ModeleIA(
                nom = "Gemini 3.8 Pro",
                identifiantModele = "gemini-3.8-flash",
                actif = true,
                fournisseur = gemini,
                urlAPI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent",
            ),
            ModeleIA(
                nom = "Mistral Small",
                identifiantModele = "mistral-small-latest",
                actif = true,
                fournisseur = mistral,
                urlAPI = "https://api.mistral.ai/v1/chat/completions",
            ),
            ModeleIA(
                nom = "Mistral Medium",
                identifiantModele = "mistral-medium-latest",
                actif = true,
                fournisseur = mistral,
                urlAPI = "https://api.mistral.ai/v1/chat/completions",
            )
        )

        // Insertion des modèles s'ils n'existent pas déjà
        for (modele in modeles) {
            if (modeleIADAO.findByNom(modele.nom) == null) {
                modeleIADAO.save(modele)
            }
        }
    }
}