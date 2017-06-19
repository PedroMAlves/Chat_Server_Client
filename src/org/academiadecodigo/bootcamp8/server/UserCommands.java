package org.academiadecodigo.bootcamp8.server;

/**
 * Created by prashanta on 17-06-2017.
 */
public enum UserCommands {
    SETUSERNAME("/setuser", "Changes username. No whitespaces allowed for username. Usage /setuser <newusername>"),
    LOGGED("/logged", "Prints to screen all currently logged users."),
    PRIVATEMSG("/whisper", "Sends private message to specified user. Usage /whisper <targetuser> msg:<yourmessage>"),
    OPLOGIN("/oplogin", "Get operator privileges. /oplogin <password>");



    private String command;
    private String description;

    UserCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
