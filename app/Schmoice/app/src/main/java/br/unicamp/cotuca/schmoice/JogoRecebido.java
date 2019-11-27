package br.unicamp.cotuca.schmoice;

import java.util.ArrayList;

public class JogoRecebido {
    public JogoRecebido() {}
    public JogoRecebido(Jogo jogo, int slot, String ip)
    {
        try
        {
            setIp(ip);
            setId(jogo.getId());
            setSlot(slot);
            setCaminho(ClienteWS.toJson(jogo.getArvore().getCaminho()));
            setEscolhasImportantes(ClienteWS.toJson(jogo.getEscolhasImportantes()));
            setCarisma(jogo.getPlayer().getCarisma());
            setFelicidade(jogo.getPlayer().getFelicidade());
            setFinancas(jogo.getPlayer().getFinancas());
            setForca(jogo.getPlayer().getForca());
            setInteligencia(jogo.getPlayer().getInteligencia());
            setSanidade(jogo.getPlayer().getSanidade());
            setTranquilidade(jogo.getPlayer().getTranquilidade());
        } catch (Exception ex) {}
    }

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

    public ArrayList<Integer> getEscolhasImportantes() {
        return escolhasImportantes;
    }

    public void setEscolhasImportantes(String escolhasImportantes) throws Exception {
        this.escolhasImportantes = (ArrayList<Integer>)ClienteWS.fromJson(escolhasImportantes, ArrayList.class);
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

    int id;
    int slot;
    String ip;
    ArrayList<Integer> caminho;
    ArrayList<Integer> escolhasImportantes;
    double tranquilidade, felicidade, financas, forca, inteligencia, sanidade, carisma;
}
