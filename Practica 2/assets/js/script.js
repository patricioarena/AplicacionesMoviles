document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("mailto").addEventListener('click', mailto);
    styloBox();
    suma();
});


function mailto() {
    document.location.href = "mailto:mypetshop2020@gmail.com?subject="
        + encodeURIComponent("Asunto")
        + "&body=" + encodeURIComponent("Razon de contacto");
}

function styloBox() {
    $('.item').on('click', function() {
        var id = $(this).attr("id");
        $(`#${id}`).toggleClass("seleccionado");
        $(this).children(".box").toggleClass("imgopacity");
        var op = $(this).attr("isSelected");
        if(op == "true"){
            $(this).attr("isSelected","false");
        }else{
            $(this).attr("isSelected","true");
        }
    });
}

function suma(){
    var storage = localStorage.getItem("productosAcumulados");
    console.log(storage);
    var arrayAux = [];
    if (storage != null){
        arrayAux = storage;
    }
    $('.item').on('click', function() {
        var arrayProductos = [];
        $('.item').each(function() {
            var op = $(this).attr("isSelected");
            if(op == "true"){
                arrayProductos.push(this);
            }
        })
        var arrayPrecioProductos = []
        $.each(arrayProductos, function(index, elem) {
            var precioOriginal = $(elem).children('.precioProducto').text();
            var precioRegular = precioOriginal.replace(/[$.]/g,'');
            var precioSinEspacios = precioRegular.trim();
            var precioParseInt = parseInt(precioSinEspacios);
            arrayPrecioProductos.push(precioParseInt);
        })

        sum = 0;
        $.each(arrayPrecioProductos, function (index, value) {
            sum += value;
        });

        console.log(sum);

        localStorage.setItem("productosAcumulados", arrayProductos);

        //var obtenerValor = $('.precioProducto').attr("span");
        // $('.precioProducto').each(function(){
        //     sessionStorage.setItem("variableAcumuladora", variableAcumuladora + Number(obtenerValor));
        // });

        // variableAcumuladora = sessionStorage.getItem("variableAcumuladora");
        // capturaAcumulable = Number(variableAcumuladora)
        // console.log(capturaAcumulable);
        // console.log(obtenerValor)
        })
    };

