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
nivelAtual int not null,
linhaAtual int not null
)


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