package com.archql.labs.wtlab3.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ArchiveClient implements IClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void connect(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    @Override
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
