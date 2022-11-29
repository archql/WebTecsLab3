package com.archql.labs.wtlab3.server;

import java.io.IOException;
import java.util.Scanner;

public class ServerLauncher {
    public static void main(String[] args) {

        ArchiveServer server = new ArchiveServer("students.xml");
        try {
            server.start(8000);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Server launched (press any key to stop)");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        System.out.println("STOP LAUNCHED!\nWait, it can take up to 10 seconds...");

        server.stop();
    }
}
