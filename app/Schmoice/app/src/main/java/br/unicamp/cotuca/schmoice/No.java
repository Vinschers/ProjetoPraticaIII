package br.unicamp.cotuca.schmoice;

import java.io.Serializable;
import java.util.ArrayList;

public class No implements Serializable {
    private Fase fase;
    private ArrayList<No> proxs;
    public No() {
        setFase(null);
        proxs = new ArrayList<No>();
    }
    public No(No no) {
        setFase(no.getFase());
        setProxs(no.getProxs());
    }
    public No(Fase fase, ArrayList<No> proxs) {
        this.fase = fase;
        this.proxs = proxs;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }
    public Fase getFase() {
        return fase;
    }
    public void setProxs(ArrayList<No> proxs) {this.proxs = proxs;}
    public ArrayList<No> getProxs() {return proxs;}

    public boolean equals(Object outro) {
        if (outro == this)
            return true;
        if (outro == null)
            return false;
        if (outro.getClass() != getClass())
            return false;
        No no = (No)outro;
        return this.proxs.equals(no.proxs) && this.getFase().equals(no.getFase());
    }
}
