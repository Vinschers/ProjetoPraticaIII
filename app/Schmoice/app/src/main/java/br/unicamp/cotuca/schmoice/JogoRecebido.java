package br.unicamp.cotuca.schmoice;

import java.util.ArrayList;

public class JogoRecebido {
    public JogoRecebido() {}
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getFaseAtual() {
        return faseAtual;
    }

    public void setFaseAtual(int faseAtual) {
        this.faseAtual = faseAtual;
    }

    public int getParteAtual() {
        return parteAtual;
    }

    public void setParteAtual(int parteAtual) {
        this.parteAtual = parteAtual;
    }

    public int getRotaAtual() {
        return rotaAtual;
    }

    public void setRotaAtual(int rotaAtual) {
        this.rotaAtual = rotaAtual;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ArrayList<Integer> getCaminho() {
        return this.caminho;
    }

    public void setCaminho(String caminho) throws Exception {
        this.caminho = (ArrayList<Integer>)ClienteWS.fromJson(caminho, ArrayList.class);
    }

    public boolean isAcabouDeComecar() {
        return acabouDeComecar;
    }

    public void setAcabouDeComecar(boolean acabouDeComecar) {
        this.acabouDeComecar = acabouDeComecar;
    }

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

    public double getFinancas() {
        return financas;
    }

    public void setFinancas(double financas) {
        this.financas = financas;
    }

    public double getForca() {
        return forca;
    }

    public void setForca(double forca) {
        this.forca = forca;
    }

    public double getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(double inteligencia) {
        this.inteligencia = inteligencia;
    }

    public double getSanidade() {
        return sanidade;
    }

    public void setSanidade(double sanidade) {
        this.sanidade = sanidade;
    }

    public double getCarisma() {
        return carisma;
    }

    public void setCarisma(double carisma) {
        this.carisma = carisma;
    }

    int slot;
    int faseAtual;
    int parteAtual;
    int rotaAtual;
    String ip;
    ArrayList<Integer> caminho;
    boolean acabouDeComecar;
    double tranquilidade, felicidade, financas, forca, inteligencia, sanidade, carisma;
}
