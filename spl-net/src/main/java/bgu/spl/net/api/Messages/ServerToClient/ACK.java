package bgu.spl.net.api.Messages.ServerToClient;

public class ACK extends ACKOrError {

    public ACK(short typeOfMessage) {
        super((short)10, typeOfMessage);
    }

    @Override
    public String messageString() {return "ACK";}

    @Override
    public String toString() {return "ACK";}


}
