package br.unicamp.cotuca.schmoice;

import android.graphics.Bitmap;

public class Personagem {
    private String nome;
    private double amizade;
    private Bitmap[] frames;

    public Personagem(String nome, double amizade, Bitmap[] frames) {
        this.nome = nome;
        this.amizade = amizade;
        this.frames = frames;
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

    public Bitmap[] getFrames() {
        return frames;
    }

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
    }
    public void addToAmizade(double val) {
        amizade += val;
    }
}
