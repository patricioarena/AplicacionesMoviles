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
    $('.item').on('click', function() {
        var array = [];
        $('.item').each(function() {
            var op = $(this).attr("isSelected");
            if(op == "true"){
                array.push(this);
                console.log(array);
            }
        })
        var array2 = []
        $.each(array, function(index, elem) {
            array2.push(elem.value($`{span}`));
            console.log(array2);
        })
        var obtenerValor = $('.precioProducto').attr("span");
        // $('.precioProducto').each(function(){
        //     sessionStorage.setItem("variableAcumuladora", variableAcumuladora + Number(obtenerValor));
        // });

        // variableAcumuladora = sessionStorage.getItem("variableAcumuladora");
        // capturaAcumulable = Number(variableAcumuladora)
        // console.log(capturaAcumulable);
        // console.log(obtenerValor)
        })
    };
    