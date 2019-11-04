const express = require('express');
const app = express();
const porta = 3000; //porta padrão
const sql = require('mssql');
const conexaoStr = "Server=regulus.cotuca.unicamp.br;Database=PR118178;User Id=PR118178;Password=MillerScherer1;";

//conexao com BD
sql.connect(conexaoStr)
   .then(conexao => global.conexao = conexao)
   .catch(erro => console.log(erro));

//acrescentando informacoes de cabecalho para suportar o CORS
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  res.header("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS, PATCH, DELETE");
  next();
});
//definindo as rotas


function Fase(id, niveis, titulo, descricao, nivelAtual, terminada, status, parteAtual) {
  this.id = id;
  this.niveis = niveis;
  this.titulo = titulo;
  this.descricao = descricao;
  this.nivelAtual = nivelAtual;
  this.terminada = terminada;
  this.status = status;
  this.parteAtual = parteAtual;
}

function Nivel(escolhas, descricao, background, terminado, parentFase, escolhaFeita) {
  this.escolhas = escolhas;
  this.descricao = descricao;
  this.background = background;
  this.terminado = terminado;
  this.parentFase = parentFase;
  this.escolhaFeita = escolhaFeita;
}

function Escolha(nome, paraOndeIr, status, amizades) {
  this.nome = nome;
  this.paraOndeIr = paraOndeIr;
  this.status = status;
  this.amizades = amizades;
}

var f = new Fase(0, [[new Nivel([new Escolha("avançar", 1, [], [])], "Nível padrão", "oi", false, null, null), new Nivel([new Escolha("avançar", 2, [], [])], "Minigame 1", "oi", false, null, null), new Nivel([new Escolha("avançar", -1, [], [])], "Minigame 1", "oi", false, null, null)]], "Teste", "Entrega parcial do projeto", null, false, 0.5, 0)
var fases = [f]


const rota = express.Router();
rota.get('/get', (req, res) => {
  res.json(fases);
});
app.use('/', rota);

//inicia servidor
app.listen(porta);
console.log('API Funcionando!');

function execSQL(sql, resposta) {
	global.conexao.request()
				  .query(sql)
				  .then(resultado => resposta.json(resultado.recordset))
				  .catch(erro => resposta.json(erro));
}