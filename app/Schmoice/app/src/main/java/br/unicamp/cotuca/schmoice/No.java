package br.unicamp.cotuca.schmoice;

public class No {
    private Fase fase;
    private No esquerda, direita;
    public No() {
        setFase(null);
        setEsquerda(null);
        setDireita(null);
    }
    public No(No no) {
        setFase(no.getFase());
        setEsquerda(no.getEsquerda());
        setDireita(no.getDireita());
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

    private boolean igual(No no1, No no2) {
        if ((no1.getFase() == null) != (no2.getFase() == null))
            return false;
        if ((no1.getEsquerda() == null) != (no2.getEsquerda() == null))
            return false;
        if ((no1.getDireita() == null) != (no2.getDireita() == null))
            return false;
        if (no1 == no2)
            return true;
        return no1.fase.equals(no2.fase) && igual(no1.getDireita(), no2.getDireita()) && igual(no1.getEsquerda(), no2.getEsquerda());
    }

    public boolean equals(Object outro) {
        if (outro == this)
            return true;
        if (outro == null)
            return false;
        if (outro.getClass() != getClass())
            return false;
        No no = (No)outro;
        return igual(this, no);
    }

    public int hashCode() {
        int ret = 3;
        ret = ret * 13 + fase.hashCode();
        return ret;
    }

    private String toStringAux(No no) {
        return "(" + toStringAux(no.getEsquerda()) + ")" + no.getFase() + "(" + toStringAux(no.getDireita()) + ")";
    }

    public String toString() {
        return "{" + toStringAux(this) + "}";
    }
}
