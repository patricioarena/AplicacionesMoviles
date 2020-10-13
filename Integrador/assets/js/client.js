document.addEventListener("DOMContentLoaded", function () {

  document.getElementById('search1').addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
      let temp = document.getElementById('inputSearch').value;
      getPokemon(temp);
    }
  })

  document.getElementById('search-Btn').addEventListener('click', function () {
    let temp = document.getElementById('inputSearch').value;
    getPokemon(temp);
  });

  if (document.getElementById('previousPage') != null) {
    document.getElementById('previousPage')
      .addEventListener('click', function () {
        previousPage()
      });
  }

  if (document.getElementById('nextPage') != null) {
    document.getElementById('nextPage')
      .addEventListener('click', function () {
        nextPage();
      });
  }

  if (document.getElementById('fire') != null) {
    document.getElementById('fire').addEventListener('click', function () {
      getPokemonType('fire');
    });
  }

  if (document.getElementById('water') != null) {
    document.getElementById('water').addEventListener('click', function () {
      getPokemonType('water');
    });
  }

  if (document.getElementById('bug') != null) {
    document.getElementById('bug').addEventListener('click', function () {
      getPokemonType('bug');
    });
  }

  if (document.getElementById('poison') != null) {
    document.getElementById('poison').addEventListener('click', function () {
      getPokemonType('poison');
    });
  }

  if (document.getElementById('dragon') != null) {
    document.getElementById('dragon').addEventListener('click', function () {
      getPokemonType('dragon');
    });
  }

  //limite de 10 y comienza en 0
  getPokemones(10, offset);
  printPokemonMore();

});

numPage = 1;
offset = 0;

class Pokemon {
  constructor(name) {
    this.nombre = name;
  }
}

function nextPage() {
  numPage = numPage + 1;
  offset = offset + 10;
  getPokemones(10, offset);
  $('#numPage').html(numPage);
}

function previousPage() {
  numPage = numPage - 1;
  offset = offset - 10;
  if (numPage === 0) {
    numPage = 1;
    offset = 0;
  }
  getPokemones(10, offset);
  $('#numPage').html(numPage);
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

function printPokemon(aPokemon, id) {
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
  $(`#${id}`).append($(item));
  $(`#${aPokemon.id}`).click(function () {
    var arr = [];
    arr.push(aPokemon);
    localStorage.setItem('aPokemon', JSON.stringify(arr));
    printPokemonMore();
    redirecToMore();
  });
}

function setPicture(data) {
  let op = data.sprites.other['official-artwork'].front_default;
  if (op === null) {
    console.log(op);
    return '../assets/img/404.png"';
  } return op;
}

function getPokemon(name) {

  localStorage.removeItem('aPokemon');
  $('#searchResult').html('');
  var arr = [];
  name = name.toLowerCase();

  $.get(`https://pokeapi.co/api/v2/pokemon/${name}`, function () {
  }).done(function (data) {
    let aPokemon = new Pokemon(data.name);
    aPokemon.id = data.id;
    aPokemon.height = data.height;
    aPokemon.weight = data.weight;
    aPokemon.types = getTypes(data);
    aPokemon.abilities = getAbilities(data);
    aPokemon.stats = getStats(data);
    aPokemon.picture = setPicture(data);
    aPokemon.amiibo = [];
    arr.push(aPokemon);
    localStorage.setItem('aPokemon', JSON.stringify(arr));
  }).done(function () {
    let ap = localStorage.getItem('aPokemon');
    printPokemon(JSON.parse(ap)[0], 'searchResult');
    $("#main").css("display", "none");
    $("#pageNavigation").css("display", "none");
  });

}

function getPokemones(limit, offset) {

  $('#main').html('');
  var arr = [];

  $.get(`https://pokeapi.co/api/v2/pokemon/?limit=${limit}&offset=${offset}`, function (data) {
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
        aPokemon.picture = setPicture(data);
        aPokemon.amiibo = [];
        arr.push(aPokemon);
        localStorage.setItem('list', JSON.stringify(arr));
      });
    });
  }).done(function () {
    setTimeout(() => {
      let arr = localStorage.getItem('list');
      let arrOrdened = JSON.parse(arr).sort((a, b) => a.id - b.id);
      $.each(arrOrdened, function (index) {
        printPokemon(arrOrdened[index], 'main');
      })
    }, 100);
  });

}

function getPokemonType(typePokemon) {
  $('#main').html('');
  var arr = [];

  var urlType = `https://pokeapi.co/api/v2/type/${typePokemon}`;
  $.get(urlType, function (data) {
    $.each(data.pokemon, function (key, value) {
      $.get(`https://pokeapi.co/api/v2/pokemon/${value.pokemon.name}`, function () {
      }).done(function (data) {
        let aPokemon = new Pokemon(data.name);
        aPokemon.id = data.id;
        aPokemon.height = data.height;
        aPokemon.weight = data.weight;
        aPokemon.types = getTypes(data);
        aPokemon.abilities = getAbilities(data);
        aPokemon.stats = getStats(data);
        aPokemon.picture = setPicture(data);
        aPokemon.amiibo = [];
        arr.push(aPokemon);
        localStorage.setItem('list', JSON.stringify(arr));
      });
    });
  }).done(function () {
    $("#pageNavigation").css("display", "none");
    setTimeout(() => {
      let arr = localStorage.getItem('list');
      let arrOrdened = JSON.parse(arr).sort((a, b) => a.id - b.id);
      $.each(arrOrdened, function (index) {
        printPokemon(arrOrdened[index], 'main');
      })
    }, 100);
  });
}

function printPokemonMore() {
  let ap = localStorage.getItem('aPokemon');
  let aPokemon = JSON.parse(ap)[0];

  $('#idPokemon').text(aPokemon.id);
  $('#namePokemon').text(aPokemon.nombre);
  $('#imgPokemon').attr('src', aPokemon.picture);
  $('#kg').text(aPokemon.weight/10);
  $('#metro').text(aPokemon.height/10);
  $('#type0').text(aPokemon.types[0]);
  $('#type1').text(aPokemon.types[1]);

  $('#type0').addClass(`${aPokemon.types[0]} type tf`);
  $('#type1').addClass(`${aPokemon.types[1]} type tf`);

  $('#hp').val(aPokemon.stats[0].base_stat);
  $('#def').val(aPokemon.stats[1].base_stat);
  $('#atk').val(aPokemon.stats[2].base_stat);
  $('#spatk').val(aPokemon.stats[3].base_stat);
  $('#spdef').val(aPokemon.stats[4].base_stat);
  $('#spd').val(aPokemon.stats[5].base_stat);
  $('#habilidadUnlock').text(aPokemon.abilities[0].name);
  $('#habilidadLock').text(hidenHb(aPokemon));

  $.get(`https://pokeapi.co/api/v2/pokemon-species/${aPokemon.id}`, function () {
  }).done(function (data) {
    var description = getDescription(data)
    $('#description').text(description)
  });
}

function hidenHb(data) {
  if (data.abilities.length == 1) {
    $('#hb-lock-box').css('display','none')
    return ''
  }else{
    $('#hb-lock-box').css('display','flex')
    return data.abilities[1].name;
  }
}

function getDescription(data) {
  let arr = data.flavor_text_entries;
  let string = '';
  for (let index = 0; index < arr.length; index++) {
    if (data.flavor_text_entries[index].language.name == 'es') {
        let temp = data.flavor_text_entries[index].flavor_text;
        string = string + "" + temp;
    }
  }
  return string;
}

function redirecToMore() {
  window.location.href = 'pages/more.html';
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