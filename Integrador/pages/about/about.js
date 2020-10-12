document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("backPage").addEventListener('click', back);
    initMap();
    // mostrarObjeto(navigator);
});


function back() {
    window.history.back();
}

async function initMap(){
    navigator.geolocation.getCurrentPosition(function_ok, function_error);
}

// function mostrarObjeto(objeto){
//     for(var propiedad in objeto){
//         document.write(propiedad+': '+objeto[propiedad] + '<br />');
//     }
// }

function function_ok(respuesta){
    var titlesProvider = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
    var latitud = respuesta.coords.latitude;
    var longitud = respuesta.coords.longitude;

    var gLatLon = L.map('map').setView([latitud, longitud], 9);

    L.tileLayer(titlesProvider, {
        attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' + 
        '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id : 'mapbox/streets-v11',
        maxZoom: 18,
        tileSize: 512,
        zoomOffset: -1     
    }).addTo(gLatLon)


    // var gLatLon = new google.maps.LatLng(latitud,longitud);
    // var objConfig = {
    //     zoom : 17,
    //     center : gLatLon
    // }

    var pointMarker = L.icon({
        iconUrl : '../../assets/img/mypoint.png',
        iconSize : [30, 50],
        iconAnchor : [15, 02]
    })

    var gMarker = L.marker([latitud, longitud], {icon: pointMarker});
    gMarker.addTo(gLatLon);
    gMarker.bindPopup("<b>Tu Estas Aquí!</b>").openPopup();

    var iconMarker = L.icon({
        iconUrl : '../../assets/img/tienda.png',
        iconSize : [60, 60],
        iconAnchor : [30, 10]
    })

    var positionMarket = [-34.9227784, -57.9563658];
    var gMarker2 = L.marker(positionMarket, {icon: iconMarker});
    gMarker2.addTo(gLatLon);
    gMarker2.bindPopup("<b>La Tienda se encuentra Aquí!</b><br>Calle 51 e/14 y 15, B1900 La Plata, Provincia de Buenos Aires.").openPopup();

    
    L.Routing.control({
        waypoints: [
            L.latLng(latitud, longitud),
            L.latLng(-34.9227784, -57.9563658)
        ],
        language: 'es',
        createMarker: function (){
            return null;
        }
    }).addTo(gLatLon);


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