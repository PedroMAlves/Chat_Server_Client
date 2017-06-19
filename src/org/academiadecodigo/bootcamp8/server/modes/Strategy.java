package org.academiadecodigo.bootcamp8.server.modes;

import org.academiadecodigo.bootcamp8.server.ClientServerSide;
import org.academiadecodigo.bootcamp8.server.LiveChat;

/**
 * Created by prashanta on 17-06-2017.
 */
public interface Strategy {

    void writeToAll(LiveChat server, ClientServerSide c, String user, String msg);

    String getMsg();
}
