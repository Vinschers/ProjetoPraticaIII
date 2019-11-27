package br.unicamp.cotuca.schmoice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class Controle implements Serializable {
    public static String macAdress = "00:18:E5:03:93:5E";
    Eventos eventos;
    boolean funcionando = false;
    ConnectionThread conexao;

    public Controle(Eventos ev) {
        eventos = ev;
    }
    public Controle() {

    }

    public boolean conectar() {
       conexao = new ConnectionThread(macAdress, this, null);
       conexao.start();

        return true;
    }
    public boolean conectar(Handler handlerConexao) {
       conexao = new ConnectionThread(macAdress, this, handlerConexao);
       conexao.start();

        return true;
    }
    public void desconectar(boolean async) {
       conexao.running = false;

       if (!async)
            while (conexao.isConnected()) {}

        conexao = null;
    }

    public void setEventos(Eventos e) {
        this.eventos = e;
    }
}
