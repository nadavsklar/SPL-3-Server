package bgu.spl.net.api.Messages;

public class Register extends Message {

    private String userName;
    private String password;

    public Register() {
        super((short) 1,0);
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
