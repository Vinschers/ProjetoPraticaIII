package br.unicamp.cotuca.schmoice;

import java.io.Serializable;

public class Escolha implements Serializable {
    private final int nAmigos = 10;
    private String nome;
    private int paraOndeIr;
    private double[] status;
    private double[] amizades;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double[] getStatus() {
        return status;
    }

    public void setStatus(double[] status) {
        this.status = status;
    }

    public double[] getAmizades() {
        return amizades;
    }

    public void setAmizades(double[] amizades) {
        this.amizades = amizades;
    }
    public void setParaOndeIr(int paraOndeIr) {
        this.paraOndeIr = paraOndeIr;
    }
    public int getParaOndeIr() {
        return paraOndeIr;
    }
    public Escolha() {
        nome = "";
        paraOndeIr = 0;
        status = new double[7];
        amizades = new double[nAmigos];
    }
    public Escolha(String nome, int paraOndeIr, double[] s, double[] a) {
        this.nome = nome;
        this.paraOndeIr = paraOndeIr;
        this.status = s;
        this.amizades = a;
    }
}
