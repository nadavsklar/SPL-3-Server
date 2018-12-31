package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.ClientToServer.PM;
import bgu.spl.net.api.Messages.ClientToServer.Post;

import java.util.Vector;

public class User {

    private String userName;
    private String passWord;
    private Vector<User> followers;
    private Vector<User> following;
    private Vector<Post> postMessagesAwaiting;
    private Vector<PM> pmMessagesAwaiting;

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.followers = new Vector<>();
        this.following = new Vector<>();
        this.postMessagesAwaiting = new Vector<>();
        this.pmMessagesAwaiting = new Vector<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public Vector<User> getFollowers() {
        return followers;
    }

    public Vector<User> getFollowing() {
        return following;
    }

    public boolean follow(User other) {
        if (!following.contains(other)) {
            following.add(other);
            other.getFollowers().add(this);
            return true;
        }
        return false;
    }

    public boolean unfollow(User other) {
        if (following.contains(other)) {
            following.remove(other);
            other.getFollowers().remove(this);
            return true;
        }
        return false;
    }

    public void addAwaitingPM(PM message) {
        this.pmMessagesAwaiting.add(message);
    }

    public void addAwaitingPost(Post message) {
        this.postMessagesAwaiting.add(message);
    }

    public void removeAwaitingPM(PM message) {
        this.pmMessagesAwaiting.remove(message);
    }

    public void removeAwaitingPost(Post message) {
        this.postMessagesAwaiting.remove(message);
    }

    public Vector<Post> getPostMessagesAwaiting() {
        return postMessagesAwaiting;
    }

    public Vector<PM> getPmMessagesAwaiting() {
        return pmMessagesAwaiting;
    }
}