package org.academiadecodigo.bootcamp8.server;

import org.academiadecodigo.bootcamp8.client.Client;
import org.academiadecodigo.bootcamp8.server.modes.Strategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Prashanta on 14/06/17.
 */
public class LiveChat {
    private static int port;
    private static ServerSocket serverSocket = null;
    private final String opPassword;
    private ExecutorService cachedPool;
    private final CopyOnWriteArrayList<ClientServerSide> loggedUsers;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: ChatServer <port> <operator_password>");
            System.exit(1);
        }

        LiveChat chatServer = new LiveChat(Integer.parseInt(args[0]), args[1]);
        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                chatServer.start(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                chatServer.closeServerSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private LiveChat(int port, String pass) {
        opPassword = pass;
        LiveChat.port = port;
        loggedUsers = new CopyOnWriteArrayList<>();
        cachedPool = Executors.newCachedThreadPool();
    }

    private void start(Socket clientSocket) {
        System.out.println("New client logged with IP: " + clientSocket.getRemoteSocketAddress());
        cachedPool.submit(new ClientServerSide(clientSocket, this));
    }

    public void addUser(ClientServerSide client){
        loggedUsers.add(client);
    }

    public void removeUser(ClientServerSide client){
        loggedUsers.remove(client);
    }

    public synchronized boolean validateUserName(String userName) {
        if (loggedUsers.isEmpty()) {
            return true;
        }
        for (ClientServerSide c : loggedUsers) {
            if (userName.equals(c.getUserName())) {
                return false;
            }
        }
        return true;

    }

    public synchronized void printLoggedUsers(ClientServerSide client) {
        StringBuilder builder = new StringBuilder();
        for (ClientServerSide c : loggedUsers) {
            builder.append(c.getUserName());
            if (c == client) {
                builder.append("-(me)");
            }
            builder.append(" ");
        }
        client.write(CustomMsg.USERLIST.getMessage() + builder.toString());
    }

    public void privateMsg(ClientServerSide client, String user, String msg) {
        for (ClientServerSide c : loggedUsers) {
            if (user.equals(c.getUserName())) {
                c.write(client.getUserName(), " whispered to you: " + msg);
                return;
            }
        }
        client.write(CustomMsg.UNKNOWNUSER.getMessage());
    }

    public synchronized void writeToAll(String user, String msg) {
        for (ClientServerSide c : loggedUsers) {
            if (user.equals(c.getUserName())) {
                continue;
            }
            c.write(user, msg);
        }
    }

    public boolean getOp(String pass) {
        return pass.equals(opPassword);
    }

    public void kickUser(ClientServerSide op, String user) {
        for (ClientServerSide c : loggedUsers) {
            if (user.equals(c.getUserName())) {
                c.write(CustomMsg.KICKEDBY.getMessage() + op.getUserName());
                c.closeClient();
                writeToAll(op.getUserName(), CustomMsg.KICKED.getMessage() + user);
                op.write(user + " was kicked from the chat room.");
                return;
            }
        }
        op.write(CustomMsg.UNKNOWNUSER.getMessage());
    }

    public void warn(ClientServerSide op, String user) {
        for (ClientServerSide c : loggedUsers) {
            if (user.equals(c.getUserName())) {
                c.write(CustomMsg.OPWARNING.getMessage());
                op.write(user + " was warned.");
                return;
            }
        }
        op.write(CustomMsg.UNKNOWNUSER.getMessage());
    }

    public void setMode(ClientServerSide op, Strategy mode, String user) {
        for (ClientServerSide c : loggedUsers) {
            if (user.equals(c.getUserName())) {
                c.setMode(mode);
                c.write(mode.getMsg());
                op.write("Mode set successfully. /opnormal <targetuser> to reset to normal.");
                return;
            }
        }
        op.write(CustomMsg.UNKNOWNUSER.getMessage());
    }

    private void closeServerSocket() throws IOException {
        serverSocket.close();
    }



}
