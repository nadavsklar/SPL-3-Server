package bgu.spl.net.api.Messages.ClientToServer;

public class Login extends RegisterOrLogin {

    public Login(){
        super((short) 2);
    }

    @Override
    public String messageString() { return "Login"; }
}