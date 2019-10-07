package br.unicamp.cotuca.schmoice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import br.unicamp.cotuca.schmoice.Escolha;

public class Nivel {
    private int id;
    private ArrayList<Escolha> escolhas;
    private String descricao;
    private Bitmap background;
    private boolean terminado;
    private Fase parentFase;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Escolha> getEscolhas() {
        return escolhas;
    }
    public void setEscolhas(ArrayList<Escolha> escolhas) {
        this.escolhas = escolhas;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public boolean isTerminado() {return terminado;}
    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
        parentFase.atualizarNivelAtual();
    }

    public Fase getParentFase() {
        return parentFase;
    }
    public void setParentFase(Fase fase) {
        parentFase = fase;
    }

    public Nivel() {
        escolhas = new ArrayList<Escolha>();
        id = -1;
        descricao = null;
        background = null;
        terminado = false;
        parentFase = null;
    }

    public Nivel(Nivel nivel) {
        this.escolhas = nivel.escolhas;
        this.id = nivel.id;
        this.descricao = nivel.descricao;
        this.background = nivel.background;
        this.terminado = nivel.terminado;
        this.parentFase = nivel.parentFase;
    }

    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + escolhas.hashCode();
        ret = ret * 3 + new Integer(id).hashCode();
        ret = ret * 5 + descricao.hashCode();
        ret = ret * 7 + background.hashCode();
        ret = ret * 11 + new Boolean(terminado).hashCode();
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
        if (terminado != n.terminado)
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
        s += padLeft((terminado?"Terminado":""), 10);
        return s;
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

    public void efetuarEscolha(Escolha e) {
        //fazer as coisas
        setTerminado(true);
        parentFase.atualizarNivelAtual();
    }
}