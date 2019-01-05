package bgu.spl.net.api.Messages.ClientToServer;

import bgu.spl.net.api.Messages.Message;

public class
UserList extends Message {

    public UserList() {super((short) 7,0);}

    @Override
    public String messageString() { return "UserList"; }
}
