package br.unicamp.cotuca.schmoice;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Personagem implements Serializable {
    private String nome;
    private float width;
    private float rotation;
    private float x;
    private float y;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Personagem(String nome, double amizade) {
        this.nome = nome;
    }
    public Personagem() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getBmp() {
        return Uteis.getImageByName(this.nome);
    }
}
