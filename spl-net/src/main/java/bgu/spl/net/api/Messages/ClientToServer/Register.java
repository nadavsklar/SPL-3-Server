package bgu.spl.net.api.Messages.ClientToServer;

public class Register extends RegisterOrLogin {

    public Register(){
        super((short) 1);
    }

    @Override
    public String messageString() { return "Register"; }

    @Override
    public String toString() { return "Register = Username: " + this.getUserName() + ", Password: " + this.getPassword(); }
}
