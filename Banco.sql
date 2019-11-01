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

caminho varchar(4) not null,
nivelAtual int not null
)
create table JogoPersonagem(
id int identity(1,1) primary key,
idJogo int not null,
constraint fkIdJogo foreign key(idJogo) references Jogo(id),
idPersonagem int not null,
amizade float not null
)
