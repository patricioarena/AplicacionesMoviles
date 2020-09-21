document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("mailto").addEventListener('click', mailto)
});


function mailto() {
    document.location.href = "mailto:mypetshop2020@gmail.com?subject="
        + encodeURIComponent("Asunto")
        + "&body=" + encodeURIComponent("Razon de contacto");
}

