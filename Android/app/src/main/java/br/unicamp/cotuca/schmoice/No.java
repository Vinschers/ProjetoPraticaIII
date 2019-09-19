package br.unicamp.cotuca.schmoice;

public class No {
    private Fase fase;
    private No esquerda, direita;
    public No() {

    }
    public No(Fase fase, No esq, No dir) {
        this.fase = fase;
        esquerda = esq;
        direita = dir;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }
    public Fase getFase() {
        return fase;
    }
    public void setEsquerda(No esq) {
        esquerda = esq;
    }
    public No getEsquerda() {
        return esquerda;
    }
    public void setDireita(No dir) {
        direita = dir;
    }
    public No getDireita(){
        return direita;
    }
}
