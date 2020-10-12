document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("backPage").addEventListener('click', back);
    initMap();
    // mostrarObjeto(navigator);
});


function back() {
    window.history.back();
}

function initMap(){
    navigator.geolocation.getCurrentPosition(function_ok, function_error);
}

// function mostrarObjeto(objeto){
//     for(var propiedad in objeto){
//         document.write(propiedad+': '+objeto[propiedad] + '<br />');
//     }
// }

function function_ok(respuesta){
    var titlesProvider = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    var latitud = respuesta.coords.latitude;
    var longitud = respuesta.coords.longitude;

    var gLatLon = L.map('map').setView([latitud, longitud], 10);

    L.tileLayer(titlesProvider, {
        maxZoom: 18,
    }).addTo(gLatLon)


    // var gLatLon = new google.maps.LatLng(latitud,longitud);
    // var objConfig = {
    //     zoom : 17,
    //     center : gLatLon
    // }

    var gMarker = L.marker([latitud, longitud]);
    gMarker.addTo(gLatLon);

    var iconMarker = L.icon({
        iconUrl : '../../assets/img/tienda.png',
        iconSize : [60, 60],
        iconAnchor : [30, 60]
    })

    var positionMarket = [-34.9251137, -57.9540854];
    var gMarker2 = L.marker(positionMarket, {icon: iconMarker});
    gMarker2.addTo(gLatLon);

    document.getElementById('select-location').addEventListener('change', function(e){
        let value = e.target.value;
        if (value == "myPosition"){
            let myCoords = [latitud, longitud];
            console.log(myCoords);
            gLatLon.flyTo(myCoords,18);
        }else{
            console.log(positionMarket);
            gLatLon.flyTo(positionMarket,18);
        }
    })


    // var gMapa = new google.maps.Map(divMapa, objConfig);
    // var objConfigMarker = {
    //     position : gLatLon,
    //     map : gMapa,
    //     title : "Usted esta Aquí"
    // }
    // var gMarker = new google.maps.Marker(objConfigMarker);


    // var gCoder = new google.maps.Geocoder();
    // var objInformacion = {
    //     address : 'Calle 14, B1900 La Plata, Provincia de Buenos Aires'
    // }
    // gCoder.geocode(objInformacion, function_coder);

    // function function_coder(datos){
    //     var coordenadas = datos[0].geometry.location; //obj LatLong
    //     var config = {
    //         map: gMapa,
    //         position: coordenadas,
    //         title: 'Catedral de La Plata'
    //     }
    //     var gMarkerDV = new google.maps.Marker(config);
    //     //gMarkerDV.setIcon('catedral-la-plata.png');

    // }
}

function function_error(){
    var divMapa = document.getElementById('map');
    divMapa.innerHTML = "Hubo un problema solicitando los datos";
}