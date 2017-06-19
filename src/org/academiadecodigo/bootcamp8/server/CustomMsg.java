package org.academiadecodigo.bootcamp8.server;

/**
 * Created by Prashanta on 15/06/17.
 */
public enum CustomMsg {
    REQUESTLOGIN("Please enter username to login (no whitespaces allowed): "),
    TAKENUSER("Invalid or already taken username. Try again."),
    LOGINSUCCESS("Username set successfully. You can change it at anytime with /setuser <newusername>"),
    USERCHANGED("Username changed successfully."),
    WELCOME(" welcome to this chat server. Have fun but behave, OP is watching you!\n" +
            "Useful commands: /quit to exit or /list for list of available commands."),
    ENTRY(" just entered the chat room."),
    EXIT(" just left the chat room."),
    GOODBYE("Logging out...\n" + "Logged out. Have a nice day. We hope to see you soon."),
    USERLIST("Users currently logged: "),
    COMMANDS("Available commands: \n"),
    INVALIDCOMMAND("Invalid or wrong usage of command. Type /list for available commands and usage."),
    UNKNOWNUSER("Unknown user. Check for typos or /logged to check currently logged users."),
    OPGRANTED("Operator privileges granted. Type /oplist <password> for list of available operator commands."),
    OPDENIED("Wrong password. Operator privileges not granted."),
    OPWARNING("OP says: Behave or face the consequences. I'm OP, I'm Lord of this chat room! \n"),
    NOPERMISSION("You do not have privileges to run this command. Get OP first."),
    KICKEDBY("You have been kicked from the chat room by op "),
    KICKED(" kicked user ");


    private String message;

    CustomMsg(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }
}
