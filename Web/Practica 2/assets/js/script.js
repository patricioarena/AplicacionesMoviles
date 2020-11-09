document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("mailto").addEventListener('click', mailto);
    document.getElementById("bag").addEventListener('click', openNav);
    document.getElementById("Sidebar-bag").addEventListener('click', closeNav);
    document.getElementById("confirVenta").addEventListener('click', Confirmar);

    styloBox();
    suma();

});

function mailto() {
    document.location.href = "mailto:mypetshop2020@gmail.com?subject="
        + encodeURIComponent("Asunto")
        + "&body=" + encodeURIComponent("Razon de contacto");
}

function styloBox() {
    $('.item').on('click', function () {
        var id = $(this).attr("id");
        $(`#${id}`).toggleClass("seleccionado");
        $(this).children(".box").toggleClass("imgopacity");
        var op = $(this).attr("isSelected");
        if (op == "true") {
            $(this).attr("isSelected", "false");
        } else {
            $(this).attr("isSelected", "true");
        }
    });
}

function suma() {
    var storage = localStorage.getItem("productosAcumulados");
    console.log(storage);
    var arrayAux = [];

    if (storage != null) {
        arrayAux = storage;
    }

    $(document).on('click', '.item', function () {
        var arrayProductos = [];
        $('.item').each(function () {
            var op = $(this).attr("isSelected");
            if (op == "true") {
                arrayProductos.push(this);
            }
        })

        $('#tabla').empty()

        let temp = $('#tabla').text().trim();
        if (temp == "") {
            document.getElementById("total").innerText = 0;
            document.getElementById("bagCounter").innerText = 0;
        }

        var arrayPrecioProductos = []
        $.each(arrayProductos, function (index, elem) {
            var titulo = $(elem).children('.tituloProducto').text().trim();
            var precioOriginal = $(elem).children('.precioProducto').text();
            var precioRegular = precioOriginal.replace(/[$.]/g, '');
            var precioSinEspacios = precioRegular.trim();
            var precioParseInt = parseInt(precioSinEspacios);
            arrayPrecioProductos.push(precioParseInt);

            let x = `
            <tr>
            <th>${titulo}</th>
                <th>${precioRegular}</th>
            </tr>`

            $("#tabla").append($(x));

            sum = 0;
            $.each(arrayPrecioProductos, function (index, value) {
                sum += value;
            });

            document.getElementById("bagCounter").innerText = arrayPrecioProductos.length;
            document.getElementById("total").innerText = sum;


        })
    })




};

function openNav() {
    document.getElementById("Sidebar-bag").style.width = "250px";
    document.getElementById("main").style.marginRight = "250px";
    document.getElementById("price").style.width = "250px";
}

function closeNav() {
    document.getElementById("Sidebar-bag").style.width = "0";
    document.getElementById("main").style.marginRight = "0";
    document.getElementById("price").style.width = "0";
}

function Confirmar() {
    let temp = $('#tabla').text().trim();
    if (temp == "") {
        alert('No se puede concretar la venta')
    } else {
        alert('Venta concretada')
    }
}