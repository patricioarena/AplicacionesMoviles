document.addEventListener("DOMContentLoaded", function () {

    setInterval(function () {
        validarObligatorio();
    }, 1000);

    document.getElementById("email1").value = null;
    document.getElementById("email2").value = null;
    document.getElementById("comentario").value = null;

    document.getElementById("email1").addEventListener('input', validarEmail);
    document.getElementById("email2").addEventListener('input', validarEmail);
    document.getElementById("comentario").addEventListener('input', procesarComentario)

    document.getElementById("sendEmail").addEventListener('click', sendEmail)
    document.getElementById("cancelar").addEventListener('click', cancelar)

});

class Mensaje {
    constructor() { }

    emailEmisor;
    emailReceptor;
    comentario;
    tarjeta;

}

var emailEmisorValido = false;
var emailReceptorValido = false;

var comentario;

//Letras con acentos y ñ
const reg1 = /^[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+$/;

//Email con caracteres latinos
const reg2 = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const reg3 = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;


function test() {
    document.addEventListener('keydown', (event) => {
        var key = window.event ? event.keyCode : event.which;
        if (key === 8 || key === 46) {
            return true;
        } else if ( key < 48 || key > 57 ) {
            event.preventDefault();
        } else {
            return true;
        }

    });
}



function validarEmail() {
    let element = document.getElementById("email1");
    let element2 = document.getElementById("email2");
    let value = element.value;

    if (reg2.test(value) && reg3.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        element2.classList.remove("error");
        element2.classList.add("success");
        emailEmisorValido = true;
        emailReceptorValido = true;
    } else if (reg2.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        element2.classList.remove("error");
        element2.classList.add("success");
        emailEmisorValido = true;
        emailReceptorValido = true;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
        element2.classList.remove("success");
        element2.classList.add("error");
        emailEmisorValido = true;
        emailReceptorValido = true;
    }
}


function procesarComentario() {
    let limite = 350
    let textarea = document.getElementById("comentario");
    textarea.maxLength = limite;

    let cant = textarea.value.length;
    restan = limite - cant;
    document.getElementById("restante").innerHTML = restan;

    comentario = textarea.value;
}

function validarObligatorio() {
    let element = document.getElementById("sendEmail");
    if ((emailEmisorValido && emailReceptorValido)) {
        element.classList.remove("disable");
        element.classList.add("enable");
    } else {
        element.classList.remove("enable");
        element.classList.add("disable");
    }
}

function sendEmail() {
    aMensaje = new Mensaje();
    aMensaje.emailEmisor = document.getElementById("email1").value;
    aMensaje.emailReceptor = document.getElementById("email2").value;
    aMensaje.comentario = comentario;
    aMensaje.tarjeta = document.getElementById("item2").value;
    mailto2();
}

function mailto2() {
    var email2 = document.getElementById("email2").value;
    document.location.href = "mailto:"+`${email2}`+"?subject="
        + encodeURIComponent("Conoce mas Sobre Esta App")
        + "&body=" + encodeURIComponent(document.getElementById('item2'));
}

function cancelar() {
    let response = confirm("¿Desea volver a la página anterior?");
    if (response) {
        window.history.back();
    }
}
