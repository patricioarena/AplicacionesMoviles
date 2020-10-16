document.addEventListener("DOMContentLoaded", function () {

    if(window.location.pathname == '/pages/more.html'){
      tomarImagenPorSeccion('itemx');
    }

});

function tomarImagenPorSeccion(article){
    let temp = document.querySelector(`#${article}` );
    html2canvas(temp).then(canvas => {
        var img = canvas.toDataURL();
        $('#download').click(function () {
            console.log(temp);
        })
    })

  }
