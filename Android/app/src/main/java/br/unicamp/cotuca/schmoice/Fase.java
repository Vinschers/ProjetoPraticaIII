package br.unicamp.cotuca.schmoice;

import java.util.ArrayList;

public class Fase implements Comparable<Fase>, Cloneable {
    private int id;
    ArrayList<Nivel> niveis;
    private String titulo;
    private String descricao;
    public Fase() {
        niveis = new ArrayList<Nivel>();
    }
    public Fase(Fase fase) {
        this.id = fase.id;
        this.niveis = fase.niveis;
        this.titulo = fase.titulo;
        this.descricao = fase.descricao;
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
        return true;
    }
    public String toString() {
        String s = "";
        s += id + ": " + titulo + "\t" + descricao;
        s += "\nNíveis: ";
        s += niveis.toString();
        return s;
    }
    public int hashCode() {
        int ret = 3;
        ret = ret * 7 + new Integer(id).hashCode();
        ret = ret * 11 + niveis.hashCode();
        ret = ret * 17 + titulo.hashCode();
        ret = ret * 23 + descricao.hashCode();
        return ret;
    }
}
