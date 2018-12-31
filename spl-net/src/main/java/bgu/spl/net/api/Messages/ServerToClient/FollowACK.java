package bgu.spl.net.api.Messages.ServerToClient;

import java.util.LinkedList;
import java.util.List;

public class FollowACK extends ACK {

    private short numOfUsers;
    private List<String> userNameList;

    public FollowACK(){
        super((short)4);
        numOfUsers = 0;
        userNameList = new LinkedList<>();
    }

    public short getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(short numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void addUserName(String userName) {
        this.userNameList.add(userName);
    }

    @Override
    public String messageString() {return "FollowACK";}
}
