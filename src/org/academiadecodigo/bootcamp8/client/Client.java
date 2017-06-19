package org.academiadecodigo.bootcamp8.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Prashanta on 14/06/17.
 */
public class Client {
    private int port;
    private String hostName;
    private Socket clientSocket = null;
    private BufferedReader in;
    private static boolean run;


    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: TCP Java Chatclient <host> <port>");
            System.exit(1);
        }

        Client client = new Client(args[0], Integer.parseInt(args[1]));

        try {
            client.start();
            while (run) {
                client.getGroupChat();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.closeSocket();
        }
    }


    private Client(String hostName, int port){
        this.port = port;
        this.hostName = hostName;
        run = true;
    }

    private void start() throws IOException {
        postWelcome();
        setSocket();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        login();
        Thread t1 = new Thread(new WriteMsg());
        t1.start();
    }

    private void postWelcome() {
        BufferedReader bReader = null;
        String str;
        try {
            bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Welcome")));
            while ((str = bReader.readLine()) != null) {
                System.out.println(str);
                Thread.sleep(200);
            }
            Thread.sleep(800);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bReader != null) {
                    bReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void login() throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        Scanner scan = new Scanner(System.in);
        String str = in.readLine();
        while (!str.equals("logged")) {
            System.out.println(str);
            if (!str.startsWith("Invalid")) {
                out.println(scan.nextLine());
            }
            str = in.readLine();
        }
        System.out.println(in.readLine());
    }

    private void setSocket() {
        try {
            clientSocket = new Socket(hostName, port);
        } catch (IOException e) {
            System.out.println("Unable to connect. Make sure you are connecting " +
                    "to host and port where server is running");
            System.exit(1);
        }
    }

    private void getGroupChat() throws IOException {
        String str;
        while ((str = in.readLine()) != null && !str.isEmpty()) {
            System.out.println(str);
            if (str.contains("You have been kicked from the chat room by op")) {
                System.exit(0);
            }
        }
    }

    private void closeSocket() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class WriteMsg implements Runnable {
        private PrintWriter out = null;
        Scanner scan;

        public WriteMsg() {
            scan = new Scanner(System.in);
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (run) {
                String str = userMsg();
                if (str.equals("/alist")) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Available Ascii Art: \n");
                    for (AsciiArt art : AsciiArt.values()) {
                        builder.append(" --> " + art.getDescription() + "\n");
                    }
                    System.out.println(builder.toString());
                    continue;
                }
                if (str.startsWith("/ascii")) {
                    handleAscii(str);
                    continue;
                }
                out.println(str);
                if (str.equals("/quit")) {
                    run = false;
                    scan.close();
                }
            }
        }

        private String userMsg() {
            return scan.nextLine();
        }

        private void handleAscii(String request) {
            if (request.split(" ").length != 2) {
                System.out.println("Wrong usage of command. Type /alist for correct usage");
                return;
            }
            for (AsciiArt art : AsciiArt.values()) {
                if (request.equals(art.getCommand())) {
                    print(art);
                    return;
                } else {
                    System.out.println("Invalid ascii. Type /alist for available art");
                }
            }
        }

        private void print(AsciiArt art) {
            BufferedReader bReader = null;
            String str;
            try {
                bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(art.getFilePath())));
                while ((str = bReader.readLine()) != null) {
                    System.out.println(str);
                    out.println(str);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bReader != null) {
                        bReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}




