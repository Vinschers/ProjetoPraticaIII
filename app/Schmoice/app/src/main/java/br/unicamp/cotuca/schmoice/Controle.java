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
    public static String macAdress = "18:E5:3935E";
    Eventos eventos;
    int statusFuncionando = 0; // 1 - OK; 2 - ERRO
    ConnectionThread conexao;

    public Controle(Eventos ev) {
        eventos = ev;
    }
    public Controle() {

    }

    public boolean conectar(boolean async) {
        conexao = new ConnectionThread(macAdress, this);
        conexao.start();

        if (!async)
            while (statusFuncionando == 0) {}

        return statusFuncionando == 1;
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
