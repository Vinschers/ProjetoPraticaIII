package br.unicamp.cotuca.schmoice;

import java.io.Serializable;

public class Escolha implements Serializable {
    private int idEscolha;
    private String nome;
    private int paraOndeIr;
    private double[] statusPlayer;
    private double[] statusAmizades;

    public int getIdEscolha() {return idEscolha;}
    public void setIdEscolha(int i) {idEscolha = i;}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double[] getStatusPlayer() {
        return statusPlayer;
    }

    public void setStatusPlayer(String status) {
        try {
            this.statusPlayer = (double[])ClienteWS.fromJson(status, double[].class);
        }
        catch (Exception er) {
            er.printStackTrace();
        }
    }

    public double[] getStatusAmizades() {
        return statusAmizades;
    }

    public void setStatusAmizades(String amizades) {
        try {
            this.statusAmizades = (double[])ClienteWS.fromJson(amizades, double[].class);
        }
        catch (Exception er) {
            er.printStackTrace();
        }
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
        statusPlayer = new double[7];
        statusAmizades = new double[Jogo.qtdAmigos];
    }
    public Escolha(String nome, int paraOndeIr, double[] s, double[] a) {
        this.nome = nome;
        this.paraOndeIr = paraOndeIr;
        this.statusPlayer = s;
        this.statusAmizades = a;
    }
}
