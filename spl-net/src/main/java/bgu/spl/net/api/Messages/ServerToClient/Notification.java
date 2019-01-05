package bgu.spl.net.api.Messages.ServerToClient;

import bgu.spl.net.api.Messages.Message;

public class Notification extends Message {

    private char publicOrPrivate; // 1 - post, 0 - pm
    private String postingUser;
    private String content;

    public Notification() {
        super((short) 9,0);
        publicOrPrivate = 'B';
        postingUser = content = null;
    }


    public char getPublicOrPrivate() {
        return publicOrPrivate;
    }

    public void setPublicOrPrivate(char publicOrPrivate) {
        this.publicOrPrivate = publicOrPrivate;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public void setPostingUser(String postingUser) {
        this.postingUser = postingUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String messageString() {return "Notification";}
}
