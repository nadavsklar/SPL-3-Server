package bgu.spl.net.api.Messages.ClientToServer;

import bgu.spl.net.api.Messages.Message;

public class PM extends Message {

    private String userName;
    private String content;

    public PM() {
        super((short) 6,0);
        userName = content = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String messageString() { return "PM"; }
}
