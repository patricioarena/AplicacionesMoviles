document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("mailto").addEventListener('click', mailto);
    document.getElementById("inputSearch").addEventListener('keydown', search);
    document.getElementById("search-Btn").addEventListener('click', search);


    hideMenu();
    collapseMenu();


});


function mailto() {
    document.location.href = "mailto:mypetshop2020@gmail.com?subject="
        + encodeURIComponent("Asunto")
        + "&body=" + encodeURIComponent("Razon de contacto");
}

function hideMenu() {
    var collapsed = $("#menuBox").attr("collapsed");
    if (collapsed === "true") {
        $("#menuBox").hide();
    }
}

function collapseMenu() {
    $("#menutoggle").click(function () {
        var collapsed = $("#menuBox").attr("collapsed");
        $('#title1, #search1').toggleClass('hidden');
        if (collapsed === "true") {
            $("#menuBox").slideDown();
            $("#menuBox").attr("collapsed", "false");
        } else {
            $("#menuBox").slideUp();
            $("#menuBox").attr("collapsed", "true");
        }
    });
}

function search() {
    var str = $("#inputSearch").val();
    console.log(str);
}


