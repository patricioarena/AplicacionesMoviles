document.addEventListener("DOMContentLoaded", function () {

    setInterval(function () {
        validarObligatorio();
    }, 1000);

    document.getElementById("email1").value = null;
    document.getElementById("email2").value = null;
    document.getElementById("comentario").value = null;

    document.getElementById("email1").addEventListener('input', validarEmailEmisor);
    document.getElementById("email2").addEventListener('input', validarEmailRceptor);
    document.getElementById("comentario").addEventListener('input', procesarComentario);

    document.getElementById("sendEmail").addEventListener('click', sendEmail);
    document.getElementById("cancelar").addEventListener('click', cancelar);
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
        } else if (key < 48 || key > 57) {
            event.preventDefault();
        } else {
            return true;
        }

    });
}


function validarEmailEmisor() {
    let element = document.getElementById("email1");
    let check = document.getElementById("check");
    let value = element.value;

    if (reg2.test(value) && reg3.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        check.innerHTML = '<label id="checking"><img src=\'../../assets/img/check-correct.png\'> Correcto</label>';
        emailEmisorValido = true;
    } else if (reg2.test(value)) {
        element.classList.remove("error");
        element.classList.add("success");
        check.innerHTML = '<label id="checking"><img src=\'../../assets/img/check-correct.png\'> Correcto</label>';
        emailEmisorValido = true;
    } else {
        element.classList.remove("success");
        element.classList.add("error");
        check.innerHTML = '<label id="checking"><img src=\'../../assets/img/check-incorrect.png\'> Incorrecto</label>';
        emailEmisorValido = false;
    }
}

function validarEmailRceptor() {
    let element2 = document.getElementById("email2");
    let check = document.getElementById("check2");
    let value2 = element2.value;

    if (reg2.test(value2) && reg3.test(value2)) {
        element2.classList.remove("error");
        element2.classList.add("success");
        check.innerHTML = '<label id="checking"><img src=\'../../assets/img/check-correct.png\'> Correcto</label>';
        emailReceptorValido = true;
    } else if (reg2.test(value2)) {
        element2.classList.remove("error");
        element2.classList.add("success");
        check.innerHTML = '<label id="checking"><img src=\'../../assets/img/check-correct.png\'> Correcto</label>';
        emailReceptorValido = true;
    }else {
        element2.classList.remove("success");
        element2.classList.add("error");
        check.innerHTML = '<label id="checking"><img src=\'../../assets/img/check-incorrect.png\'> Incorrecto</label>';
        emailReceptorValido = false;
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

    let idPokemon = $('#idPokemon').text();
    let namePokemon = $('#namePokemon').text();
    //let imgPokemon = $('#imgPokemon').attr();
    let kg = $('#kg').text();
    let metro = $('#metro').text();
    let type0 = $('#type0').text();
    let type1 = $('#type1').text();

    let hp = $('#hp').val();
    let def = $('#def').val();
    let atkv = $('#atk').val();
    let spatk = $('#spatk').val();
    let spdef = $('#spdef').val();
    let spd = $('#spd').val();
    let habilidadUnlock = $('#habilidadUnlock').text();
    let habilidadLock = $('#habilidadLock').text();
    let description = $('#description').text().substring(500,-1);



    console.log(idPokemon);
    console.log(namePokemon);
    console.log(imgPokemon.src);
    console.log(kg);
    console.log(metro);
    console.log(type0);
    console.log(type1);
    console.log(hp);
    console.log(def);
    console.log(atkv);
    console.log(spatk);
    console.log(spdef);
    console.log(spd);
    console.log(habilidadUnlock);
    console.log(habilidadLock);
    console.log(description);

    var email2 = document.getElementById("email2").value;
    document.location.href = "mailto:"+`${email2}`+"?subject="
        + encodeURIComponent("Conoce mas Sobre Esta App")
        + "&body=" + encodeURIComponent(document.getElementById("comentario").value) + "%0AHola%0A%0AEste%20mail%20te%20lo%20mande%20con%20un%20mailto,%20el%20pokemon%20que%20elegi%20tiene%20Nº%20de%20POKEDEX:%20" + idPokemon +
        "%0A%0ASu%20nombre%20es:%20" + namePokemon + "%0A%0ASu%20Peso%20y%20altura%20son:%20" + kg + "%20Kg%20y%20" + metro + "%20M" + "%0A%0ASu%20Tipo%20es:%20" + type0 + "%20y%20" + type1 +
        "%0A%0ASu%20puntaje%20de%20hp%20es:%20" + hp + "%0ASu%20puntaje%20de%20defensa%20es:%20" + def + "%0ASu%20puntaje%20de%20atkv%20es:%20" + atkv + "%0ASu%20puntaje%20de%20atkv%20es:%20" + spatk +
        "%0ASu%20puntaje%20de%20spdef%20es:%20" + spdef + "%0ASu%20puntaje%20de%20spdef%20es:%20" + spd + "%0A%0ASu%20habilidad%20desbloqueada%20es:%20" + habilidadUnlock +
        "%0A%0ASu%20habilidad%20bloqueada%20es:%20" + habilidadLock + "%0A%0ADescripción%20del%20Pokemon:%20" + description
}


function cancelar() {
    window.history.back();
}
