package org.academiadecodigo.bootcamp8.server;

/**
 * Created by prashanta on 17-06-2017.
 */
public enum OpCommands {
    OPISSUEWARNING("/opwarn", "Warns user to behave. Usage /opwarn <targetuser>"),
    OPKICK("/opkick", "Forces user logout. Usage /opkick <targetuser>"),
    OPMUTE("/opmute", "Silences user. Usage /opmute <targetuser>"),
    OPROCK("/oprock", "Turns on rock and roll mode for target user. Usage /oprock <targeruser>"),
    OPSTAR("/opstar", "Adds stars to everything target user says. Usage /oprock <targetuser>"),
    OPNORMAL("/opnormal", "Resets any mode change to target user. Usage /opnormal <targetuser>");


    private String command;
    private String description;

    OpCommands(String command, String description) {
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
