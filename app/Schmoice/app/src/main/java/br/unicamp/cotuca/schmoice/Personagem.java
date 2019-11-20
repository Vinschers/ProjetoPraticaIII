package br.unicamp.cotuca.schmoice;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Personagem implements Serializable {
    private String nome;
    private double amizade;

    public Personagem(String nome, double amizade) {
        this.nome = nome;
        this.amizade = amizade;
    }
    public Personagem() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAmizade() {
        return amizade;
    }

    public void setAmizade(double amizade) {
        this.amizade = amizade;
    }

    public void addToAmizade(double val) {
        amizade += val;
    }

    public Bitmap getBmp() {
        return Uteis.getImageByName(this.nome);
    }
}
