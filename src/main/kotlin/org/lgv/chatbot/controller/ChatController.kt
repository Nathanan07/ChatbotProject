package org.lgv.chatbot.controller


import org.lgv.chatbot.model.dao.ConversationDAO
import org.lgv.chatbot.model.dao.MessageDAO
import org.lgv.chatbot.model.dao.ModeleIADAO
import org.lgv.chatbot.model.dao.UtilisateurDAO
import org.lgv.chatbot.model.entity.Conversation
import org.lgv.chatbot.model.entity.*
import org.lgv.chatbot.service.IAServiceFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
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




    // Page d'accueil = "Nouveau chat" (aucune conversation sélectionnée)
    // L'historique et l'utilisateur sont ajoutés à la vue par GlobalModelAdvice.
    @GetMapping("/")
    fun accueil(
        @RequestParam(required = false) modele: Long?,
        model: Model
    ): String {
        // Aucune conversation courante : on affiche l'écran "Comment puis-je vous aider ?"
        model.addAttribute("conversation", null)
        model.addAttribute("messages", emptyList<Message>())

        // Seuls les modèles actifs sont proposés
        model.addAttribute("modeles", modeleIADAO.findAll().filter { it.actif })

        // Modèle présélectionné (lien "Discuter" depuis la page Modèles IA)
        model.addAttribute("modeleSelectionne", modele)

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
                type = TypeMessage.IA,
                conversation = conversationEnregistree
            )

            messageDAO.save(messageAssistant)

        } catch (e: Exception) {
            println("Erreur lors de l’appel au modèle ${modele.nom} : ${e.message}")
            enregistrerErreurIA(conversationEnregistree)
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
        // L'utilisateur et l'historique sont ajoutés à la vue par GlobalModelAdvice
        val utilisateur = this.getUtilisateurConnecte(authentication)

        val conversation = conversationDAO
            .findByIdAndUtilisateur(id,utilisateur)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Conversation introuvable")


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
            val modele = conversation.modeleIA
            // Sélection automatique du service
            val iaService = iaServiceFactory.getService(modele)

            // Appel du fournisseur correspondant
            val reponse = iaService.envoyerPrompt(prompt, modele)


            val messageAssistant = Message(
                contenu = reponse,
                dateEnvoi = LocalDateTime.now(),
                type = TypeMessage.IA,
                conversation = conversation
            )

            messageDAO.save(messageAssistant)

        } catch (e: Exception) {
            println("Erreur lors de l'appel à ${conversation.modeleIA.nom} : ${e.message}")
            enregistrerErreurIA(conversation)
        }

        conversation.dateModification = LocalDateTime.now()
        conversationDAO.save(conversation)

        return "redirect:/conversations/$id"
    }

    // Si l'appel à l'IA échoue, on garde une trace visible dans la conversation
    private fun enregistrerErreurIA(conversation: Conversation) {
        messageDAO.save(
            Message(
                contenu = "Désolé, une erreur est survenue lors de la réponse du modèle. Veuillez réessayer.",
                dateEnvoi = LocalDateTime.now(),
                type = TypeMessage.IA,
                conversation = conversation
            )
        )
    }

    fun getUtilisateurConnecte( authentication: Authentication): Utilisateur{
        val email = authentication.name
        val utilisateur = utilisateurDAO
            .findByEmailIgnoreCase(email)
            ?: throw UsernameNotFoundException("Utilisateur introuvable")
        return utilisateur
    }
}