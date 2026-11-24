package org.lgv.chatbot.service

import org.lgv.chatbot.dto.gemini.GeminiChatRequest
import org.lgv.chatbot.dto.gemini.GeminiChatResponse
import org.lgv.chatbot.dto.gemini.GeminiContent
import org.lgv.chatbot.dto.gemini.GeminiPart
import org.lgv.chatbot.model.entity.ModeleIA

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class GeminiService(
    private val restClientBuilder: RestClient.Builder,

    @Value("\${gemini.api.key}")
    private val apiKey: String

) : IAService {

    override fun getFournisseur(): String = "Gemini"

    override fun envoyerPrompt(prompt: String, modele: ModeleIA): String {

        // Étape 1 : Création de la partie et du contenu pour l'utilisateur
        val part = GeminiPart(text = prompt)
        val content = GeminiContent(
            role = "user",
            parts = listOf(part)
        )

        // Étape 2 : Préparation de la requête pour Gemini
        val request = GeminiChatRequest(
            contents = listOf(content)
        )

        // Étape 3 : Envoi de la requête HTTP POST à l'API (la clé API passe en paramètre d'URL pour Gemini)
        val client = restClientBuilder.build()

        // Note : model.urlAPI doit contenir l'URL de base, et on ajoute la clé API en paramètre `?key=...`
        val finalUri = "${modele.urlAPI}?key=$apiKey"

        val response = client.post()
            .uri(finalUri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(GeminiChatResponse::class.java)

        // Étape 4 : Récupération du texte de la réponse
        val candidat = response?.candidates?.firstOrNull()
        val reponseTexte = candidat?.content?.parts?.firstOrNull()?.text

        return reponseTexte ?: "Aucune réponse reçue de Gemini."
    }
}