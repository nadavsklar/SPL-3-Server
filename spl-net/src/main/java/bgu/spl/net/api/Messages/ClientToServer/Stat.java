package bgu.spl.net.api.Messages.ClientToServer;

import bgu.spl.net.api.Messages.Message;

public class Stat extends Message {

    private String userName;

    public Stat() {
        super((short) 8,0);
        userName = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String messageString() { return "Stat"; }
}
