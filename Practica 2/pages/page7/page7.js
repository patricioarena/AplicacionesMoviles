document.addEventListener("DOMContentLoaded", function () {

    // setInterval(function () {
    //     validarObligatorio();
    //     // console.log(1);

    // }, 1000);

    document.getElementById("nom1").value = null;
    document.getElementById("ape1").value = null;
    document.getElementById("day").value = null;
    document.getElementById("month").value = null;
    document.getElementById("year").value = null;
    document.getElementById("sex1").value = null;
    document.getElementById("email1").value = null;

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

});

class Persona {
    constructor(nombre, apellido, email, fechaDeNac, sexo, valoracion, comentario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaDeNac = fechaDeNac;
        this.sexo = sexo;
        this.valoracion = valoracion;
        this.comentario = comentario;
    }
}

var nombre;
var apellido;
var email;
var fechaDeNac;
var sexo;
var valoracion;
var comentario;

var nombreValido = false;
var apellidoValido = false;
var emailValido = false;
var fechaValida = false;
var sexoValido = false;
var valoracionValida = false;


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
        nombre = text;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}

function validarApellido() {
    let element = document.getElementById('ape1');
    let text = element.value;

    if (reg1.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
        apellido = text;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}

function validarSexo() {
    let element = document.getElementById('sex1');
    let text = element.value;

    if (reg1.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
        sexo = text;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}

function validarNacimiento() {
    let day1 = document.getElementById('day');
    let month1 = document.getElementById('month');
    let year1 = document.getElementById('year');

    let day = day1.value;
    let month = month1.value;
    let year = year1.value;

    if ((validarLogitud2(day)) && (validarLogitud2(month)) && (validarLogitud4(year))) {
        let date = day + "/" + month + "/" + year;
        day1.classList.remove("error");
        day1.classList.add("success");
        month1.classList.remove("error");
        month1.classList.add("success");
        year1.classList.remove("error");
        year1.classList.add("success");
        fechaDeNac = date;
    } else {
        day1.classList.remove("success");
        day1.classList.add("error");
        month1.classList.remove("success");
        month1.classList.add("error");
        year1.classList.remove("success");
        year1.classList.add("error");
    }
}

function validarLogitud2(str) {
    let reg2 = /^[0-9]{2}$/;
    if (reg2.test(str)) {
        return true;
    } else {
        return false;
    }
}

function validarLogitud4(str) {
    let reg2 = /^[0-9]{4}$/;
    if (reg2.test(str)) {
        console.log(true)
        return true;
    } else {
        console.log(false)
        return false;
    }
}

function validarEmail() {
    let element = document.getElementById("email1");
    let value = element.value;

    if (reg2.test(value) && reg3.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        email = value;
    } else if (reg2.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        email = value;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}

function obtenerValoracion() {
    document.querySelectorAll('input[name="valoracion"]').forEach(element => {
        if (element.checked)
            valoracion = element.value;
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
    let a = document.getElementById("nom1").value;
    let b = document.getElementById("ape1").value;
    let c = document.getElementById("day").value;
    let d = document.getElementById("month").value;
    let e = document.getElementById("year").value;
    let f;

    document.querySelectorAll('input[name="valoracion"]').forEach(valoracion => {
        if (valoracion.checked)
            f = valoracion.value;
    });

    let g = document.getElementById("sex1").value;
    let i = document.getElementById("email1").value;

    console.log(a, b, c, d, e, f, g, i);
}


function enviar() {
    aPerson = new Persona();
    aPerson.nombre = nombre;
    aPerson.apellido = apellido;
    aPerson.email = email;
    aPerson.fechaDeNac = fechaDeNac;
    aPerson.sexo = sexo;
    aPerson.valoracion = valoracion;
    aPerson.comentario = comentario;

    console.log(aPerson);
}