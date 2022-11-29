package com.archql.labs.wtlab3.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface IClient {
    void connect(String ip, int port) throws IOException;

    String sendMessage(String msg) throws IOException;

    void stop() throws IOException;
}
