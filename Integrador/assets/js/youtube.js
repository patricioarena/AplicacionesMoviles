document.addEventListener("DOMContentLoaded", function () {
    searchYoutube();
});

function searchYoutube() {
    var idVideo;
    var useApi = false;
    var useMockApi = true;

    if (window.outerWidth >= 1200 && useApi == true) {
        // $("#test").css("display", "inline-flex");
        // $("#main").css("width","70%")
        setTimeout(() => {
            var arr = JSON.parse(localStorage.getItem('list'));
            var item = arr[Math.floor(Math.random() * arr.length)];
            var url = `https://www.googleapis.com/youtube/v3/search?part=snippet&q=${item.nombre} vs.&key=AIzaSyAuX-YL2jZif_f6ZbBPBd-Uq6ROePU-QbM&maxResults=10`
            $.get(url, function () {
            }).done(function (data) {
                let idRandom = Math.floor(Math.random() * arr.length);
                console.log(data.items[idRandom].id.videoId);
                idVideo = data.items[idRandom].id.videoId;
                var iframe = `<iframe width="350" height="200" src="https://www.youtube.com/embed/${idVideo}?modestbranding=1&color=red&iv_load_policy=3"
                frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`;
                $('#test').html(iframe);
            });
        }, 300);
    }

    if (window.outerWidth >= 1200 && useMockApi == true){
        // $("#asaide1").css("display", "inline-flex");
        // $("#test").css("display", "inline-flex");
        // $("#main").css("width","70%")
        var iframe = `<iframe width="210" height="200" src="https://www.youtube.com/embed/svvbbNnHDR4?modestbranding=1&color=red&iv_load_policy=3"
                frameborder="1" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`
        $('#test').html(iframe);
    }
    // else{
    //     // $("#asaide1").css("display", "none");
    //     // $("#test").css("display", "none");
    //     // $("#main").css("width","100%")
    // }

    if (window.location.pathname == '/pages/about/about.html') {
        $("#mainMap").css("width","100%");
    }

}

