document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("backPage").addEventListener('click', back);
    initMap();

});


function back() {
    window.history.back();
}

async function initMap(){
    navigator.geolocation.getCurrentPosition(function_ok, function_error);
}


function function_ok(respuesta){
    var titlesProvider = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';
    var latitud = respuesta.coords.latitude;
    var longitud = respuesta.coords.longitude;

    var map = L.map('map').setView([latitud, longitud], 5);

    L.tileLayer(titlesProvider, {
        attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' + 
        '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id : 'mapbox/streets-v11',
        maxZoom: 18,
        tileSize: 512,
        zoomOffset: -1     
    }).addTo(map)


    var pointMarker = L.icon({
        iconUrl : '../../assets/img/mypoint.png',
        iconSize : [30, 50],
        iconAnchor : [15, 02]
    })

    var gMarker = L.marker([latitud, longitud], {icon: pointMarker});
    gMarker.addTo(map);
    gMarker.bindPopup("<b>Tu Estas Aquí!</b>").openPopup();

    var iconMarker = L.icon({
        iconUrl : '../../assets/img/tienda.png',
        iconSize : [60, 60],
        iconAnchor : [30, 10]
    })

    var positionMarket = [-34.9227784, -57.9563658];
    var gMarker2 = L.marker(positionMarket, {icon: iconMarker});
    gMarker2.addTo(map);
    gMarker2.bindPopup("<b>Nuestra Oficina se encuentra Aquí!</b><br>Calle 51 e/14 y 15, B1900 La Plata, Provincia de Buenos Aires.").openPopup();

    
    L.Routing.control({
        waypoints: [
            L.latLng(latitud, longitud),
            L.latLng(-34.9227784, -57.9563658)
        ],
        language: 'es',
        createMarker: function (){
            return null;
        }
    }).addTo(map);

   
    // L.easyButton('fa-level-up',
    //   function (){
    //     $('.leaflet-routing-container').is(':visible') ? gLatLon.removeLayer(rlayer) : gLatLon.addLayer(gLatLon)
    //   }).addTo(gLatLon);

    // var rlayer = null;
    //     L.easyButton('fa-level-up',
    //         function() {
    //             if(rlayer) {
    //             map.removeLayer(rlayer);
    //             rlayer = null;
    //         } else {
    //             var routing = L.Routing.control(...);
    //             rlayer = L.layerGroup([routing]);
    //             map.addLayer(rlayer);      
    //         }
    //     }, 'Mostrar Ruta' ).addTo(map);
   

    document.getElementById('select-location').addEventListener('change', function(e){
        let value = e.target.value;
        if (value == "myPosition"){
            let myCoords = [latitud, longitud];
            console.log(myCoords);
            map.flyTo(myCoords,18);
        }else{
            console.log(positionMarket);
            map.flyTo(positionMarket,18);
        }
    })

}

function function_error(){
    var divMapa = document.getElementById('map');
    divMapa.innerHTML = "Hubo un problema solicitando los datos";
}