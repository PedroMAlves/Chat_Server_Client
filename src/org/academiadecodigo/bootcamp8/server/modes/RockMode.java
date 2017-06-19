package org.academiadecodigo.bootcamp8.server.modes;

import org.academiadecodigo.bootcamp8.server.ClientServerSide;
import org.academiadecodigo.bootcamp8.server.LiveChat;

/**
 * Created by prashanta on 17-06-2017.
 */
public class RockMode implements Strategy {
    @Override
    public void writeToAll(LiveChat server, ClientServerSide c, String user, String msg) {
        server.writeToAll(user, " ROCK AND ROLL DUDE" + msg + " ELVIS IS ALIVE!");
    }

    @Override
    public String getMsg() {
        return "OP punished you with Rock Mode.";
    }
}
