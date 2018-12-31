package bgu.spl.net.api.Messages.ServerToClient;

import bgu.spl.net.api.Messages.Message;

public abstract class ACKOrError extends Message {

    protected final short typeOfMessage;

    public ACKOrError(short opcode, short typeOfMessage) {
        super(opcode,0);
        this.typeOfMessage = typeOfMessage;
    }

    public short getTypeOfMessage() {
        return typeOfMessage;
    }

}
