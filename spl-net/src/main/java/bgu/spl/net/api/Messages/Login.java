package bgu.spl.net.api.Messages;

public class Login extends Message {

    private String userName;
    private String password;

    public Login() {super((short) 2,0);}

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
