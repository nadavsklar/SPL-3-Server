package bgu.spl.net.api.Messages;

public class Message {

    protected short opcode;
    protected int length;
    private boolean isReaded;

    public Message(short opcode, int length){
        this.opcode = opcode;
        this.length = length;
        isReaded = false;
    }

    public short getOpcode() {
        return opcode;
    }

    public void setOpcode(short opcode) {
        this.opcode = opcode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded() {
        isReaded = true;
    }

    public String messageString() { return ""; }
}
