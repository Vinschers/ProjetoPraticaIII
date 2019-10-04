package br.unicamp.cotuca.schmoice;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Jogo implements Serializable {
    private Arvore arvore;
    private Player player;

    public Jogo() {
        arvore = new Arvore();
        player = new Player();
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
}
