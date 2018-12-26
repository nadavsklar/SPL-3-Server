package bgu.spl.net.api;

import bgu.spl.net.api.Messages.Register;

import java.util.LinkedList;
import java.util.List;

public class MessageEncoderDecoderImpl<Message> implements MessageEncoderDecoder<Message> {

    private List<Byte> bytes;
    private String encoding;
    private int bytesReaded;
    private short typeOfMessage;
    private Message currentMessage;

    public MessageEncoderDecoderImpl(){
        bytes = new LinkedList<>();
        encoding = "utf-8";
        bytesReaded = 0;
        typeOfMessage = -1;
        currentMessage = null;
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (typeOfMessage == -1) {
            bytesReaded++;
            bytes.add(nextByte);
            if (bytesReaded == 2) {
                typeOfMessage = bytesToShort((Byte[]) bytes.toArray());
                switch(typeOfMessage){
                    case 1:
                        Register Message = new Register();
                }
            }
        }
        else {
            switch (typeOfMessage){
                case 1:
                    //Register
                    break;
                case 2:
                    //Login
                    break;
                case 3:
                    //Logout
                    break;
                case 4:
                    //Follow
                    break;
                case 5:
                    //Post
                    break;
                case 6:
                    //PM
                    break;
                case 7:
                    //User
                    break;
                case 8:
                    //Stat
                    break;
                case 9:
                    //Notification
                    break;
                case 10:
                    //ACK
                    break;
                case 11:
                    //Error
                    break;
                default:
                    //Execption
                    break;
            }
        }
        return null;
    }

    private Message registerRead(byte nextByte){
        return null;
    }

    public short bytesToShort(Byte[] byteArr) {
        short result = (short)((byteArr[0].byteValue() & 0xff) << 8);
        result += (short)(byteArr[1].byteValue() & 0xff);
        return result;
    }

    @Override
    public byte[] encode(Message message) {
        return new byte[0];
    }
}
