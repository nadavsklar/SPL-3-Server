package bgu.spl.net.api.Messages;

public class Register extends Message {

    private String userName;
    private String password;
    public Register() {super((short) 1,0);}
}
