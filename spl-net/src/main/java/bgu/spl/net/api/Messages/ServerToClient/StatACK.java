package bgu.spl.net.api.Messages.ServerToClient;

public class StatACK extends ACK {

    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;

    public StatACK(){
        super((short)8);
        numOfPosts = numOfFollowers = numOfFollowing = 0;
    }

    public short getNumOfPosts() {
        return numOfPosts;
    }

    public void setNumOfPosts(short numOfPosts) {
        this.numOfPosts = numOfPosts;
    }

    public short getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(short numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public short getNumOfFollowing() {
        return numOfFollowing;
    }

    public void setNumOfFollowing(short numOfFollowing) {
        this.numOfFollowing = numOfFollowing;
    }

    public boolean isPopular(){
        return numOfFollowers >= numOfFollowing;
    }

    @Override
    public String messageString() {return "StatACK";}
}
