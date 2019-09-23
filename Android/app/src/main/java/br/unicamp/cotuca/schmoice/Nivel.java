package br.unicamp.cotuca.schmoice;

import java.awt.Image;
import java.util.ArrayList;

import br.unicamp.cotuca.schmoice.Escolha;

public class Nivel {
    private int id;
    private ArrayList<Escolha> escolhas;
    private String descricao;
    private Image background;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Escolha> getEscolhas() {
        return escolhas;
    }
    public void setEscolhas(ArrayList<Escolhas> escolhas) {
        this.escolhas = escolhas;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public Nivel() {
        escolhas = new ArrayList<Escolha>();
        id = -1;
        descricao = null;
        background = null;
    }

    public Nivel(Nivel nivel) {
        this.escolhas = nivel.escolhas;
        this.id = nivel.id;
        this.descricao = nivel.descricao;
        this.background = nivel.background;
    }

    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + escolhas.hashCode();
        ret = ret * 3 + new Integer(id).hashCode();
        ret = ret * 5 + descricao.hashCode();
        ret = ret * 7 + background.hashCode();
        return ret;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Nivel n = (Nivel)obj;
        if (!escolhas.equals(n.escolhas))
            return false;
        if (id != n.id)
            return false;
        if (!descricao.equals(n.descricao))
            return false;
        if (!background.equals(n.background))
            return false;
        return true;
    }

    public Object clone() {
        Nivel n = null;
        try {
            n = new Nivel(this);
        } catch (Exception ex) {}
        return n;
    }

    public String toString() {
        String s = id + ": " + descricao + "\n";
        s += "Escolhas: " + escolhas.toString() + "\n";
        s += "Imagem: " + background.toString();
        return s;
    }
}