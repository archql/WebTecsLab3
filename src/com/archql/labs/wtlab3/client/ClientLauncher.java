package com.archql.labs.wtlab3.client;

import com.archql.labs.wtlab3.logics.Student;

import java.io.Console;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;


public class ClientLauncher {

    public static String md5(String password) {
        if (password == null || password.length() == 0)
            return null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }

    public static String getString(Scanner sc, String message) {
        String str;
        do {
            System.out.println("Please, input " + message);
            str = sc.nextLine();
        } while (str.length() == 0);
        return str;
    }

    public static void main(String[] args) {
        ArchiveClient myClient = new ArchiveClient();

        String login, password;
        Student currentStudent = null;

        System.out.println("Student archive client interface");
        try {
            myClient.connect("127.0.0.1", 8000);
        } catch (IOException e) {
            System.out.println("Failed connection!");
            return;
        }
        Scanner sc= new Scanner(System.in);

        while(true) {
            System.out.print("> ");
            String line = sc.nextLine();

            // catch command and reform it correctly
            if (line.equals("EXIT"))
                break;
            else if (line.startsWith("AUT"))
            {
                System.out.println("Please, input your login");
                login = sc.nextLine();
                password = null;
                while (password == null) {
                    System.out.println("Please, input your password");
                    password = md5(sc.nextLine());
                }
                line = "AUT " + login + ' ' + password;
            }
            else if (line.startsWith("REG"))
            {
                System.out.println("Please, input your login");
                login = sc.nextLine();
                password = null;
                while (password == null) {
                    System.out.println("Please, input your password");
                    password = md5(sc.nextLine());
                }
                System.out.println("Please, input your role (hex)");
                String role = sc.nextLine();
                line = "REG " + login + ' ' + password + ' ' + role;
            }
            else if (line.startsWith("NEW"))
            {
                String id = getString(sc, "students id");
                String name = getString(sc, "students name");
                String surname = getString(sc, "students surname");
                String groupNo = getString(sc, "students groupNo");
                currentStudent = new Student(id, name, surname, groupNo);

                line = "NEW\n" + Student.toSingleLineString(Student.toString(currentStudent));
            }
            else if (line.startsWith("SET"))
            {
                if (currentStudent == null)
                {
                    System.out.println("currentStudent is null! GET or NEW first!");
                    continue;
                }
                boolean ifContinue = false;
                boolean ifSkip = false;
                while (!ifContinue && !ifSkip) {
                    System.out.println("""
                            Choose what you wish to edit:\s
                            (1) name
                            (2) surname
                            (3) groupNo
                            (0) exit with save
                            (any) exit wout send to server""");
                    String readed = sc.nextLine();
                    if (readed.length() == 0) {
                        ifSkip = true;
                        break;
                    }
                    switch (readed.charAt(0)) {
                        case '1' -> currentStudent.setName(getString(sc, "students name"));
                        case '2' -> currentStudent.setSurname(getString(sc, "students surname"));
                        case '3' -> currentStudent.setGroupNumber(getString(sc, "students group number"));
                        case '0' -> ifContinue = true;
                        default -> ifSkip = true;
                    }
                }
                if (ifSkip)
                    continue;
                line = "SET\n" + Student.toSingleLineString(Student.toString(currentStudent));
            }
            // get ans
            String answer;
            try {
                //System.out.println("TEMP LOG " + line);
                answer = myClient.sendMessage(line);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            System.out.println(answer);
            if (line.equals("BYE"))
                break;
            else if (line.startsWith("GET"))
            {
                answer = Student.toMultiLineString(answer);
                if (!answer.equals("not exists")) {
                    currentStudent = Student.fromString(answer);

                    answer = myClient.waitForNextMessage();
                    if (answer != null)
                        System.out.println(answer);
                }
            }
        }
        try {
            myClient.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
