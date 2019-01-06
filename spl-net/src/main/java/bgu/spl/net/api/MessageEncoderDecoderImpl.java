package bgu.spl.net.api;

import bgu.spl.net.api.Messages.*;
import bgu.spl.net.api.Messages.ClientToServer.*;
import bgu.spl.net.api.Messages.ServerToClient.*;
import bgu.spl.net.api.Messages.ServerToClient.Error;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {

    private List<Byte> bytes;
    private final String encoding = "utf-8";
    private int bytesReaded;
    private short typeOfMessage;
    private Message currentMessage;

    public MessageEncoderDecoderImpl(){
        bytes = new LinkedList<>();
        bytesReaded = 0;
        typeOfMessage = -1;
        currentMessage = null;
    }

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (typeOfMessage == -1) {
            currentMessage = null;
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
                        logoutRead((Logout)currentMessage);
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
                        userListRead((UserList)currentMessage);
                        break;
                    case 8:
                        currentMessage = new Stat();
                        break;
                    default:
                        break;
                }
            }
        }
        else {
            switch (typeOfMessage){
                case 1:
                    registerOrLoginRead(nextByte, (Register)currentMessage);
                    break;
                case 2:
                    registerOrLoginRead(nextByte, (Login)currentMessage);
                    break;
                case 3:
                    logoutRead((Logout)currentMessage);
                    break;
                case 4:
                    followRead(nextByte, (Follow)currentMessage);
                    break;
                case 5:
                    postRead(nextByte, (Post)currentMessage);
                    break;
                case 6:
                    pmRead(nextByte, (PM)currentMessage);
                    break;
                case 7:
                    userListRead((UserList)currentMessage);
                    break;
                case 8:
                    statRead(nextByte, (Stat)currentMessage);
                    break;
                default:
                    break;
            }
        }
        if(currentMessage != null && currentMessage.isReaded()){
            typeOfMessage = -1;
            bytesReaded = 0;
            return currentMessage;
        }
        else
            return null;
    }


    private void registerOrLoginRead(byte nextByte, RegisterOrLogin message){
        if(message.getUserName() == null) {
            if(nextByte == '\0') {
                byte[] tmpBytes = getBytesArray();
                message.setUserName(new String(tmpBytes));
            }
            else
                bytes.add(nextByte);
        }
        else{
            if(message.getPassword() == null){
                if(nextByte == '\0'){
                    byte[] tmpBytes = getBytesArray();
                    message.setPassword(new String(tmpBytes));
                    message.setReaded();
                }
                else
                    bytes.add(nextByte);
            }
        }
    }


    private void followRead(byte nextByte, Follow message) {
        if (message.getFollowOrUnfollow() == -1)
            message.setFollowOrUnfollow(nextByte);
        else {
            if (message.getNumOfUsers() == 0) {
                if (bytes.size() == 2) {
                    byte[] tmpBytes = getBytesArray();
                    message.setNumOfUsers(bytesToShort(tmpBytes));
                }
                else
                    bytes.add(nextByte);
            }
            else {
                if (nextByte == '\0') {
                    byte[] tmpBytes = getBytesArray();
                    message.addUser(new String(tmpBytes));
                    message.increaseNumOfUsers();
                    if(message.getNumOfUsers() == message.getCurrentNumOfUsers())
                        message.setReaded();
                    bytes.clear();
                }
                else {
                    bytes.add(nextByte);
                }
            }
        }
    }

    private void logoutRead(Logout message){ message.setReaded(); }

    private void userListRead(UserList message){
        message.setReaded();
    }

    private void postRead(byte nextByte, Post message){
        if(nextByte != '\0') {
            bytes.add(nextByte);
        }
        else {
            byte[] tmpBytes = getBytesArray();
            message.setContent(new String(tmpBytes));
            message.setReaded();
        }
    }

    private void pmRead(byte nextByte, PM message){
        if(message.getUserName() == null) {
            if (nextByte != '\0')
                bytes.add(nextByte);
            else {
                byte[] tmpBytes = getBytesArray();
                message.setUserName(new String(tmpBytes));
            }
        }
        else {
            if (nextByte != '\0')
                bytes.add(nextByte);
            else {
                byte[] tmpBytes = getBytesArray();
                message.setContent(new String(tmpBytes));
                message.setReaded();
            }
        }
    }

    private void statRead(byte nextByte, Stat message){
        if(nextByte != '\0')
            bytes.add(nextByte);
        else {
            byte[] tmpBytes = getBytesArray();
            message.setUserName(new String(tmpBytes));
            message.setReaded();
        }
    }


    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public byte[] encode(Message message) {
        String typeOfMessage = message.messageString();
        switch (typeOfMessage){
            case "ACK":
                return encodeACKOrError((ACK)message);
            case "Error":
                return encodeACKOrError((Error)message);
            case "FollowACK":
                return encodeFollowACK((FollowACK)message);
            case "Notification":
                return encodeNotification((Notification)message);
            case "StatACK":
                return encodeStatACK((StatACK)message);
            case "UserListACK":
                return encodeUserListACK((UserListACK)message);
            default:
                return null;
        }
    }

    private byte[] encodeACKOrError(ACKOrError message) {
        byte[] tmpBytes1 = shortToBytes(message.getOpcode());
        byte[] tmpBytes2 = shortToBytes(message.getTypeOfMessage());
        byte[] resultBytes = new byte[tmpBytes1.length + tmpBytes2.length];
        int index = 0;
        for(int i = 0; i < tmpBytes1.length; i++){
            resultBytes[index] = tmpBytes1[i];
            index++;
        }
        for(int i = 0; i < tmpBytes2.length; i++){
            resultBytes[index] = tmpBytes2[i];
            index++;
        }
        return resultBytes;
    }

    private byte[] encodeFollowACK(FollowACK message) {
        byte[] firstPart = encodeACKOrError(message);
        byte[] numOfUsers = shortToBytes(message.getNumOfUsers());
        byte[] userNameList = null;
        String userNameListString = "";
        int index = 0;
        //Init
        List<String> users = message.getUserNameList();
        for (int i = 0; i < users.size(); i++)
            userNameListString = users.get(i) + '\0';
        try {
            userNameList = userNameListString.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] result = new byte[firstPart.length + numOfUsers.length + userNameList.length];
        for (int i = 0; i < firstPart.length; i++) {
            result[index] = firstPart[i];
            index++;
        }
        for (int i = 0; i < numOfUsers.length; i++) {
            result[index] = numOfUsers[i];
            index++;
        }
        for (int i = 0; i < userNameList.length; i++) {
            result[index] = userNameList[i];
            index++;
        }
        return result;
    }

    private byte[] encodeNotification(Notification message) {
        byte[] opcode = shortToBytes(message.getOpcode());
        byte[] postingUser = null, content = null;
        try {
           postingUser = message.getPostingUser().getBytes(encoding);
           content = message.getContent().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] result = new byte[opcode.length + postingUser.length + content.length + 3];
        int index = 0;
        for (int i = 0; i < opcode.length; i++) {
            result[index] = opcode[i];
            index++;
        }
        result[index] = (byte)message.getPublicOrPrivate();
        index++;
        for (int i = 0; i < postingUser.length; i++) {
            result[index] = postingUser[i];
            index++;
        }
        result[index] = '\0';
        index++;
        for (int i = 0; i < content.length; i++) {
            result[index] = content[i];
            index++;
        }
        result[index] = '\0';
        return result;
    }

    private byte[] encodeStatACK(StatACK message) {
        byte[] firstPart = encodeACKOrError(message);
        byte[] numOfPosts = shortToBytes(message.getNumOfPosts());
        byte[] numOfFollowers = shortToBytes(message.getNumOfFollowers());
        byte[] numOfFollowing = shortToBytes(message.getNumOfFollowing());
        byte[] result = new byte[firstPart.length + numOfPosts.length + numOfFollowers.length + numOfFollowing.length];
        int index = 0;
        int i;
        for (i = 0; i < firstPart.length; i++) {
            result[index] = firstPart[i];
            index++;
        }
        for (i = 0; i < numOfPosts.length; i++) {
            result[index] = numOfPosts[i];
            index++;
        }
        for (i = 0; i < numOfFollowers.length; i++) {
            result[index] = numOfFollowers[i];
            index++;
        }
        for (i = 0; i < numOfFollowing.length; i++) {
            result[index] = numOfFollowing[i];
            index++;
        }
        return result;
    }

    private byte[] encodeUserListACK(UserListACK message) {
        byte[] firstPart = encodeACKOrError(message);
        byte[] numOfUsers = shortToBytes(message.getNumOfUsers());
        byte[] userNameList = null;
        String userNameListString = "";
        List<String> users = message.getUserNameList();
        for (int i = 0; i < users.size(); i++)
            userNameListString += users.get(i) + '\0';
        try {
            userNameList = userNameListString.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] result = new byte[firstPart.length + numOfUsers.length + userNameList.length];
        int index = 0;
        for (int i = 0; i < firstPart.length; i++) {
            result[index] = firstPart[i];
            index++;
        }
        for (int i = 0; i < numOfUsers.length; i++) {
            result[index] = numOfUsers[i];
            index++;
        }
        for (int i = 0; i < userNameList.length; i++) {
            result[index] = userNameList[i];
            index++;
        }
        return result;
    }

    private byte[] getBytesArray(){
        byte[] tmpBytes = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++)
            tmpBytes[i] = bytes.get(i).byteValue();
        bytes.clear();
        return tmpBytes;
    }

}
