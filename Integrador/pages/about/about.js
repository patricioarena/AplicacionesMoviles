document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("backPage").addEventListener('click', back);
    initMap();
    // mostrarObjeto(navigator);
});


function back() {
    window.history.back();
}

function initMap(){
    // var coordenadas = {lat: -34.9228288 ,lng: -57.9562555};
    // var map = new google.maps.Map(document.getElementById('map'),{
    //     zoom: 10,
    //     center: coordenadas
    // });
    // var marker = new google.maps.Marker({
    //     position: coordenadas,
    //     map: map
    // });
    navigator.geolocation.getCurrentPosition(function_ok, function_error);
}

// function mostrarObjeto(objeto){
//     for(var propiedad in objeto){
//         document.write(propiedad+': '+objeto[propiedad] + '<br />');
//     }
// }

function function_ok(respuesta){
    //mostrarObjeto(respuesta.coords);
    var divMapa = document.getElementById('map');
    var latitud = respuesta.coords.latitude;
    var longitud = respuesta.coords.longitude;
    //divMapa.innerHTML = latitud+','+longitud;
    // divMapa.innerHTML = "Tenemos autorizacion para ver su ubicacion";

    var gLatLon = new google.maps.LatLng(latitud,longitud);
    var objConfig = {
        zoom : 17,
        center : gLatLon
    }
    var gMapa = new google.maps.Map(divMapa, objConfig);
    var objConfigMarker = {
        position : gLatLon,
        map : gMapa,
        title : "Usted esta Aquí"
    }
    var gMarker = new google.maps.Marker(objConfigMarker);

    var gCoder = new google.maps.Geocoder();
    var objInformacion = {
        address : 'Calle 14, B1900 La Plata, Provincia de Buenos Aires'
    }
    gCoder.geocode(objInformacion, function_coder);

    function function_coder(datos){
        var coordenadas = datos[0].geometry.location; //obj LatLong
        var config = {
            map: gMapa,
            position: coordenadas,
            title: 'Catedral de La Plata'
        }
        var gMarkerDV = new google.maps.Marker(config);
        //gMarkerDV.setIcon('catedral-la-plata.png');

    }
}

function function_error(){
    var divMapa = document.getElementById('map');
    divMapa.innerHTML = "Hubo un problema solicitando los datos";
}