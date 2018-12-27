package bgu.spl.net.api;

import bgu.spl.net.api.Messages.*;
import bgu.spl.net.api.Messages.Error;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {

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
                byte[] tmpBytes = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); i++)
                    tmpBytes[i] = bytes.get(i).byteValue();
                typeOfMessage = bytesToShort(tmpBytes);
                bytes.clear();
                switch(typeOfMessage){
                    case 1:
                        currentMessage = new Register();
                        break;
                    case 2:
                        currentMessage = new Login();
                        break;
                    case 3:
                        currentMessage = new Logout();
                        break;
                    case 4:
                        currentMessage = new Follow();
                        break;
                    case 5:
                        currentMessage = new Post();
                        break;
                    case 6:
                        currentMessage = new PM();
                        break;
                    case 7:
                        currentMessage = new UserList();
                        break;
                    case 8:
                        currentMessage = new Stat();
                        break;
                    case 9:
                        currentMessage = new Notification();
                        break;
                    case 10:
                        currentMessage = new ACK();
                        break;
                    case 11:
                        currentMessage = new Error();
                        break;
                    default:
                        //Error
                }
            }
        }
        else {
            switch (typeOfMessage){
                case 1:
                    registerRead(nextByte, (Register)currentMessage);
                    break;
                case 2:
                    loginRead(nextByte, (Login)currentMessage);
                    break;
                case 3:
                    break;
                case 4:
                    followRead(nextByte, (Follow)currentMessage);
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
        return currentMessage;
    }


    private Register registerRead(byte nextByte, Register registerMessage){
        Register result = null;
        if(registerMessage.getUserName() == null) {
            if(nextByte == '\0') {
                byte[] tmpBytes = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); i++)
                    tmpBytes[i] = bytes.get(i).byteValue();
                bytes.clear();
                registerMessage.setUserName(new String(tmpBytes));
            }
            else
                bytes.add(nextByte);
        }
        else{
            if(registerMessage.getPassword() == null){
                if(nextByte == '\0'){
                    byte[] tmpBytes = new byte[bytes.size()];
                    for (int i = 0; i < bytes.size(); i++)
                        tmpBytes[i] = bytes.get(i).byteValue();
                    bytes.clear();
                    registerMessage.setPassword(new String(tmpBytes));
                    result = registerMessage;
                }
                else
                    bytes.add(nextByte);
            }
        }
        return result;
    }

    private Login loginRead(byte nextByte, Login loginMessage) {
        Login result = null;
        Register registerMessage = new Register();
        registerMessage.setUserName(loginMessage.getUserName());
        registerMessage.setPassword(loginMessage.getPassword());

        Register newRegisterMessage = registerRead(nextByte, registerMessage);

        if(newRegisterMessage != null){
            result = new Login();
            result.setUserName(newRegisterMessage.getUserName());
            result.setPassword(newRegisterMessage.getPassword());
        }
        return result;
    }

    private Follow followRead(byte nextByte, Follow message) {
        if (message.getFollowOrUnfollow() == -1)
            message.setFollowOrUnfollow(nextByte);
        else {
            if (message.getNumOfUsers() == 0) {
                if (bytes.size() == 2) {
                    byte[] tmpBytes = new byte[bytes.size()];
                    for (int i = 0; i < bytes.size(); i++)
                        tmpBytes[i] = bytes.get(i).byteValue();
                    bytes.clear();
                    message.setNumOfUsers(bytesToShort(tmpBytes));
                }
                else
                    bytes.add(nextByte);
            }
            else {
                if (nextByte == '\0') {
                    byte[] tmpBytes = new byte[bytes.size()];
                    for (int i = 0; i < bytes.size(); i++)
                        tmpBytes[i] = bytes.get(i).byteValue();
                    bytes.clear();
                    message.addUser(new String(tmpBytes));
                }
                else {
                    bytes.add(nextByte);
                }
            }
        }
        return message;
    }


    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    @Override
    public byte[] encode(Message message) {
        return new byte[0];
    }

}
