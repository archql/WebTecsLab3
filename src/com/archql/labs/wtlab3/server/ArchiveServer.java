package com.archql.labs.wtlab3.server;

import com.archql.labs.wtlab3.logics.Student;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArchiveServer implements IServer {
    private ServerSocket serverSocket;
    private AtomicBoolean running;
    private final List<ClientHandler> openedConnectionsHandlers
            = new LinkedList<ClientHandler>();
    private final ConcurrentHashMap<ServerUser, AuthorizedServerUser> usersSynced
            = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Student> students
            = new ConcurrentHashMap<>();
    private String file;

    ArchiveServer(String file)
    {
        readDB(file);
    }

    public void readDB(String file)
    {
        this.file = file;
        // read students from xml db
        XMLDecoder decoder=null;
        try {
            decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found - " + file);
            return;
        }
        while (true) {
            var obj = decoder.readObject();
            if (obj == null)
                break;
            students.put(((Student)obj).getId(), (Student)obj);
        }
        decoder.close();
    }

    public void start(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);

        running = new AtomicBoolean(true);
        new Thread( () -> {
            while (running.get()) {
                System.out.println("Waiting for client on port " + port + "...");
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                } catch (SocketTimeoutException s) {
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                System.out.println("Opened new connection");
                var clientHandler = new ClientHandler(clientSocket, usersSynced, students);
                openedConnectionsHandlers.add(clientHandler);
                clientHandler.start();
            }
            // stop request arrived
            for (var clientHandler: openedConnectionsHandlers) {
                clientHandler.close();
            }
            //
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } ).start();
    }

    public void stop() {
        // stop request
        running.set(false);

        writeDB(file);
    }

    public void writeDB(String file)
    {
        // serialize all db
        XMLEncoder encoder=null;
        try{
            encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
        }catch(FileNotFoundException fileNotFound){
            System.out.println("ERROR: While Creating or Opening the File - " + file);
            return;
        }
        XMLEncoder finalEncoder = encoder;
        students.forEach((k, v) -> { finalEncoder.writeObject(v); });
        encoder.close();
    }
}
