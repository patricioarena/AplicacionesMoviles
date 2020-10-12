document.addEventListener("DOMContentLoaded", function () {

  // getAllProducts();
  // getPokemones(5);


});


function getAllProducts() {
  $.get("http://www.json-generator.com/api/json/get/cgkBPoSHZu?indent=2", function (data) {
    $.each(data, function (key, value) {
      let item = `<div class="item" isSelected="false" id="${value.id}">
      <div class="box"><img src="${value.picture}" alt=""></div>
        <div class="..name-box">
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

class Pokemon {
  constructor(name) {
    this.nombre = name;
  }
}

function getPokemones() {
  $.get(`https://pokeapi.co/api/v2/pokemon/?limit=10&offset=0`, function (data) {
    $.each(data.results, function (key, value) {
      let aPokemon = new Pokemon(value.name);
      setData(aPokemon);
      console.log(aPokemon);
    });
    // console.log(data.results);
  });
}

function getTypes(data) {
  var arr = [];
  $.each(data['types'], function (key, value) {
    arr.push(value['type'].name)
  });
  return arr;
}

function getAbilities(data) {
  var arr = [];
  $.each(data['abilities'], function (key, value) {
    var ability = { name: '', is_hidden: '' };
    ability.name = value['ability'].name;
    ability.is_hidden = value.is_hidden;
    arr.push(ability);
  });
  return arr;
}

function getStats(data) {
  var arr = [];
  $.each(data['stats'], function (key, value) {
    var stat = { name: '', base_stat: '' };
    stat.base_stat = value.base_stat;
    stat.name = value['stat'].name
    arr.push(stat);
  });
  return arr;
}

// https://www.amiiboapi.com/api/amiibo/?character=zelda
function getAmiibo(pokemon) {
  var arr = [];
  $.get(`https://www.amiiboapi.com/api/amiibo/?name=${pokemon.nombre}`, function (data) {
    var amiibo = { amiiboSeries: '', character: '', image: '' };
    amiibo.amiiboSeries = data.amiibo[0].amiiboSeries;
    amiibo.character = data.amiibo[0].character;
    amiibo.image = data.amiibo[0].image;
    arr.push(amiibo);
    // console.log(data.amiibo)
  });
  return arr;
}

function setData(pokemon) {
  $.get(`https://pokeapi.co/api/v2/pokemon/${pokemon.nombre}`, function (data) {
    pokemon.id = data.id;
    pokemon.height = data.height;
    pokemon.weight = data.weight;
    pokemon.types = getTypes(data);
    pokemon.abilities = getAbilities(data);
    pokemon.stats = getStats(data);
    pokemon.picture = data.sprites.other['official-artwork'].front_default;
    pokemon.amiibo = getAmiibo(pokemon);
    // console.log(data);
  });
}

// Nota: altura y peso agregar de atras para adelante . y interpretarlos como mtr y kg
// https://pokeapi.api-docs.io/v2.0/pokemon/75poNABkA3Nf5Px9M

// species para pagina dos
// https://pokeapi.co/api/v2/pokemon-species/4/
//  egg_groups.name
//  flavor_text_entries.flavor_text
//  habitat.name
//  is_baby
//  is_legendary
//  is_mythical
// https://pokeapi.co/api/v2/evolution-chain/2/