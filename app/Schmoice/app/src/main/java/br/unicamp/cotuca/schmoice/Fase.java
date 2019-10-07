package br.unicamp.cotuca.schmoice;

import java.util.ArrayList;

public class Fase implements Comparable<Fase>, Cloneable {
    private int id;
    private ArrayList<ArrayList<Nivel>> niveis;
    private String titulo;
    private String descricao;
    private Nivel nivelAtual;
    private boolean terminada;
    private double status;
    private int parteAtual;

    private Arvore arvore;
    public Fase() {
        nivelAtual = null;
        niveis = new ArrayList<ArrayList<Nivel>>();
        terminada = false;
        status = 0.5;
        arvore = null;
        parteAtual = 0;
    }
    public Fase(Fase fase) {
        this.id = fase.id;
        this.niveis = fase.niveis;
        this.titulo = fase.titulo;
        this.descricao = fase.descricao;
        this.nivelAtual = fase.nivelAtual;
        this.terminada = fase.terminada;
        this.status = fase.status;
        this.arvore = fase.arvore;
        this.parteAtual = fase.parteAtual;
    }
    public int compareTo(Fase fase) {
        return this.id - fase.id;
    }
    public Object clone() {
        Fase f = null;
        try {
            f = new Fase(this);
        } catch (Exception e) {}
        return f;
    }
    public boolean equals(Object outro) {
        if (outro == this)
            return true;
        if (outro == null)
            return false;
        if (outro.getClass() != getClass())
            return false;
        Fase fase = (Fase) outro;
        if (fase.id != this.id)
            return false;
        if (!fase.niveis.equals(this.niveis))
            return false;
        if (!fase.titulo.equals(this.titulo))
            return false;
        if (!fase.descricao.equals(this.descricao))
            return false;
        if (!fase.nivelAtual.equals(this.nivelAtual))
            return false;
        if (terminada != fase.terminada)
            return false;
        if (status != fase.status)
            return false;
        if (!arvore.equals(fase.arvore))
            return false;
        if (parteAtual != fase.parteAtual)
            return false;
        return true;
    }
    private String padLeft(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(' ');
        }
        sb.append(inputString);

        return sb.toString();
    }
    public String toString() {
        String s = "";
        s += id + ": " + titulo + "\t" + descricao;
        s += "\nNíveis: ";
        s += niveis.toString();
        s += padLeft((terminada?"Terminada":""), 10);
        s += "\t[" + status + "]";
        return s;
    }
    public int hashCode() {
        int ret = 3;
        ret = ret * 7 + new Integer(id).hashCode();
        ret = ret * 11 + niveis.hashCode();
        ret = ret * 17 + titulo.hashCode();
        ret = ret * 23 + descricao.hashCode();
        ret = ret * 29 + nivelAtual.hashCode();
        ret = ret * 31 + new Boolean(terminada).hashCode();
        ret = ret * 37 + new Double(status).hashCode();
        return ret;
    }
    public void atualizarNivelAtual() {
        if (nivelAtual == null)
            nivelAtual = niveis.get(0).get(0);
        if (nivelAtual.isTerminado()) {
            atualizarStatusPlayer();
            atualizarAmizadesPlayer();
            int result = nivelAtual.getEscolhaFeita().getParaOndeIr();
            parteAtual++;
            if (parteAtual >= niveis.size()) {
                arvore.atualizarFaseAtual();
            }
            else
                nivelAtual = niveis.get(parteAtual).get(result);
        }
    }
    public Nivel getNivelAtual() {
        atualizarNivelAtual();
        return nivelAtual;
    }
    public void setNivelAtual(Nivel n) {
        nivelAtual = n;
    }
    public void setNivelAtual(int i, int j) {
        nivelAtual = niveis.get(i).get(j);
    }
    public void avancarNivel() {
        if (parteAtual >= niveis.size()) {
            terminada = true;
        } else {
            atualizarStatusPlayer();
            atualizarAmizadesPlayer();
            int result = nivelAtual.getEscolhaFeita().getParaOndeIr();
            nivelAtual = niveis.get(++parteAtual).get(result);
        }
    }
    public void atualizarStatusPlayer() {
        Player player = arvore.getJogo().getPlayer();
        player.addToTranquilidade(nivelAtual.getEscolhaFeita().getStatus()[0]);
        player.addToFelicidade(nivelAtual.getEscolhaFeita().getStatus()[1]);
        player.addToSanidade(nivelAtual.getEscolhaFeita().getStatus()[2]);
        player.addToFinancas(nivelAtual.getEscolhaFeita().getStatus()[3]);
        player.addToInteligencia(nivelAtual.getEscolhaFeita().getStatus()[4]);
        player.addToCarisma(nivelAtual.getEscolhaFeita().getStatus()[5]);
        player.addToForca(nivelAtual.getEscolhaFeita().getStatus()[6]);
    }
    public void atualizarAmizadesPlayer() {
        Jogo jogo = arvore.getJogo();
        double[] amizades = nivelAtual.getEscolhaFeita().getAmizades();
        Personagem[] personagens = jogo.getPersonagens();
        for(int i = 0; i < personagens.length; i++) {
            personagens[i].addToAmizade(amizades[i]);
        }
    }
    public boolean isTerminada() {
        return terminada;
    }
    public double getStatus() {
        return status;
    }
    public void setId(int id) {this.id = id;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public void setNiveis(ArrayList<ArrayList<Nivel>> niveis) {this.niveis = niveis;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public void setTerminada(boolean terminada) {this.terminada = terminada;}
    public void addToStatus(double val) {this.status += val;}
    public Arvore getArvore() {
        return arvore;
    }
    public void setArvore(Arvore a) {
        arvore = a;
    }
}
