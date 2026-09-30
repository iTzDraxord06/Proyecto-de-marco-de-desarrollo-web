// Manejo del menú lateral responsive (Mobile)
document.addEventListener("DOMContentLoaded", function () {
    const botonMenuHamburguesa = document.querySelector(".boton-menu-hamburguesa");
    const menuLateral = document.getElementById("menuLateral");

    if (botonMenuHamburguesa && menuLateral) {
        function cerrarMenuMovil() {
            menuLateral.classList.remove("abierto");
            botonMenuHamburguesa.setAttribute("aria-expanded", "false");
            botonMenuHamburguesa.setAttribute("aria-label", "Abrir menú");
        }

        botonMenuHamburguesa.addEventListener("click", function () {
            const menuAbierto = menuLateral.classList.toggle("abierto");
            this.setAttribute("aria-expanded", menuAbierto);
            this.setAttribute("aria-label", menuAbierto ? "Cerrar menú" : "Abrir menú");
        });

        // Cierra el menú al presionar la tecla Escape
        document.addEventListener("keydown", function (e) {
            if (e.key === "Escape" && menuLateral.classList.contains("abierto")) {
                cerrarMenuMovil();
            }
        });
    }

    console.log("Sistema de Clínica: estilos y scripts cargados correctamente.");
});