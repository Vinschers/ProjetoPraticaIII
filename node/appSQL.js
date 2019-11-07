const express = require('express');
const app = express();
const porta = 3000; //porta padrão
const sql = require('mssql');
const conexaoStr = "Server=regulus.cotuca.unicamp.br;Database=PR118179;User Id=PR118179;Password=MillerScherer1;";

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


function Fase(id, niveis, titulo, descricao) {
  this.id = id;
  this.niveis = niveis;
  this.titulo = titulo;
  this.descricao = descricao;
  this.nivelAtual = null;
  this.terminada = false;
  this.status = 0.5;
  this.parteAtual = 0;
}

function Nivel(escolhas, descricao, background, tipo = 0, diff = -1) {
  this.escolhas = escolhas;
  this.descricao = descricao;
  this.background = background;
  this.terminado = false;
  this.parentFase = null;
  this.escolhaFeita = null;
  this.tipo = tipo; // 0 -> normal; 1-> minigame 1; 2-> minigame 2.
  this.diff = diff;
}

function Escolha(nome, paraOndeIr, status, amizades) {
  this.nome = nome;
  this.paraOndeIr = paraOndeIr;
  this.status = status;
  this.amizades = amizades;
}

var f = new Fase(0, [[new Nivel([new Escolha("avançar", 0, [0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0])], "Nível padrão", "oi")], [new Nivel([new Escolha("avançar", 0, [0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0])], "Minigame 1", "oi", 1, 2)], [new Nivel([new Escolha("avançar", -1, [0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0])], "Minigame 2", "oi", 2, 3)]], "Teste", "Entrega parcial do projeto")
var fases = [f]


const rota = express.Router();
rota.get('/get', (req, res) => {
  res.json(fases);
});

rota.get('/jogos/:ip', (req, res) => {
  execSQL(`select * from Jogo where ip='${req.params.ip}' order by slot asc`, res);
})
rota.post('/criarJogo', (req, res) => {
  const slot = req.body.slot;
  const ip = req.body.ip;
  const tranquilidade = req.body.tranquilidade;
  const felicidade = req.body.felicidade;
  const sanidade = req.body.sanidade;
  const financas = req.body.financas;
  const inteligencia = req.body.inteligencia;
  const carisma = req.body.carisma;
  const forca = req.body.forca;

  execSQL(`CriarJogo_sp ${slot}, '${ip}', ${tranquilidade}, ${felicidade}, ${sanidade}, ${financas}, ${inteligencia}, ${carisma}, ${forca}`, res);
})
rota.patch('/atualizarJogo', (req, res) => {
  const id = req.body.id;
  const slot = req.body.slot;
  const acabouDeComecar = req.body.acabouDeComecar ? 1 : 0;
  const tranquilidade = req.body.tranquilidade;
  const felicidade = req.body.felicidade;
  const sanidade = req.body.sanidade;
  const financas = req.body.financas;
  const inteligencia = req.body.inteligencia;
  const carisma = req.body.carisma;
  const forca = req.body.forca;
  const caminho = req.body.caminho;
  const nivelAtual = req.body.nivelAtual;
  const linhaAtual = req.body.linhaAtual;

  const amizades = req.body.amizades;

  execSQLSemResposta(`update Jogo set slot=${slot}, acabouDeComecar=${acabouDeComecar}, tranquilidade=${tranquilidade}, felicidade=${felicidade}, sanidade=${sanidade}, financas=${financas}, inteligencia=${inteligencia}, carisma=${carisma}, forca=${forca}, caminho='${caminho}, nivelAtual='${nivelAtual}, linhaAtual='${linhaAtual}' where id=${id}`, res);
  for (var i = 0; i < 6; i++)
    execSQLSemResposta(`update JogoPersonagem set amizade=${amizades[i]} where idJogo=${id} and idPersonagem=${i}`, res);

  res.json("Tudo certo :D");
})
rota.delete('/deletarJogo', (req, res) => {
  const id = req.body.id;
  execSQL(`DeletarJogo_sp ${id}`, res);
})

app.use('/', rota);

//inicia servidor
app.listen(porta);
console.log('API Funcionando!');

function execSQL(sql, resposta) {
	global.conexao.request()
				  .query(sql)
				  .then(resultado => resposta.json(resultado.recordset))
				  .catch(erro => {console.log(sql); resposta.json(erro);});
}
function execSQLSemResposta(sql, resposta) {
	global.conexao.request()
				  .query(sql)
				  .then(resultado => resposta = resposta)
				  .catch(erro => resposta.json(erro));
}