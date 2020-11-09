document.addEventListener("DOMContentLoaded", function () {

    setInterval(function () {
        validarObligatorio();
    }, 1000);

    document.getElementById("nom1").value = null;
    document.getElementById("ape1").value = null;
    document.getElementById("day").value = null;
    document.getElementById("month").value = null;
    document.getElementById("year").value = null;
    document.getElementById("sex1").value = null;
    document.getElementById("email1").value = null;

    document.querySelectorAll('input[name="valoracion"]').forEach(element => {
        element.checked = false;
    });

    // document.getElementById("day").addEventListener('focus', test);
    // document.getElementById("month").addEventListener('focus', test);
    // document.getElementById("year").addEventListener('focus', test);

    document.getElementById("comentario").value = null;

    document.getElementById("nom1").addEventListener('input', validarNombre);
    document.getElementById("ape1").addEventListener('input', validarApellido);

    document.getElementById("day").addEventListener('input', validarNacimiento);
    document.getElementById("month").addEventListener('input', validarNacimiento);
    document.getElementById("year").addEventListener('input', validarNacimiento);

    document.getElementById("valoraciones").addEventListener('click', obtenerValoracion);
    document.getElementById("sex1").addEventListener('input', validarSexo);
    document.getElementById("email1").addEventListener('input', validarEmail);
    document.getElementById("comentario").addEventListener('input', procesarComentario)

    document.getElementById("enviar").addEventListener('click', enviar)
    document.getElementById("cancelar").addEventListener('click', cancelar)
    document.getElementById("reset").addEventListener('click', reset)

});

class Persona {
    constructor() { }

    nombre;
    apellido;
    email;
    fechaDeNac;
    sexo;
    valoracion;
    comentario;

}

var nombreValido = false;
var apellidoValido = false;
var emailValido = false;
var fechaValida = false;
var sexoValido = false;
var valoracionValida = false;

var valoracion;
var comentario;

//Letras con acentos y ñ
const reg1 = /^[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+$/;

//Email con caracteres latinos
const reg2 = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
const reg3 = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;


function validarNombre() {
    let element = document.getElementById('nom1');
    let text = element.value;

    if (reg1.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
        nombreValido = true;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
        nombreValido = false;
    }
}

function validarApellido() {
    let element = document.getElementById('ape1');
    let text = element.value;

    if (reg1.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
        apellidoValido = true;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
        apellidoValido = false
    }
}

function validarSexo() {
    let element = document.getElementById('sex1');
    let text = element.value;

    if (reg1.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
        sexoValido = true;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
        sexoValido = false;
    }
}

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

function validarNacimiento() {

    let day1 = document.getElementById('day');
    let month1 = document.getElementById('month');
    let year1 = document.getElementById('year');

    day1.maxLength = 2;
    month1.maxLength = 2;
    year1.maxLength = 4;

    let day = day1.value;
    let month = month1.value;
    let year = year1.value;

    if ((validarDia(day)) && (validarMes(month)) && (validarYear(year))) {
        day1.classList.remove("error");
        day1.classList.add("success");
        month1.classList.remove("error");
        month1.classList.add("success");
        year1.classList.remove("error");
        year1.classList.add("success");
        fechaValida = true;
    } else {
        day1.classList.remove("success");
        day1.classList.add("error");
        month1.classList.remove("success");
        month1.classList.add("error");
        year1.classList.remove("success");
        year1.classList.add("error");
        fechaValida = false;
    }
}

function validarDia(str) {
    let reg2 = /^[0-9]{2}$/;
    if (reg2.test(str)) {
        let aux = Number.parseInt(str);
        if ((aux >= 1) && (aux <= 31)) {
            return true;
        }
    } else {
        return false;
    }
}

function validarMes(str) {
    let reg2 = /^[0-9]{2}$/;
    if (reg2.test(str)) {
        let aux = Number.parseInt(str);
        if ((aux >= 1) && (aux <= 12)) {
            return true;
        }
    } else {
        return false;
    }
}

function validarYear(str) {
    let reg2 = /^[0-9]{4}$/;
    if (reg2.test(str)) {
        let aux = Number.parseInt(str);
        if (aux >= 1900) {
            return true;
        }
    } else {
        return false;
    }
}

function validarEmail() {
    let element = document.getElementById("email1");
    let value = element.value;

    if (reg2.test(value) && reg3.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        emailValido = true;
    } else if (reg2.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        emailValido = true;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
        emailValido = false;
    }
}

function obtenerValoracion() {
    document.querySelectorAll('input[name="valoracion"]').forEach(element => {
        if (element.checked) {
            valoracion = element.value;
            valoracionValida = true;
        }
    });
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
    let element = document.getElementById("enviar");
    if ((nombreValido) && (apellidoValido) && (emailValido) && (fechaValida) && (sexoValido) && (valoracionValida)) {
        element.classList.remove("disable");
        element.classList.add("enable");
    } else {
        element.classList.remove("enable");
        element.classList.add("disable");
    }
}

function enviar() {
    let day = document.getElementById('day').value;
    let month = document.getElementById('month').value;
    let year = document.getElementById('year').value;

    aPerson = new Persona();
    aPerson.nombre = document.getElementById("nom1").value;
    aPerson.apellido = document.getElementById("ape1").value;
    aPerson.email = document.getElementById("email1").value;
    aPerson.fechaDeNac = day + "/" + month + "/" + year;
    aPerson.sexo = document.getElementById("sex1").value;
    aPerson.valoracion = valoracion;
    aPerson.comentario = comentario;

    // console.log(aPerson);

    alert(
        "Nombre: " + aPerson.nombre + "\n" +
        "Apellido: " + aPerson.apellido + "\n" +
        "Fecha de Nacimiento: " + aPerson.fechaDeNac + "\n" +
        "Sexo: " + aPerson.sexo + "\n" +
        "Valoración: " + aPerson.valoracion + "\n" +
        "Email: " + aPerson.email + "\n" +
        "Comentario: " + aPerson.comentario + "\n"
    );
}

function cancelar() {
    let response = confirm("¿Desea volver a la página anterior?");
    if (response) {
        window.history.back();
    }
}

function reset() {
    window.location.reload();
}