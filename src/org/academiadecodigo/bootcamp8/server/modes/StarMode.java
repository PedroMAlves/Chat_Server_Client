package org.academiadecodigo.bootcamp8.server.modes;

import org.academiadecodigo.bootcamp8.server.ClientServerSide;
import org.academiadecodigo.bootcamp8.server.LiveChat;

/**
 * Created by prashanta on 17-06-2017.
 */
public class StarMode implements Strategy {

    @Override
    public void writeToAll(LiveChat server, ClientServerSide c, String user, String msg) {
        server.writeToAll(user," *************" + msg + " *************");
    }

    @Override
    public String getMsg() {
        return "OP punished you with Star Mode, you'll kiss everyone every time you write.";
    }
}
