document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("backPage").addEventListener('click', back);

});


function back() {
    window.history.back();
}