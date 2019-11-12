create table Jogo (
id int identity(1,1) primary key,
slot int not null,
ip varchar(max) not null,
acabouDeComecar bit not null,

tranquilidade float not null,
felicidade float not null,
sanidade float not null,
financas float not null,
inteligencia float not null,
carisma float not null,
forca float not null,

faseAtual int not null,
parteAtual int not null,
rotaAtual int not null
)

select * from Jogo


create table JogoPersonagem(
id int identity(1,1) primary key,
idJogo int not null,
constraint fkIdJogo foreign key(idJogo) references Jogo(id),
idPersonagem int not null,
amizade float not null
)

alter proc CriarJogo_sp
@slot int,
@ip varchar(max),
@tranquilidade float,
@felicidade float,
@sanidade float,
@financas float,
@inteligencia float,
@carisma float,
@forca float
as
insert into Jogo values(@slot, @ip, 1, @tranquilidade, @felicidade, @sanidade, @financas, @inteligencia, @carisma, @forca, '', 0, 0)
declare @id int = Scope_Identity()
declare @cont int = 0
while @cont < 6
begin
insert into JogoPersonagem values(@id, @cont, 0.5)
set @cont += 1
end
select * from Jogo where id=@id

create proc DeletarJogo_sp
@id int
as
delete from JogoPersonagem where idJogo=@id
delete from Jogo where id=@id



alter proc adicionarNivel_sp
@desc ntext,
@back ntext,
@tipo int,
@diff int,
@rota int,
@fase int
as
declare @parteAnt int
set @parteAnt = 0

select @parteAnt = parte from Nivel as n
inner join NivelFase as nf on nf.idNivel = n.idNivel
where
nf.idFase = @fase and
n.rota = @rota
order by n.parte

set @parteAnt = @parteAnt + 1
insert into Nivel values(@desc, @back, @tipo, @diff, @rota, @parteAnt)
insert into NivelFase values(Scope_Identity(), @fase)


create table Escolha (
idEscolha int primary key identity(1, 1),
nome ntext,
paraOndeIr int,
statusPlayer ntext,
statusAmizades ntext
)

create table EscolhaNivel (
idEscolhaNivel int primary key identity(1, 1),
idNivel int,
idEscolha int,
constraint fkIdNivelEscolhaNivel foreign key(idNivel) references Nivel(idNivel),
constraint fkIdEscolhaEscolhaNivel foreign key(idEscolha) references Escolha(idEscolha)
)


create table Nivel(
idNivel int primary key identity(1, 1),
descricao ntext,
background ntext,
tipo int,
diff int,
rota int,
parte int
)

create table Fase(
idFase int primary key,
titulo ntext,
descricao ntext,
)

create table NivelFase(
idNivelFase int primary key identity(1, 1),
idNivel int,
idFase int,
constraint fkIdNivelNivelFase foreign key(idNivel) references Nivel(idNivel),
constraint fkIdFaseNivelFase foreign key(idFase) references Fase(idFase)
)

insert into Escolha values('avançar', 0, '[0, 0, 0, 0, 0, 0, 0]', '[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]')
insert into Nivel values ('Nivel padrão', 'oi', 0, -1, 0, 0)
insert into Nivel values ('Minigame 1', 'oi', 1, 2, 0, 1)
insert into Nivel values ('Minigame 2', 'oi', 2, -1, 0, 2)
insert into Fase values(1, 'Teste', 'não sei')

insert into EscolhaNivel values (5, 2)
insert into NivelFase values(5, 1)
insert into NivelFase values(6, 1)
insert into NivelFase values(7, 1)