$(document).ready(function () {
    // if (window.location.pathname == '/pages/more/more.html' || window.location.pathname == '/pages/share/share.html') {
    //     setTimeout(() => {
    //         tomarImagenPorSeccion();
    //     }, 300);

    //     setTimeout(() => {
    //         console.log('yolooo!');
    //     }, 3000);

    // }
});



function tomarImagenPorSeccion() {
    let temp = document.querySelector(`#itemx`);
    let img = temp.querySelector('#imgPokemon');
    var canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');
    var fondo = new Image();
    fondo.src = img.src;
    context.drawImage(fondo, 0, 0,500,400);

    html2canvas(temp).then((oCanvas) => {

        var img = oCanvas.toDataURL("image/png");
        $('#download').click(function () {
            console.log(temp);
            var link = document.createElement("a");
            document.body.appendChild(link);
            link.setAttribute("href", img);
            link.setAttribute("download", "test.png");
            link.click();
        })
    })

}
