package org.lgv.chatbot.controller

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.lgv.chatbot.model.dao.ConversationDAO
import org.lgv.chatbot.model.dao.MessageDAO
import org.lgv.chatbot.model.dao.ModeleIADAO
import org.lgv.chatbot.model.dao.UtilisateurDAO
import org.lgv.chatbot.model.entity.Conversation
import org.lgv.chatbot.model.entity.*
import org.lgv.chatbot.service.IAServiceFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
@Controller
class  ChatController(
    private val iaServiceFactory: IAServiceFactory,
    private val conversationDAO: ConversationDAO,
    private val messageDAO: MessageDAO,
    private val modeleIADAO: ModeleIADAO,
    private val utilisateurDAO: UtilisateurDAO
) {
    // Les méthodes du contrôleur sont détaillées ci-dessous.




    // Page d'accueil : liste des conversations
    @GetMapping("/")
    fun accueil(model: Model, authentication: Authentication): String {
//Recupere l'utilisateur de la session
        val utilisateur = this.getUtilisateurConnecte(authentication)
// Ajout de l'utilisateur à la vue
        model.addAttribute("utilisateur", utilisateur)

        // Retire la conversation courante
        model.addAttribute("conversation", null)

        model.addAttribute("modeles", modeleIADAO.findAll())
        model.addAttribute("messages", emptyList<Message>())
        model.addAttribute(
            "conversations",
            conversationDAO.findAllByUtilisateurOrderByDateModificationDesc(utilisateur = utilisateur)
        )

        return "utilisateur/chat"
    }

    // Création d'une nouvelle conversation
    @PostMapping("/conversations")
    fun creerConversation(
        @RequestParam prompt: String,
        @RequestParam idModele: Long,
        authentication: Authentication
    ): String {

        // Recupere l'utilisateur
        val utilisateur = this.getUtilisateurConnecte(authentication)

        // Récupération du modèle sélectionné
        val modele = modeleIADAO.findById(idModele)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Modèle IA introuvable"
                )
            }

        // Création de la conversation
        val conversation = Conversation(
            utilisateur = utilisateur,
            titre = prompt.take(50),
            dateCreation = LocalDateTime.now(),
            modeleIA = modele,
            dateModification = LocalDateTime.now(),

        )

        val conversationEnregistree =
            conversationDAO.save(conversation)

        // Enregistrement du premier message
        val messageUtilisateur = Message(
            contenu = prompt,
            dateEnvoi = LocalDateTime.now(),
            type = TypeMessage.UTILISATEUR,
            conversation = conversationEnregistree
        )

        messageDAO.save(messageUtilisateur)

        try {
            // Sélection automatique du service
            val iaService = iaServiceFactory.getService(modele)

            // Appel du fournisseur correspondant
            val reponse = iaService.envoyerPrompt(prompt, modele)


            val messageAssistant = Message(
                contenu = reponse,
                dateEnvoi = LocalDateTime.now(),
                type = TypeMessage.UTILISATEUR,
                conversation = conversationEnregistree
            )

            messageDAO.save(messageAssistant)

        } catch (e: Exception) {
            println("Erreur lors de l’appel au modèle ${modele.nom} : ${e.message}")
        }

        conversationEnregistree.dateModification =
            LocalDateTime.now()

        conversationDAO.save(conversationEnregistree)

        return "redirect:/conversations/${conversationEnregistree.id}"
    }

    // Affichage d'une conversation
    @GetMapping("/conversations/{id}")
    fun afficherConversation(
        @PathVariable id: Long,
        authentication: Authentication,
        model: Model
    ): String {
//Recupere l'utilisateur
        val utilisateur = this.getUtilisateurConnecte(authentication)
// Ajout de l'utilisateur à la vue
        model.addAttribute("utilisateur", utilisateur)


        val conversation = conversationDAO
            .findByIdAndUtilisateur(id,utilisateur)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Conversation introuvable")


        model.addAttribute(
            "conversations",
            conversationDAO.findAllByUtilisateurOrderByDateModificationDesc(utilisateur)
        )


        model.addAttribute("conversation", conversation)
        model.addAttribute(
            "messages",
            conversation.messages.sortedBy { it.dateEnvoi }
        )

        return "utilisateur/chat"
    }

    // Envoi d'un nouveau message dans une conversation existante
    @PostMapping("/conversations/{id}/messages")
    fun envoyerMessage(
        @PathVariable id: Long,
        @RequestParam prompt: String,
        authentication: Authentication
    ): String {

        val utilisateur = this.getUtilisateurConnecte(authentication)

        val conversation = conversationDAO.findByIdAndUtilisateur(id,utilisateur)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Conversation introuvable")


        // Enregistrement du message utilisateur
        val messageUtilisateur = Message(
            contenu = prompt,
            dateEnvoi = LocalDateTime.now(),
            type = TypeMessage.UTILISATEUR,
            conversation = conversation
        )

        messageDAO.save(messageUtilisateur)

        try {
            val modele = conversation.modeleIA!!
            // Sélection automatique du service
            val iaService = iaServiceFactory.getService(modele)

            // Appel du fournisseur correspondant
            val reponse = iaService.envoyerPrompt(prompt, modele)


            val messageAssistant = Message(
                contenu = prompt,
                dateEnvoi = LocalDateTime.now(),
                type = TypeMessage.UTILISATEUR,
                conversation = conversation
            )

            messageDAO.save(messageAssistant)

        } catch (e: Exception) {
            println("Erreur lors de l'appel à ${conversation.modeleIA!!.nom} : ${e.message}")
        }

        conversation.dateModification = LocalDateTime.now()
        conversationDAO.save(conversation)

        return "redirect:/conversations/$id"
    }

    fun getUtilisateurConnecte( authentication: Authentication): Utilisateur{
        val email = authentication.name
        val utilisateur = utilisateurDAO
            .findByEmailIgnoreCase(email)
            ?: throw UsernameNotFoundException("Utilisateur introuvable")
        return utilisateur
    }
}