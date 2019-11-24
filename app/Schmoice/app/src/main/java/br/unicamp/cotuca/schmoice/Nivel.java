package br.unicamp.cotuca.schmoice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import br.unicamp.cotuca.schmoice.Escolha;

public class Nivel implements Serializable {
    private int idNivel;
    private ArrayList<Escolha> escolhas;
    private ArrayList<Personagem> personagens;
    private String descricao;
    private String background;
    private boolean terminado;
    private Fase parentFase;
    private Escolha escolhaFeita;
    private int tipo; // 0 -> normal; 1 -> minigame 1; 2 -> minigame 2.
    private int rotaVitoria;
    private int parte;
    private int rota;
    private int dependenciaStatus;

    public int getDependenciaStatus() {
        return dependenciaStatus;
    }
    public void setDependenciaStatus(int dependenciaStatus) {
        this.dependenciaStatus = dependenciaStatus;
    }

    public int getParte() {
        return parte;
    }

    public void setParte(int parte) {
        this.parte = parte;
    }

    public int getRota() {
        return rota;
    }

    public void setRota(int rota) {
        this.rota = rota;
    }

    public void setIdNivel(int i) {idNivel = i;}
    public int getIdNivel() {return idNivel;}

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

    public String getBackground() {
        return background;
    }
    public void setBackground(String background) {
        this.background = background;
    }

    public boolean isTerminado() {return terminado;}
    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public Fase getParentFase() {
        return parentFase;
    }
    public void setParentFase(Fase fase) {
        parentFase = fase;
    }

    public int getTipo() {return  tipo;}
    public void setTipo(int tipo) {this.tipo = tipo;}

    public int getRotaVitoria() {return rotaVitoria;}
    public void setRotaVitoria(int r) {rotaVitoria = r;}

    public ArrayList<Personagem> getPersonagens() {return personagens;}
    public void setPersonagens(ArrayList<Personagem> personagens) {
        this.personagens = personagens;
    }

    public Nivel() {
        personagens = new ArrayList<Personagem>();
        escolhas = new ArrayList<Escolha>();
        descricao = null;
        background = null;
        terminado = false;
        parentFase = null;
    }

    public Nivel(Nivel nivel) {
        this.escolhas = nivel.escolhas;
        this.descricao = nivel.descricao;
        this.background = nivel.background;
        this.terminado = nivel.terminado;
        this.parentFase = nivel.parentFase;
        this.escolhaFeita = nivel.escolhaFeita;
        this.tipo = nivel.tipo;
        this.personagens = nivel.personagens;
    }

    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + escolhas.hashCode();
        ret = ret * 5 + descricao.hashCode();
        ret = ret * 7 + background.hashCode();
        ret = ret * 11 + new Boolean(terminado).hashCode();
        ret = ret * 13 + Integer.valueOf(tipo).hashCode();
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
        if (!descricao.equals(n.descricao))
            return false;
        if (!background.equals(n.background))
            return false;
        if (terminado != n.terminado)
            return false;
        if (tipo != n.tipo)
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
        String s = "";
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
        escolhaFeita = e;
        setTerminado(true);
        parentFase.atualizarNivelAtual();
    }
    public Escolha getEscolhaFeita() {
        return escolhaFeita;
    }
}