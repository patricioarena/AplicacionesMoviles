document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("nom1").addEventListener('input', validarNombre);
    document.getElementById("ape1").addEventListener('input', validarApellido);

    document.getElementById("day").addEventListener('input', validarNacimiento);
    document.getElementById("month").addEventListener('input', validarNacimiento);
    document.getElementById("year").addEventListener('input', validarNacimiento);

    document.getElementById("sex1").addEventListener('input', validarSexo);
    document.getElementById("email1").addEventListener('input', validarEmail);

});

function validarNombre() {

    let element = document.getElementById('nom1');
    let text = element.value;

    let reg = /^[A-Z]+$/i;

    if (reg.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}

function validarApellido() {

    let element = document.getElementById('ape1');
    let text = element.value;

    let reg = /^[A-Z]+$/i;

    if (reg.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}

function validarSexo() {

    let element = document.getElementById('sex1');
    let text = element.value;

    let reg = /^[A-Z]+$/i;

    if (reg.test(text)) {
        element.classList.remove("error");
        element.classList.add("success");
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
        let date = day + "/" + month + "/" + year
        console.log(date)
        day1.classList.remove("error");
        day1.classList.add("success");
        month1.classList.remove("error");
        month1.classList.add("success");
        year1.classList.remove("error");
        year1.classList.add("success");
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
    let email = element.value;

    let reg = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    let regOficial = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

    //Se muestra un texto a modo de ejemplo, luego va a ser un icono
    if (reg.test(email) && regOficial.test(email)) {
        element.classList.remove("error");
        element.classList.add("success");
    } else if (reg.test(email)) {
        element.classList.remove("error");
        element.classList.add("success");
    } else {
        element.classList.remove("success");
        element.classList.add("error");
    }
}
