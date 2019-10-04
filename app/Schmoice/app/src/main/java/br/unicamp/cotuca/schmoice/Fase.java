package br.unicamp.cotuca.schmoice;

import java.util.ArrayList;

public class Fase implements Comparable<Fase>, Cloneable {
    private int id;
    ArrayList<Nivel> niveis;
    private String titulo;
    private String descricao;
    private Nivel nivelAtual;
    private boolean terminada;
    private double status;
    public Fase() {
        nivelAtual = null;
        niveis = new ArrayList<Nivel>();
        terminada = false;
        status = 0.5;
    }
    public Fase(Fase fase) {
        this.id = fase.id;
        this.niveis = fase.niveis;
        this.titulo = fase.titulo;
        this.descricao = fase.descricao;
        this.nivelAtual = fase.nivelAtual;
        this.terminada = fase.terminada;
        this.status = fase.status;
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
    public Nivel getNivelAtual() {
        return nivelAtual;
    }
    public void setNivelAtual(Nivel n) {
        nivelAtual = n;
    }
    public void setNivelAtual(int i) {
        nivelAtual = niveis.get(i);
    }
    public void avancarNivel() {
        int index = niveis.indexOf(nivelAtual);
        if (index >= niveis.size()) {
            terminada = true;
        } else {
            nivelAtual = niveis.get(++index);
        }
    }
    public boolean isTerminada() {
        return terminada;
    }
    public double getStatus() {
        return status;
    }
}
