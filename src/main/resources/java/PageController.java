package ressources.java.chatbot   // <-- à adapter à ton package

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Routes front-end : relie toutes les pages de la maquette.
 * (Pas de logique métier ici : les POST redirigent simplement.)
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String accueil() {
        return "redirect:/login";
    }

    // ---------- Authentification ----------
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginSubmit() {
        return "redirect:/chat";
    }

    // ---------- Utilisateur ----------
    @GetMapping("/chat")
    public String chat() {
        return "utilisateur/chat";
    }

    @PostMapping("/chat/envoyer")
    public String chatEnvoyer() {
        return "redirect:/chat";
    }

    // ---------- Administration : modèles d'IA ----------
    @GetMapping("/admin/modeles")
    public String modeles() {
        return "admin/modeles";
    }

    @GetMapping("/admin/modeles/ajouter")
    public String modeleAjout() {
        return "admin/modele-ajout";
    }

    @GetMapping("/admin/modeles/{id}")
    public String modeleDetail() {
        return "admin/modele-detail";
    }

    @GetMapping("/admin/modeles/{id}/modifier")
    public String modeleEdition() {
        return "admin/modele-edition";
    }

    @PostMapping({"/admin/modeles", "/admin/modeles/{id}/modifier"})
    public String modeleEnregistrer() {
        return "redirect:/admin/modeles";
    }

    @GetMapping("/admin/modeles/{id}/supprimer")
    public String modeleSupprimer() {
        return "redirect:/admin/modeles";   // à remplacer par un POST + service plus tard
    }
}
