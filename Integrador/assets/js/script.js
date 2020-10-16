document.addEventListener("DOMContentLoaded", function () {

  document.getElementById("mailto").addEventListener('click', mailto);

  if (document.getElementById('backPage') != null) {
    document.getElementById("backPage").addEventListener('click', back);
  }

  if (document.getElementById('share') != null) {
    document.getElementById("share").addEventListener('click', redirecToShare);
  }

  if (document.getElementById('history') != null) {
    document.getElementById("history").addEventListener('click', printHistory);
  }

  hideMenu();
  collapseMenu();

});


function mailto() {
  document.location.href = "mailto:pokepedia@gmail.com?subject="
    + encodeURIComponent("Asunto")
    + "&body=" + encodeURIComponent("Razon de contacto");
}

function hideMenu() {
  var collapsed = $("#menuBox").attr("collapsed");
  if (collapsed === "true") {
    $("#menuBox").css("display", "none");
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

function back() {
  window.history.back();
}

function redirecToShare() {
  window.location.href = '../pages/share.html';
}

function redirecToIndex() {
  window.location.href = '/index.html';
}

function printHistory() {
  $("#main").css("display", "none");
  $('#searchResult').css("display", "none");
  $("#pageNavigation").css("display", "none");

  $('#mainHistory').html('');

  let arr = sessionStorage.getItem('history');
  let arr2 = JSON.parse(arr);
  $.each(arr2, function (index) {
    printPokemon(arr2[index], 'mainHistory');
  });
}

function redirecToShare() {
  window.location.href = '../pages/share/share.html';
}
