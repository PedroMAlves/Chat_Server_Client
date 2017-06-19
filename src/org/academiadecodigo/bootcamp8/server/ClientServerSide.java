package org.academiadecodigo.bootcamp8.server;

import org.academiadecodigo.bootcamp8.server.modes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Prashanta on 15/06/17.
 */
public class ClientServerSide implements Runnable {
    private LiveChat server;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean run;
    private String userName;
    private boolean operator;
    private Strategy mode;


    public ClientServerSide(Socket clientSocket, LiveChat server) {
        this.clientSocket = clientSocket;
        this.server = server;
        run = true;
        mode = new Normal();
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            login();
            welcome();
            while (run) {
                read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClientSocket();
        }
    }

    private void login() throws IOException {
        write(CustomMsg.REQUESTLOGIN.getMessage());
        String newUser = in.readLine();
        while (!server.validateUserName(newUser) || newUser.isEmpty()) {
            write(CustomMsg.TAKENUSER.getMessage());
            write(CustomMsg.REQUESTLOGIN.getMessage());
            newUser = in.readLine();
        }
        write("logged");
        write(CustomMsg.LOGINSUCCESS.getMessage());
        userName = newUser;
        server.addUser(this);
    }

    private void welcome() {
        write(userName + CustomMsg.WELCOME.getMessage());
        write("\n");
        server.printLoggedUsers(this);
        server.writeToAll(userName, CustomMsg.ENTRY.getMessage());
    }

    private void read() throws IOException {
        String userInput = in.readLine();
        if (userInput == null) {
            server.writeToAll(userName, CustomMsg.EXIT.getMessage());
            closeClient();
            return;
        }
        if (userInput.startsWith("/")) {
            if (userInput.startsWith("/op")) {
                opCommand(userInput);
                return;
            }
            executeCommand(userInput);
            return;
        }
        writeToAll(" says: " +userInput);
        //server.writeToAll(userName, " says: " +userInput);
    }

    public void writeToAll(String msg) {
        mode.writeToAll(server, this, userName, msg);
    }

    private void executeCommand(String command) {
        if (command.equals("/quit")) {
            write(CustomMsg.GOODBYE.getMessage());
            server.writeToAll(userName, CustomMsg.EXIT.getMessage());
            closeClient();
            return;
        }

        if (command.equals("/list")) {
            StringBuilder builder = new StringBuilder();
            builder.append(CustomMsg.COMMANDS.getMessage());
            for (UserCommands cmd : UserCommands.values()) {
                builder.append(cmd.getCommand() + " --> " + cmd.getDescription() + "\n");
            }
            write(builder.toString());
            return;
        }

        if (command.startsWith(UserCommands.SETUSERNAME.getCommand())) {
            if (command.split(" ").length < 2) {
                invalidMsg();
                return;
            }
            String newUser = command.split(" ")[1];
            if (server.validateUserName(newUser)) {
                userName = newUser;
                write(CustomMsg.USERCHANGED.getMessage());
            } else {
                write(CustomMsg.TAKENUSER.getMessage());
            }
            return;
        }

        if (command.equals(UserCommands.LOGGED.getCommand())) {
            server.printLoggedUsers(this);
            return;
        }

        if (command.startsWith(UserCommands.PRIVATEMSG.getCommand())) {
            if (command.split(" ").length < 3 || !command.contains("msg:")) {
                invalidMsg();
                return;
            }
            String user = command.split(" ")[1];
            String msg = command.split("msg:")[1];
            server.privateMsg(this, user, msg);
            return;
        }
        invalidMsg();
    }

    private void opCommand(String command) {
        if (command.split(" ").length < 2) {
            invalidMsg();
            return;
        }
        if (command.startsWith(UserCommands.OPLOGIN.getCommand())) {
            String pass = command.split(" ")[1];
            if (!server.getOp(pass)) {
                write(CustomMsg.OPDENIED.getMessage());
                return;
            } else {
                operator = true;
                userName = "OP_" + userName;
                write(CustomMsg.OPGRANTED.getMessage());
                return;
            }
        }
        if (!operator) {
            write(CustomMsg.NOPERMISSION.getMessage());
            return;
        }
        if (command.startsWith("/oplist")) {
            StringBuilder builder = new StringBuilder();
            builder.append(CustomMsg.COMMANDS.getMessage());
            for (OpCommands opCmd : OpCommands.values()) {
                builder.append(opCmd.getCommand() + " --> " + opCmd.getDescription() + "\n");
            }
            write(builder.toString());
            return;
        }
        if (command.startsWith(OpCommands.OPKICK.getCommand())) {
            server.kickUser(this, command.split(" ")[1]);
            return;
        }
        if (command.startsWith(OpCommands.OPISSUEWARNING.getCommand())) {
            server.warn(this, command.split(" ")[1]);
            return;
        }
        if (command.startsWith(OpCommands.OPMUTE.getCommand())) {
            server.setMode(this, new Silent(), command.split(" ")[1]);
        }
        if (command.startsWith(OpCommands.OPSTAR.getCommand())) {
            server.setMode(this, new StarMode(), command.split(" ")[1]);
        }
        if (command.startsWith(OpCommands.OPROCK.getCommand())) {
            server.setMode(this, new RockMode(), command.split(" ")[1]);
        }
        if (command.startsWith(OpCommands.OPNORMAL.getCommand())) {
            server.setMode(this, new Normal(), command.split(" ")[1]);
        }
    }

    public void write(String msg) {
        out.println(msg);
    }

    public void write(String user, String msg) {
        out.println(user + msg);
    }

    public void invalidMsg() {
        write(CustomMsg.INVALIDCOMMAND.getMessage());
    }

    public String getUserName() {
        return userName;
    }

    public void setMode(Strategy mode) {
        this.mode = mode;
    }

    public void closeClient() {
        run = false;
        server.removeUser(this);
        closeClientSocket();
    }

    private void closeClientSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

