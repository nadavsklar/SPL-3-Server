package bgu.spl.net.api.Messages.ClientToServer;

import bgu.spl.net.api.Messages.Message;

import java.util.LinkedList;
import java.util.List;

public class Follow extends Message {

    private byte followOrUnfollow; // 0 - follow, 1 - unfollow
    private short numOfUsers;
    private short currentNumOfUsers;
    private List<String> userNameList;

    public Follow() {
        super((short) 4,0);
        numOfUsers = 0;
        currentNumOfUsers = 0;
        userNameList = new LinkedList<>();
        followOrUnfollow = -1;
    }

    public byte getFollowOrUnfollow() {
        return followOrUnfollow;
    }

    public void setFollowOrUnfollow(byte followOrUnfollow) {
        this.followOrUnfollow = followOrUnfollow;
    }

    public short getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(short numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public short getCurrentNumOfUsers() { return currentNumOfUsers; }

    public void increaseNumOfUsers() { this.currentNumOfUsers++; }

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void addUser(String userName) {
        this.userNameList.add(userName);
    }

    @Override
    public String messageString() { return "Follow"; }


    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }
}
