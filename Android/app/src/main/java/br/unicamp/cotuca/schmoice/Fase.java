package br.unicamp.cotuca.schmoice;

import java.util.ArrayList;

public class Fase implements Comparable<Fase>, Cloneable {
    private int id;
    ArrayList<Escolha> escolhas;
    public Fase() {
        escolhas = new ArrayList<Escolha>();
    }
    public Fase(Fase fase) {
        this.escolhas = fase.escolhas;
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
        if (!fase.escolhas.equals(this.escolhas))
            return false;
        return true;
    }
    public String toString() {
        String s = "";
        s += id + ": ";
        s += escolhas.toString();
        return s;
    }
    public int hashCode() {
        int ret = 3;
        ret = ret * 7 + new Integer(id).hashCode();
        ret = ret * 11 + escolhas.hashCode();
        return ret;
    }
}
