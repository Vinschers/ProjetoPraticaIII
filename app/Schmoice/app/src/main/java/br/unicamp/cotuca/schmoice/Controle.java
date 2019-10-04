package br.unicamp.cotuca.schmoice;

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
    private PrintWriter output;
    private BufferedReader input;
    private Socket socket = null;
    private static int serverPort = 80;
    private static String serverIp = "192.168.0.92";
    Leitor leitor;
    Escrevedor escrevedor;
    Eventos eventos;

    public Controle(Eventos ev) {
        eventos = ev;
    }
    public Controle() {

    }
    public Controle(String ip, int port) {
        setServerIp(ip);
        setServerPort(port);
    }


    public String ler() {
        if (leitor == null && !conectar()) {
            return null;
        }
        else {
            return leitor.getMensagem();
        }
    }

    public void escrever(String msg) {
        if (escrevedor == null && !conectar()) {
            escrever(msg);
        }
        else {
            escrevedor.escrever(msg);
        }
    }

    private class ClientThread implements Runnable, Serializable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(serverIp);

                socket = new Socket(serverAddr, serverPort);
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output.write("ok");
                output.flush();
                new Thread(new Leitor()).start();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    private class Leitor implements Runnable, Serializable {
        String msg = null;
        @Override
        public void run() {
            while (true) {
                try {
                    String atual = input.readLine();
                    if (atual != null) {
                        msg = atual;
                        String[] btns = msg.split(" ");
                        if (btns[0].equals("1")) {
                            eventos.onOK();
                        }
                        if (btns[1].equals("1")) {
                            eventos.onPraCima();
                        }
                        if (btns[2].equals("1")) {
                            eventos.onMenu();
                        }
                        if (btns[3].equals("1")) {
                            eventos.onPraBaixo();
                        }
                        if (btns[4].equals("1")) {
                            eventos.onPraDireita();
                        }
                        if (btns[5].equals("1")) {
                            eventos.onCancelar();
                        }
                        if (btns[6].equals("1")) {
                            eventos.onPraEsquerda();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public String getMensagem() {
            return msg;
        }
    }

    private class Escrevedor implements  Runnable, Serializable {
        @Override
        public void run() {
            while(socket != null) {}
        }
        public void escrever(String msg) {
            output.write(msg);
            output.flush();
        }
    }
    public boolean conectar() {
        return true;
        /*try {
            if (socket != null) {
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new Thread(new ClientThread()).start();
            leitor = new Leitor();
            Thread t = new Thread(leitor);
            t.start();
            t.join();
            return true;
        } catch (InterruptedException ex) {return false;}*/
    }
    public void desconectar() {
        return;
        //escrever("\r\n\r\n");
    }

    public void setServerIp(String ip) {
        serverIp = ip;
    }
    public void setServerPort(int port) {
        serverPort = port;
    }
    public String getServerIp() {
        return serverIp;
    }
    public int getServerPort() {
        return serverPort;
    }
    public void setEventos(Eventos e) {
        this.eventos = e;
    }
}
