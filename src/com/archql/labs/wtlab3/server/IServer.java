package com.archql.labs.wtlab3.server;

import java.io.IOException;
import java.net.ServerSocket;

public interface IServer {
    void start(int port) throws IOException;

    void stop();
}
