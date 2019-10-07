package br.unicamp.cotuca.schmoice;

import java.io.Serializable;

public class Player implements Serializable {
    private double tranquilidade;
    private double felicidade;
    private double sanidade;
    private double financas;
    private double inteligencia;
    private double carisma;
    private double forca;

    public Player(double tranquilidade, double felicidade, double sanidade, double financas, double inteligencia, double carisma, double forca) {
        this.tranquilidade = tranquilidade;
        this.felicidade = felicidade;
        this.sanidade = sanidade;
        this.financas = financas;
        this.inteligencia = inteligencia;
        this.carisma = carisma;
        this.forca = forca;
    }
    public Player(){ }

    public double getTranquilidade() {
        return tranquilidade;
    }

    public void setTranquilidade(double tranquilidade) {
        this.tranquilidade = tranquilidade;
    }

    public double getFelicidade() {
        return felicidade;
    }

    public void setFelicidade(double felicidade) {
        this.felicidade = felicidade;
    }

    public double getSanidade() {
        return sanidade;
    }

    public void setSanidade(double sanidade) {
        this.sanidade = sanidade;
    }

    public double getFinancas() {
        return financas;
    }

    public void setFinancas(double financas) {
        this.financas = financas;
    }

    public double getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(double inteligencia) {
        this.inteligencia = inteligencia;
    }

    public double getCarisma() {
        return carisma;
    }

    public void setCarisma(double carisma) {
        this.carisma = carisma;
    }

    public double getForca() {
        return forca;
    }

    public void setForca(double forca) {
        this.forca = forca;
    }
    public void addToTranquilidade(double val) {
        tranquilidade += val;
    }
    public void addToFelicidade(double val) {
        felicidade += val;
    }
    public void addToSanidade(double val) {
        sanidade += val;
    }
    public void addToFinancas(double val) {
        financas += val;
    }
    public void addToInteligencia(double val) {
        inteligencia += val;
    }
    public void addToCarisma(double val) {
        carisma += val;
    }
    public void addToForca(double val) {
        forca += val;
    }
}
