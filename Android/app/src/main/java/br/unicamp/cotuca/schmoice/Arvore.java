package br.unicamp.cotuca.schmoice;

public class Arvore {
    private No raiz;
    private No faseAtual;
    private int[] caminho = new int[3];
    public Arvore() {
        raiz = new No();
    }
    public Arvore(Arvore arvore) {
        this.raiz = arvore.raiz;
        this.faseAtual = arvore.faseAtual;
        this.caminho = arvore.caminho;
    }
    public boolean equals(Arvore outra) {
        if (outra == this)
            return true;
        if (outra == null)
            return false;
        if (outra.getClass() != this.getClass())
            return false;
        Arvore a = (Arvore) outra;
        if (!a.raiz.equals(this.raiz))
            return false;
        if (!a.caminho.equals(this.caminho))
            return false;
        if (!a.faseAtual.equals(this.faseAtual))
            return false;
        return true;
    }
    public void adicionar(Fase fase) throws Exception{
        No atual = raiz;
        while (atual.getEsquerda() != null && atual.getDireita() != null) {
            if (fase.compareTo(atual.getFase()) > 0) {
                atual = atual.getDireita();
            } else if (fase.compareTo(atual.getFase()) < 0) {
                atual = atual.getEsquerda();
            } else {
                throw new Exception("Fase ja existe!");
            }
        }
    }
}
