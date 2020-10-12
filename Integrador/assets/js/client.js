document.addEventListener("DOMContentLoaded", function () {

  // getAllProducts();
  getPokemones();


});

class Pokemon {
  constructor(name) {
    this.nombre = name;
  }
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

function printPokemon(aPokemon) {
  let item = `<article class="item" id="${aPokemon.id}">
      <div class="box-header">
          <img class="pokebolita" src="../assets/img/clipart2495981.png" alt="">
          <span class="np tf">Nº ${aPokemon.id}</span>
      </div>
      <div class="box"><img src="${aPokemon.picture}" alt="">
      </div>
      <div>
          <div class="name-box">
              <span class="tf">${aPokemon.nombre}</span>
          </div>
      </div>

      <div class="type-box">
          <label class="${aPokemon.types[0]} type tf">${aPokemon.types[0]}</label>
          <label class="${aPokemon.types[1]} type tf">${aPokemon.types[1]}</label>
      </div>
    </article>`
  $("#main").append($(item));
}

// https://www.amiiboapi.com/api/amiibo/?character=zelda
function getAmiibo(name) {
  var arr = [];
  $.get(`https://www.amiiboapi.com/api/amiibo/?name=${name}`, function (data) {
    var amiibo = { amiiboSeries: '', character: '', image: '' };
    amiibo.amiiboSeries = data.amiibo[0].amiiboSeries;
    amiibo.character = data.amiibo[0].character;
    amiibo.image = data.amiibo[0].image;
    arr.push(amiibo);
    // console.log(data.amiibo)
  });
  return arr;
}

function getPokemones() {
  var arr = [];
  $.get(`https://pokeapi.co/api/v2/pokemon/?limit=10&offset=0`, function (data) {
    $.each(data.results, function (key, value) {
      $.get(`https://pokeapi.co/api/v2/pokemon/${value.name}`, function () {
      }).done(function (data) {
        let aPokemon = new Pokemon(value.name);
        aPokemon.id = data.id;
        aPokemon.height = data.height;
        aPokemon.weight = data.weight;
        aPokemon.types = getTypes(data);
        aPokemon.abilities = getAbilities(data);
        aPokemon.stats = getStats(data);
        aPokemon.picture = data.sprites.other['official-artwork'].front_default;
        // pokemon.amiibo = getAmiibo(pokemon);
        aPokemon.amiibo = [];
        // console.log(aPokemon);
        arr.push(aPokemon);
        localStorage.setItem('list', JSON.stringify(arr));
      });
    });
  });
  superPrint()
}

function superPrint() {
  let arr = localStorage.getItem('list');
  let arrOrdened = JSON.parse(arr).sort((a,b)=> a.id - b.id);
  $.each(arrOrdened, function(index){
    printPokemon(arrOrdened[index]);
  })
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