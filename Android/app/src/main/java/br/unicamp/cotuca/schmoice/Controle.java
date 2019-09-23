package br.unicamp.cotuca.schmoice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controle {
    private PrintWriter output;
    private BufferedReader input;
    private Socket socket = null;
    private static int serverPort = 80;
    private static String serverIp = "192.168.0.92";
    Leitor leitor;
    Escrevedor escrevedor;

    public Controle() {
        conectar();
    }
    public Controle(String ip, int port) {
        setServerIp(ip);
        setServerPort(port);
        conectar();
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

    private class ClientThread implements Runnable {

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

    private class Leitor implements Runnable {
        String msg = null;
        @Override
        public void run() {
            while (true) {
                try {
                    String atual = input.readLine();
                    if (atual != null) {
                        msg = atual;
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

    private class Escrevedor implements  Runnable {
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
        try {
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
        } catch (InterruptedException ex) {return false;}
    }

    public void setServerIp(String ip) {
        serverIp = ip;
    }
    public void setServerPort(int port) {
        serverPort = port;
    }
    public String gerServerIp() {
        return serverIp;
    }
    public int getServerPort() {
        return serverPort;
    }
}
