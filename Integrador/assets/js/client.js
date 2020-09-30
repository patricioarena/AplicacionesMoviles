function getAllProducts() {
  $.get("http://www.json-generator.com/api/json/get/cgkBPoSHZu?indent=2", function (data) {
    $.each(data, function (key, value) {
      let item = `<div class="item" isSelected="false" id="${value.id}">
      <div class="box"><img src="${value.picture}" alt=""></div>
        <div class="precioProducto">
          <span>$</span>
          <span>${value.precio}</span>
        </div>
        <div class="envioProducto"><span>Envio Gratis</span> </div>
        <div class="tituloProducto">
          <h2>${value.title}</h2>
        </div>
      </div>`
      $("#main").append($(item));
    });
  });
}