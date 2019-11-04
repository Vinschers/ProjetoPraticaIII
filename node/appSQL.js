const express = require('express');
const app = express();
const porta = 3000; //porta padrão
const sql = require('mssql');
const conexaoStr = "Server=regulus.cotuca.unicamp.br;Database=PR118178;User Id=PR118178;Password=MillerScherer1;";

//conexao com BD
sql.connect(conexaoStr)
   .then(conexao => global.conexao = conexao)
   .catch(erro => console.log(erro));

// configurando o body parser para pegar POSTS mais tarde
app.use(bodyParser.urlencoded({limit: '500gb', extended: true}));
app.use(bodyParser.json({limit: '500gb', extended: true}));
//acrescentando informacoes de cabecalho para suportar o CORS
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  res.header("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS, PATCH, DELETE");
  next();
});
//definindo as rotas


function Fase(i, n, t, d, na, te, s, pa) {
  var id = i;
  var niveis = n;
  var titulo = t;
  var descricao = d;
  var nivelAtual = na;
  var terminada = te;
  var status = s;
  var parteAtual = pa;
}

function Nivel() {
  var escolhas;
  var descricao;
  var background;
  var terminado;
  var parentFase;
  var escolhaFeita;
}

var f = new Fase(0, [new Nivel(), new Nivel(), new Nivel()], "Teste", "Entrega parcial do projeto", 0, false, 0.5, 0)

var fases = [f]




const rota = express.Router();
rota.get('/', (req, res) => {
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