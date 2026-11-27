document.addEventListener("DOMContentLoaded", () => {

    /* Connexion : afficher / masquer le mot de passe */
    const pwd = document.getElementById("password");
    const toggle = document.getElementById("togglePwd");
    if (pwd && toggle) {
        toggle.addEventListener("click", () => {
            const show = pwd.type === "password";
            pwd.type = show ? "text" : "password";
            toggle.querySelector(".ico").className = "ico " + (show ? "ico-eye-slash" : "ico-eye");
        });
    }

    /* Chat : défilement, zone de saisie auto-extensible, envoi avec Entrée */
    const chatForm = document.getElementById("formChat");
    const prompt = document.getElementById("chatPrompt");
    const messages = document.getElementById("chatMessages");
    const btnSend = document.getElementById("btnSend");

    if (messages) messages.scrollTop = messages.scrollHeight;

    if (chatForm && prompt) {
        const autoGrow = () => {
            prompt.style.height = "auto";
            prompt.style.height = Math.min(prompt.scrollHeight, 200) + "px";
        };
        prompt.addEventListener("input", autoGrow);

        // Entrée = envoyer, Maj+Entrée = retour à la ligne
        prompt.addEventListener("keydown", (e) => {
            if (e.key === "Enter" && !e.shiftKey && !e.isComposing) {
                e.preventDefault();
                chatForm.requestSubmit();
            }
        });

        // Évite le double envoi pendant que l'IA répond
        chatForm.addEventListener("submit", () => {
            if (btnSend) {
                btnSend.disabled = true;
                btnSend.classList.add("loading");
            }
        });
    }

    /* Liste des modèles : recherche + filtre de statut */
    const search = document.getElementById("searchModele");
    const filter = document.getElementById("filterStatut");
    const rows = document.querySelectorAll("#tableModeles tbody tr");
    const count = document.getElementById("countModeles");
    const applyFilters = () => {
        const q = (search?.value || "").toLowerCase();
        const st = filter?.value || "";
        let n = 0;
        rows.forEach((tr) => {
            const ok = tr.textContent.toLowerCase().includes(q) && (!st || tr.dataset.statut === st);
            tr.style.display = ok ? "" : "none";
            if (ok) n++;
        });
        if (count) count.textContent = n + (n > 1 ? " modèles" : " modèle");
    };
    search?.addEventListener("input", applyFilters);
    filter?.addEventListener("change", applyFilters);

    /* Ajout : glisser-déposer du logo */
    const dz = document.getElementById("dropzone");
    const file = document.getElementById("logoFile");
    if (dz && file) {
        ["dragenter", "dragover"].forEach((ev) =>
            dz.addEventListener(ev, (e) => { e.preventDefault(); dz.classList.add("drag"); }));
        ["dragleave", "drop"].forEach((ev) =>
            dz.addEventListener(ev, (e) => { e.preventDefault(); dz.classList.remove("drag"); }));
        dz.addEventListener("drop", (e) => { file.files = e.dataTransfer.files; showName(); });
        file.addEventListener("change", showName);
        function showName() {
            const f = file.files[0];
            if (f) dz.querySelector("small").textContent = f.name;
        }
    }
});
