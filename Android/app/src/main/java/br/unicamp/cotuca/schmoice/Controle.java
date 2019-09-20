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
    boolean  isRunning = false;

    private class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(serverIp);

                socket = new Socket(serverAddr, serverPort);
                isRunning = true;
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
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        //message
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void conectar(String ip) {
        if (ip != null && ip != "")
            serverIp = ip;
        if (socket == null)
        {
            new Thread(new ClientThread()).start();
        }
        else {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            new Thread(new ClientThread()).start();
        }
    }
}
