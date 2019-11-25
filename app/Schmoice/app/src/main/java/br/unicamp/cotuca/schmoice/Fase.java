package br.unicamp.cotuca.schmoice;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;

public class Fase implements Serializable {
    private int idFase;
    private Nivel[][] niveis;
    private String titulo;
    private String descricao;
    private Nivel nivelAtual;
    private boolean terminada;
    private double status;
    private Player playerAntigo;
    private int parteAtual, rotaAtual = 0;
    private Arvore arvore;
    private Requerimentos reqs;
    private ArrayList<Integer> caminhoFase;

    public Fase() {
        nivelAtual = null;
        niveis = new Nivel[10][10];
        caminhoFase = new ArrayList<Integer>();
        terminada = false;
        status = 0.5;
        arvore = null;
        parteAtual = 0;
        reqs = new Requerimentos();
    }

    public boolean equals(Object outro) {
        if (outro == this)
            return true;
        if (outro == null)
            return false;
        if (outro.getClass() != getClass())
            return false;
        Fase fase = (Fase) outro;
        if (fase.idFase != this.idFase)
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
        s += idFase + ": " + titulo + "\t" + descricao;
        s += "\nNíveis: ";
        s += niveis.toString();
        s += padLeft((terminada?"Terminada":""), 10);
        s += "\t[" + status + "]";
        return s;
    }
    public int hashCode() {
        int ret = 3;
        ret = ret * 7 + new Integer(idFase).hashCode();
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
            nivelAtual = niveis[0][0];
        if (nivelAtual.isTerminado()) {
            avancarNivel();
        }
    }
    public Nivel getNivelAtual() {
        atualizarNivelAtual();
        return nivelAtual;
    }
    public void setNivelAtual(Nivel n) {
        nivelAtual = n;
    }
    public void setNivelAtual(int rota, int num) {
        nivelAtual = niveis[rota][num];
    }
    public void avancarNivel() {
        if (nivelAtual.getEscolhaFeita().getImportancia() != -1) {
            if (nivelAtual.getEscolhaFeita().getPosImportancia() != -1)
                arvore.getJogo().getEscolhasImportantes().add(nivelAtual.getEscolhaFeita().getPosImportancia(), nivelAtual.getEscolhaFeita().getImportancia());
            else
                arvore.getJogo().getEscolhasImportantes().add(nivelAtual.getEscolhaFeita().getImportancia());
        }

        if (parteAtual >= niveis[rotaAtual].length - 1) {
            terminada = true;
        } else {
            if (nivelAtual.getTipo() == 0) {
                atualizarStatusPlayer();
                status += nivelAtual.getEscolhaFeita().getStatusFase();
                rotaAtual = nivelAtual.getEscolhaFeita().getParaOndeIr();
            }
            parteAtual += 1 + nivelAtual.getEscolhaFeita().getParaOndeIrNaRota();
            nivelAtual = niveis[rotaAtual][parteAtual];
        }
    }
    public void avancarNivel(int rotaNova) {
        if (nivelAtual.getEscolhaFeita().getPosImportancia() != -1)
            arvore.getJogo().getEscolhasImportantes().add(nivelAtual.getEscolhaFeita().getPosImportancia(), nivelAtual.getEscolhaFeita().getImportancia());
        else
            arvore.getJogo().getEscolhasImportantes().add(nivelAtual.getEscolhaFeita().getImportancia());
        rotaAtual = rotaNova;
        if (parteAtual >= niveis[rotaAtual].length - 1) {
            terminada = true;
        } else {
            parteAtual += 1 + nivelAtual.getEscolhaFeita().getParaOndeIrNaRota();
            nivelAtual = niveis[rotaAtual][parteAtual];
        }
    }
    public void atualizarStatusPlayer() {
        Player player = arvore.getJogo().getPlayer();
        player.addToTranquilidade(nivelAtual.getEscolhaFeita().getStatusPlayer()[0]);
        player.addToFelicidade(nivelAtual.getEscolhaFeita().getStatusPlayer()[1]);
        player.addToSanidade(nivelAtual.getEscolhaFeita().getStatusPlayer()[2]);
        player.addToFinancas(nivelAtual.getEscolhaFeita().getStatusPlayer()[3]);
        player.addToInteligencia(nivelAtual.getEscolhaFeita().getStatusPlayer()[4]);
        player.addToCarisma(nivelAtual.getEscolhaFeita().getStatusPlayer()[5]);
        player.addToForca(nivelAtual.getEscolhaFeita().getStatusPlayer()[6]);
    }
    public boolean isTerminada() {
        return terminada;
    }
    public double getStatus() {
        return status;
    }
    public void setId(int id) {this.idFase = id;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public void setNiveis(Nivel[][] niveis) {
        this.niveis = niveis;
    }
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public void setTerminada(boolean terminada) {this.terminada = terminada;}
    public void addToStatus(double val) {this.status += val;}
    public Arvore getArvore() {
        return arvore;
    }
    public void setArvore(Arvore a) {
        arvore = a;
    }

    public void setParteAtual(int p) {
        parteAtual = p;
    }
    public int getParteAtual() {return parteAtual;}
    public Nivel[][] getNiveis() {
        return niveis;
    }
    public void setRotaAtual(int r) {
        rotaAtual = r;
    }
    public int getRotaAtual() {return rotaAtual;}

    public int getIdFase() {return idFase;}
    public void setIdFase(int i) {idFase = i;}

    public void setReqs(Requerimentos reqs) {this.reqs = reqs;}
    public Requerimentos getReqs() {return reqs;}

    public ArrayList<Integer> getCaminhoFase() {
        return caminhoFase;
    }
    public void setCaminhoFase(String caminhoFase) throws Exception {
        this.caminhoFase = (ArrayList<Integer>)ClienteWS.fromJson(caminhoFase, ArrayList.class);
    }

    public Player getPlayerAntigo() {
        return playerAntigo;
    }
    public void setPlayerAntigo(Player playerAntigo) {
        this.playerAntigo = playerAntigo;
    }
}
