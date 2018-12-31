package bgu.spl.net.api.Messages.ServerToClient;

public class Error extends ACKOrError {

    public Error(short typeOfMessage) {
        super((short) 11,typeOfMessage);
    }

    @Override
    public String messageString() {return "Error";}

}
