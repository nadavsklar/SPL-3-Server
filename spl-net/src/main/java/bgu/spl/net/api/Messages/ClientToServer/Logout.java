package bgu.spl.net.api.Messages.ClientToServer;

import bgu.spl.net.api.Messages.Message;

public class Logout extends Message {

    public Logout() {super((short) 3,0);}

    @Override
    public String messageString() { return "Logout"; }

    @Override
    public String toString() { return "Logout"; }
}
