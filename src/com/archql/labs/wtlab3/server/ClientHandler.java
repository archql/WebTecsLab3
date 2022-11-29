package com.archql.labs.wtlab3.server;

import com.archql.labs.wtlab3.logics.Rights;
import com.archql.labs.wtlab3.logics.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Integer.parseInt;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public AtomicBoolean running;

    ConcurrentHashMap<ServerUser, AuthorizedServerUser> usersSynced;
    ConcurrentHashMap<String, Student> students;
    AuthorizedServerUser user;
    Rights prevCommand = Rights.NONE;

    public ClientHandler(Socket accept, ConcurrentHashMap<ServerUser,
            AuthorizedServerUser> usersSynced,
            ConcurrentHashMap<String, Student> students) {
        this.clientSocket = accept;
        this.usersSynced = usersSynced;
        this.students = students;
    }

    @Override
    public synchronized void start() {
        user = null;
        running = new AtomicBoolean(false);
        // INIT
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            close();
            return;
        }

        running.set(true);
        super.start();
    }

    @Override
    public void run() {
        // SOME SERVER LOGICS HERE
        while (running.get()) {
            String inputLine;
            try {
                inputLine = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (inputLine.startsWith("REG")) {
                // format REG login md5(password) rights [CAN BE LITERALLY ANY! FIX THIS LATER!]
                int spacePos = inputLine.indexOf(' ', 3);
                int nextSpacePos = inputLine.indexOf(' ', spacePos);
                ServerUser user = new ServerUser(
                    inputLine.substring(3, spacePos),
                    inputLine.substring(spacePos + 1, nextSpacePos)
                );
                if (usersSynced.containsKey(user)) {
                    out.println("your credentials are somehow wrong!");
                    continue;
                }
                int rights;
                try {
                    rights = parseInt(inputLine.substring(nextSpacePos + 1), 16);
                }
                catch(Exception e)
                {
                    out.println("your credentials are somehow wrong (number parse error)!");
                    continue;
                }
                var authUser = new AuthorizedServerUser(user, rights);
                usersSynced.put(user, authUser);
            } else if (inputLine.startsWith("AUT")) {
                // format AUT login md5(password)
                int spacePos = inputLine.indexOf(' ', 3);
                ServerUser user = new ServerUser(
                        inputLine.substring(3, spacePos),
                        inputLine.substring(spacePos + 1)
                );
                if (!usersSynced.containsKey(user)) {
                    out.println("your credentials are somehow wrong!");
                    continue;
                }
                this.user = usersSynced.get(user);
                this.user.sessionOpened = true;
            }
            if (user == null) {
                out.println("you need to authorize yourself!");
                continue;
            }

            if (inputLine.startsWith("GET")) {
                // format GET id
                if (user.hasRight(Rights.GET)) {
                    out.println("you do not have rights!");
                    continue;
                }
                String id = inputLine.substring(3);
                //
            } else if (inputLine.startsWith("SET")) {
                // format SET document
                if (user.hasRight(Rights.SET)) {
                    out.println("you do not have rights!");
                    continue;
                }
                prevCommand = Rights.SET;

            } else if (inputLine.startsWith("NEW")) {
                // format NEW document
                if (user.hasRight(Rights.NEW)) {
                    out.println("you do not have rights!");
                    continue;
                }
                prevCommand = Rights.NEW;

            } else if (inputLine.startsWith("DEL")) {
                // format DEL id
                if (user.hasRight(Rights.DEL)) {
                    out.println("you do not have rights!");
                    continue;
                }
                String id = inputLine.substring(3);
                students.remove(id);

            } else if (inputLine.startsWith("BYE")) {
                user.sessionOpened = false;
                out.println("connection close success");
                break;
            } else {
                // check previous command
                switch (prevCommand){
                    case NEW -> {
                        var student = Student.fromString(inputLine);
                        if (student == null)
                        {
                            out.println("wrong data format");
                        }
                        else
                        {
                            students.put(student.getId(), student);
                        }
                    }
                    case SET -> {
                        var student = Student.fromString(inputLine);
                        if (student == null)
                        {
                            out.println("wrong data format");
                        }
                        else
                        {
                            students.remove(student.getId());
                            students.put(student.getId(), student);
                        }
                    }
                    default -> out.println("wrong command!");
                }
                prevCommand = Rights.NONE;
            }
            // just echo
            out.println(inputLine);
        }

        // EXIT
        close();
    }

    public void close()
    {
        System.out.println("Connection closed");
        if (user != null)
        {
            user.sessionOpened = false;
        }
        running.set(false);
        // EXIT
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
