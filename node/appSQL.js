const express = require('express');
const app = express();
const porta = 3000; //porta padrão
const sql = require('mssql');
const conexaoStr = "Server=regulus.cotuca.unicamp.br;Database=PR118179;User Id=PR118179;Password=MillerScherer1;";
var bodyParser = require('body-parser');

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
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded
//definindo as rotas


const rota = express.Router();
rota.get('/get', (req, res) => {
  	global.conexao.request().query(`select * from Escolha select * from Nivel select * from EscolhaNivel select * from Fase select * from NivelFase`).then(result => {
		var matriz = result.recordsets;
		var escolhas = matriz[0];
		var niveis = matriz[1];
		var escolhaNivel = matriz[2];
		var fases = matriz[3];
		var nivelFase = matriz[4];

		for (var i = 0; i < fases.length; i++)
			fases[i].niveis = [[]];

		for (var i = 0; i < niveis.length; i++)
			niveis[i].escolhas = [];

		escolhaNivel.forEach(en => {
			niveis.find(n => n.idNivel == en.idNivel).escolhas.push(escolhas.find(e => e.idEscolha == en.idEscolha));
		});

		nivelFase.forEach(nf => {
			var f = fases.find(f => f.idFase == nf.idFase);
			var n = niveis.find(n => n.idNivel == nf.idNivel);
			if (n.rota >= f.niveis.length)
				f.niveis.push(new Array(n.parte));
			f.niveis[n.rota].push(n);
		});

		for (var i = 0; i < fases.length; i++)
			fases[i].niveis.forEach(ns => {
				ns.sort((n1, n2) => {return n1.parte - n2.parte});
			});

		res.json(fases)
  	}).catch(erro => {
		console.log(erro)
  	});
});

rota.post('/addFase', (req, res) => {
	var str = `insert into Fase values(${req.body.idFase}, '${req.body.titulo}', '${req.body.descricao}')`;
	execSQL(str, res);
})
rota.post('/addNivel', (req, res) => {
	var n = req.body;
	var str = `adicionarNivel_sp '${n.descricao}', '${n.background}', ${n.tipo}, ${n.diff}, ${n.rota}, ${n.fase}`;
	execSQL(str, res);
})

rota.get('/jogos/:ip', (req, res) => {
  	execSQL(`select * from Jogo where ip='${req.params.ip}' order by slot asc`, res);
})
rota.get('/personagensJogo/:id', (req, res) => {
  	execSQL(`select * from JogoPersonagem where idJogo=${req.params.id}`, res);
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
				  .catch(erro => {console.log(erro);});
}
function execSQLSemResposta(sql, resposta) {
	global.conexao.request()
				  .query(sql)
				  .then(resultado => resposta = resposta)
				  .catch(erro => resposta.json(erro));
}