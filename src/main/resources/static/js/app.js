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

    /* Chat : affichage local du message envoyé (le back-end gèrera la réponse) */
    const form = document.getElementById("chatForm");
    const input = document.getElementById("chatInput");
    const box = document.getElementById("chatMessages");
    if (form && input && box) {
        form.addEventListener("submit", (e) => {
            const text = input.value.trim();
            if (!text) { e.preventDefault(); return; }
            e.preventDefault();                       // retirer cette ligne une fois le back branché
            const msg = document.createElement("div");
            msg.className = "msg user";
            const bubble = document.createElement("div");
            bubble.className = "bubble";
            bubble.textContent = text;
            msg.appendChild(bubble);
            box.appendChild(msg);
            box.scrollTop = box.scrollHeight;
            input.value = "";
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
