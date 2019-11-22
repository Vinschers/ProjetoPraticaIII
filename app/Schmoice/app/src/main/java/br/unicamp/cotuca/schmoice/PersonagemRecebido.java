package br.unicamp.cotuca.schmoice;

public class PersonagemRecebido {
    int id;
    int idJogo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public int getIdPersonagem() {
        return idPersonagem;
    }

    public void setIdPersonagem(int idPersonagem) {
        this.idPersonagem = idPersonagem;
    }

    public double getAmizade() {
        return amizade;
    }

    public void setAmizade(double amizade) {
        this.amizade = amizade;
    }

    int idPersonagem;
    double amizade;
    public PersonagemRecebido() {}
}
