package br.unicamp.cotuca.schmoice;

import java.io.Serializable;

public class Escolha implements Serializable {
    private int idEscolha;
    private String nome;
    private int paraOndeIr;
    private double[] statusPlayer;
    private int statusFase;
    private int importancia;
    private int posImportancia;
    private int paraOndeIrNaRota;

    public int getParaOndeIrNaRota() {
        return paraOndeIrNaRota;
    }
    public void setParaOndeIrNaRota(int paraOndeIrNaRota) {
        this.paraOndeIrNaRota = paraOndeIrNaRota;
    }

    public int getImportancia() {
        return importancia;
    }
    public void setImportancia(int importancia) {
        this.importancia = importancia;
    }

    public int getPosImportancia() {
        return posImportancia;
    }
    public void setPosImportancia(int posImportancia) {
        this.posImportancia = posImportancia;
    }

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
    public void setParaOndeIr(int paraOndeIr) {
        this.paraOndeIr = paraOndeIr;
    }
    public int getParaOndeIr() {
        return paraOndeIr;
    }

    public void setStatusFase(int sf) {statusFase = sf;}
    public int getStatusFase() {return statusFase;}

    public Escolha() {
        nome = "";
        paraOndeIr = 0;
        statusPlayer = new double[7];
    }
    public Escolha(String nome, int paraOndeIr, double[] s) {
        this.nome = nome;
        this.paraOndeIr = paraOndeIr;
        this.statusPlayer = s;
    }
}
