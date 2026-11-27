package org.lgv.chatbot.config

import org.lgv.chatbot.model.dao.FournisseurIADAO
import org.lgv.chatbot.model.dao.ModeleIADAO
import org.lgv.chatbot.model.dao.RoleDAO
import org.lgv.chatbot.model.dao.UtilisateurDAO
import org.lgv.chatbot.model.entity.FournisseurIA
import org.lgv.chatbot.model.entity.ModeleIA
import org.lgv.chatbot.model.entity.Role
import org.lgv.chatbot.model.entity.Utilisateur
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Configuration
class DataInitializer(
    private val fournisseurIADAO: FournisseurIADAO,
    private val modeleIADAO: ModeleIADAO,
    private val roleDAO: RoleDAO,
    private val utilisateurDAO: UtilisateurDAO,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun initData(): CommandLineRunner = CommandLineRunner {

        // 1. Initialisation des Rôles (pour les utilisateurs)
        /*val rolesDeBase = listOf("User", "Admin")
        for (nomRole in rolesDeBase) {
            if (roleDAO.findByNom(nomRole) == null) {
                roleDAO.save(Role(nom = nomRole, nomLogique = "ROLE_${nomRole.uppercase()}"))
            }
        }*/

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
                description = "Modèle rapide et léger de Google, idéal pour les questions courantes et les réponses instantanées.",

            ),
            ModeleIA(
                nom = "Gemini 3.8 Pro",
                identifiantModele = "gemini-3.8-flash",
                actif = true,
                fournisseur = gemini,
                urlAPI = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent",
                description = "Modèle polyvalent de Google, pensé pour des réponses plus élaborées et des échanges plus longs.",
            ),
            ModeleIA(
                nom = "Mistral Small",
                identifiantModele = "mistral-small-latest",
                actif = true,
                fournisseur = mistral,
                urlAPI = "https://api.mistral.ai/v1/chat/completions",
                description = "Modèle compact de Mistral AI, rapide et économique pour les tâches du quotidien.",
            ),
            ModeleIA(
                nom = "Mistral Medium",
                identifiantModele = "mistral-medium-latest",
                actif = true,
                fournisseur = mistral,
                urlAPI = "https://api.mistral.ai/v1/chat/completions",
                description = "Modèle équilibré de Mistral AI : un bon compromis entre qualité des réponses et rapidité.",
            )
        )

        // Insertion des modèles s'ils n'existent pas déjà
        for (modele in modeles) {
            if (modeleIADAO.findByNom(modele.nom) == null) {
                modeleIADAO.save(modele)
            }
        }

        // Les rôles
        val roles = roleDAO.findAll()
        if (roles.isEmpty()) {
            roleDAO.save(Role(nom = "Utilisateur", nomLogique ="ROLE_USER" ))
            roleDAO.save(Role(nom = "Administrateur", nomLogique ="ROLE_ADMIN"))
        }
        val roleAdmin = roleDAO.findAll().first { it.nomLogique == "ROLE_ADMIN" }
        val roleUser = roleDAO.findAll().first { it.nomLogique == "ROLE_USER" }

// Les utilisateurs
        val utilisateurs = utilisateurDAO.findAll()
        if (utilisateurs.isEmpty()) {
            utilisateurDAO.save(
                Utilisateur(
                    prenom = "Timothée",
                    nom = "Moulin",
                    email = "admin@email.com",
                    motDePasse = passwordEncoder.encode("admin123")!!,
                    role = roleAdmin,
                    dateCreation = LocalDateTime.now()
                )
            )
            utilisateurDAO.save(
                Utilisateur(
                    nom = "Prane",
                    prenom = "Doli",
                    email = "doli.prane@email.com",
                    motDePasse = passwordEncoder.encode("user123")!!,
                    role = roleUser,
                    dateCreation = LocalDateTime.now()
                )
            )
        }

    }
}