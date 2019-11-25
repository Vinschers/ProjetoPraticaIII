package br.unicamp.cotuca.schmoice;

import java.io.Serializable;
import java.util.ArrayList;

public class Arvore implements Serializable {
    private No raiz;
    private No faseAtual;
    private Jogo jogo;
    private ArrayList<Integer> caminho;

    public Arvore() {
        raiz = null;
        faseAtual = raiz;
        caminho = new ArrayList<Integer>();
    }
    public Arvore(Arvore arvore) {
        this.raiz = arvore.raiz;
        this.faseAtual = arvore.faseAtual;
        faseAtual = raiz;
        caminho = arvore.caminho;
    }
    public boolean equals(Object outra) {
        if (outra == this)
            return true;
        if (outra == null)
            return false;
        if (outra.getClass() != this.getClass())
            return false;
        Arvore a = (Arvore) outra;
        if (!a.raiz.equals(this.raiz))
            return false;
        if (!a.faseAtual.equals(this.faseAtual))
            return false;
        return true;
    }
    public int hashCode() {
        int ret = 3;
        ret = ret * 2 + raiz.hashCode();
        ret = ret * 3 + faseAtual.hashCode();
        return ret;
    }
    public void atualizarFaseAtual() {
        if (faseAtual == null) {
            faseAtual = raiz;
            faseAtual.getFase().setPlayerAntigo(jogo.getPlayer());
        }
        if (faseAtual.getFase().isTerminada()) {
            for(int i = 0; i < faseAtual.getProxs().size(); i++)
            {
                if (faseAtual.getProxs().get(i).getFase().getReqs().serve(jogo.getEscolhasImportantes()))
                {
                    faseAtual = faseAtual.getProxs().get(i);
                    caminho.add(i);
                    break;
                }
            }
        }
    }
    public Fase getFaseAtual() {
        if (faseAtual == null) {
            faseAtual = raiz;
            faseAtual.getFase().setPlayerAntigo(jogo.getPlayer());
        }
        return faseAtual.getFase();
    }

    public void adicionar(Fase fase) {
        fase.setArvore(this);
        No aux = raiz;
        for (int i = 0; i < fase.getCaminhoFase().size() - 1; i++) {
            if (aux.getProxs() == null)
                aux.setProxs(new ArrayList<No>());
            if (i >= aux.getProxs().size())
                for (int j = aux.getProxs().size() - 1; j < i; j++)
                    aux.getProxs().add(new No(null, null));
            if (aux.getProxs().get(i) == null)
                aux.getProxs().add(new No(null, null));
            aux = aux.getProxs().get(i);
        }
        try {
            int ind = fase.getCaminhoFase().get(fase.getCaminhoFase().size() - 1);
            if (aux.getProxs().get(ind) == null)
                aux.getProxs().add(new No(fase, null));
            else
                aux.getProxs().get(ind).setFase(fase);
        } catch (Exception e) {
            raiz = new No(fase, null);
        }
    }

    public ArrayList<Integer> getCaminho() {
        return caminho;
    }

    public void setCaminho(ArrayList<Integer> c) {
        caminho = c;
        faseAtual = raiz;
        for(int i : caminho) {
            faseAtual = faseAtual.getProxs().get(i);
        }
    }
    public void setJogo(Jogo j) {
        jogo = j;
    }
    public Jogo getJogo() {
        return jogo;
    }
}
