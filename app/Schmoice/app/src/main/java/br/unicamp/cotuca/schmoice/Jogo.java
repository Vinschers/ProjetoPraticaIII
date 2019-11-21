package br.unicamp.cotuca.schmoice;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Jogo implements Serializable {
    private Arvore arvore;
    private Player player;
    private Personagem[] amigos;
    private boolean acabouDeComecar;
    int id;

    public Jogo() {
        arvore = new Arvore();
        arvore.setJogo(this);
        player = new Player();
        acabouDeComecar = true;
    }
    public Jogo(Jogo jogo) {
        arvore = jogo.getArvore();
        player = jogo.getPlayer();
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
}
