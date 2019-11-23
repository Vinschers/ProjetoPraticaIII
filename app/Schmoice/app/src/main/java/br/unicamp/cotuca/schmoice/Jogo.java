package br.unicamp.cotuca.schmoice;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Jogo implements Serializable {
    public static int qtdAmigos = 6;
    private Arvore arvore;
    private Player player;
    private Personagem[] amigos;
    private boolean acabouDeComecar;
    int id;
    private ArrayList<Integer> escolhasImportantes;

    public Jogo() {
        arvore = new Arvore();
        arvore.setJogo(this);
        player = new Player();
        amigos = new Personagem[qtdAmigos];
        for (int i = 0; i < qtdAmigos; i++)
            amigos[i] = new Personagem();
        acabouDeComecar = true;
        escolhasImportantes = new ArrayList<Integer>();
    }
    public Jogo(Jogo jogo) {
        arvore = jogo.getArvore();
        player = jogo.getPlayer();
        escolhasImportantes = jogo.getEscolhasImportantes();
    }

    public Arvore getArvore() {
        return this.arvore;
    }
    public Player getPlayer() {
        return this.player;
    }
    public Personagem[] getPersonagens() {return amigos;}
    public boolean getAcabouDeComecar() {
        return acabouDeComecar;
    }
    public void setAcabouDeComecar(boolean v) {
        acabouDeComecar = v;
    }
    public int getId() {return  id; }
    public void setId(int id) {this.id = id; }
    public ArrayList<Integer> getEscolhasImportantes() {
        return escolhasImportantes;
    }
    public void setEscolhasImportantes(ArrayList<Integer> escolhasImportantes) {
        this.escolhasImportantes = escolhasImportantes;
    }
}
