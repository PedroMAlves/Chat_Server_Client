package org.academiadecodigo.bootcamp8.server.modes;

import org.academiadecodigo.bootcamp8.server.ClientServerSide;
import org.academiadecodigo.bootcamp8.server.LiveChat;

/**
 * Created by prashanta on 17-06-2017.
 */
public class Silent implements Strategy {

    @Override
    public void writeToAll(LiveChat server, ClientServerSide c, String user, String msg) {
        c.write("You have been silenced by the operator. Only you read your messages but you can still whisper.");
    }

    @Override
    public String getMsg() {
        return "OP muted you. You can still whisper to him and apologize.";
    }
}
