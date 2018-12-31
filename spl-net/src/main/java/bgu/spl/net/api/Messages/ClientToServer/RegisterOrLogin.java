package bgu.spl.net.api.Messages.ClientToServer;

import bgu.spl.net.api.Messages.Message;

public class RegisterOrLogin extends Message {

    private String userName;
    private String password;

    public RegisterOrLogin(short opcode) {
        super(opcode,0);
        userName = null;
        password = null;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
