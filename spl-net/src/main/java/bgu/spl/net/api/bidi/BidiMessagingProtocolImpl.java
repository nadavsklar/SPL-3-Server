package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.ClientToServer.*;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerToClient.*;
import bgu.spl.net.api.Messages.ServerToClient.Error;
import com.sun.net.httpserver.Authenticator;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate = false;
    private Connections connections = null;
    private int connectionId = -1;

    @Override
    public void start(int connectionId, Connections connections) {

        this.connections = connections;
        this.connectionId = connectionId;
        Users.connect(connectionId);
    }

    @Override
    public void process(Message message) {
        String typeOfMessage = message.messageString();
        switch (typeOfMessage) {
            case "Logout":
                doLogout();
                break;
            case "Login":
                doLogin((Login)message);
                break;
            case "Register":
                doRegister((Register)message);
                break;
            case "Follow":
                doFollowOrUnfollow((Follow)message);
                break;
            case "PM":
                doPM((PM)message);
                break;
            case "Post":
                doPost((Post)message);
                break;
            case "Stat":
                doStat((Stat)message);
                break;
            case "UserList":
                doUserList();
                break;
        }
    }

    private void doLogout() {
        User user = Users.getUser(connectionId);
        if(user == null || !Users.isLoggedIn(user.getUserName())){
            //Error
            Error errorMessage = new Error((short)3);
            connections.send(connectionId, errorMessage);
        }
        else {
            //Success
            ACK ackMessage = new ACK((short)3);
            Users.logout(connectionId, user);
            connections.send(connectionId, ackMessage);
            shouldTerminate = true;
        }
    }

    private void doLogin(Login message) {
        String userNameTyped = message.getUserName();
        String passwordTyped = message.getPassword();

        if(!Users.isRegistered(userNameTyped) || Users.isLoggedIn(userNameTyped) || !Users.isPasswordMatched(userNameTyped, passwordTyped)){
            //Error
            Error errorMessage = new Error((short)2);
            connections.send(connectionId, errorMessage);
        }
        else{
            //Success
            ACK ackMessage = new ACK((short)2);
            Users.login(connectionId, userNameTyped, passwordTyped);
            connections.send(connectionId, ackMessage);

            User currentUser = Users.getUser(connectionId);
            Vector<Post> awaitingPosts = currentUser.getPostMessagesAwaiting();
            Vector<PM> awaitingPMs = currentUser.getPmMessagesAwaiting();

            for (int i = 0; i < awaitingPosts.size(); i++) {
                Post currentPost = awaitingPosts.get(i);
                String sender = Messages.getSender(currentPost);
                String content = currentPost.getContent();
                Notification notificationMessage = new Notification();
                notificationMessage.setContetnt(content);
                notificationMessage.setPostingUser(sender);
                notificationMessage.setPublicOrPrivate('1');
                connections.send(connectionId, notificationMessage);
            }

            for (int i = 0; i < awaitingPMs.size(); i++) {
                PM currentPM = awaitingPMs.get(i);
                String sender = currentPM.getUserName();
                String content = currentPM.getContent();
                Notification notificationMessage = new Notification();
                notificationMessage.setContetnt(content);
                notificationMessage.setPostingUser(sender);
                notificationMessage.setPublicOrPrivate('0');
                connections.send(connectionId, notificationMessage);
            }
        }

    }

    private void doRegister(Register message){
        Pair<String, String> futureUser = new Pair(message.getUserName(), message.getPassword());
        String userNameTyped = futureUser.getKey();
        String passwordTyped = futureUser.getValue();

        if(Users.isRegistered(userNameTyped)){
            //Error
            Error errorMessage = new Error((short)1);
            connections.send(connectionId, errorMessage);
        }
        else{
            //Success
            ACK ackMessage = new ACK((short)1);
            Users.register(userNameTyped, passwordTyped);
            connections.send(connectionId, ackMessage);
        }
    }

    private void doFollowOrUnfollow(Follow message){
        User user = Users.getUser(connectionId);
        if (user == null || !Users.isLoggedIn(user.getUserName())) {
            Error errorMessage = new Error((short)4);
            connections.send(connectionId, errorMessage);
        }
        else {
            // Success
            List<String> userNameList = message.getUserNameList();
            byte followOrUnfollow = message.getFollowOrUnfollow();
            Vector<User> succeedVector;
            if (followOrUnfollow == 0)
                succeedVector = doFollow(user, userNameList);
            else
                succeedVector = doUnfollow(user, userNameList);

            List<String> userNames = new LinkedList<>();
            for (int i = 0; i < succeedVector.size(); i++)
                userNames.add(succeedVector.get(i).getUserName());

            FollowACK ackMessage = new FollowACK();
            ackMessage.setNumOfUsers((short)userNames.size());
            ackMessage.setUserNameList(userNames);
            connections.send(connectionId, ackMessage);
        }
    }

    private Vector<User> doFollow(User user, List<String> toFollow) {
        Vector<User> succeedFollows = new Vector<>();
        for (int i = 0; i < toFollow.size(); i++) {
            User currentUserToFollow = Users.getUser(toFollow.get(i));
            if (currentUserToFollow != null) {
                user.follow(currentUserToFollow);
                succeedFollows.add(currentUserToFollow);
            }
        }
        return succeedFollows;
    }

    private Vector<User> doUnfollow(User user, List<String> toUnfollow) {
        Vector<User> succeedUnfollows = new Vector<>();
        for (int i = 0; i < toUnfollow.size(); i++) {
            User currentUserToUnfollow = Users.getUser(toUnfollow.get(i));
            if (currentUserToUnfollow != null) {
                user.unfollow(currentUserToUnfollow);
                succeedUnfollows.add(currentUserToUnfollow);
            }
        }
        return succeedUnfollows;
    }

    private void doPM(PM message) {
        String userNameToSend = message.getUserName();
        String content = message.getContent();
        User currentUser = Users.getUser(connectionId);
        if (currentUser == null || !Users.isLoggedIn(currentUser.getUserName()) || !Users.isRegistered(userNameToSend)) {
            //Error
            Error errorMessage = new Error((short)6);
            connections.send(connectionId, errorMessage);
        }
        else {
            int otherConnectionId = Users.getConnectionIdByUser(userNameToSend);
            if (otherConnectionId != -1 && Users.isLoggedIn(userNameToSend)) { // connected
                Notification notificationMessage = new Notification();
                notificationMessage.setPublicOrPrivate('0');
                notificationMessage.setPostingUser(currentUser.getUserName());
                notificationMessage.setContetnt(content);
                Messages.addPM(message);
                connections.send(otherConnectionId, notificationMessage);
            }
            else { // off
                User otherUser = Users.getUser(userNameToSend);
                otherUser.addAwaitingPM(message);
                Messages.addPM(message);
            }
        }
    }

    private void doPost(Post message) {
        // TODO
    }

    private void doStat(Stat message) {
        User currentUser = Users.getUser(connectionId);
        String userNameToStat = message.getUserName();
        if (currentUser == null || !Users.isLoggedIn(currentUser.getUserName()) || !Users.isRegistered(userNameToStat)) {
            //Error
            Error errorMessage = new Error((short)8);
            connections.send(connectionId, errorMessage);
        }
        else {
            short numOfPosts = Messages.getNumOfPosts(userNameToStat);
            User otherUser = Users.getUser(userNameToStat);
            short numOfFollowers = (short)otherUser.getFollowers().size();
            short numOfFollowing = (short)otherUser.getFollowing().size();
            StatACK ackMessage = new StatACK();
            ackMessage.setNumOfPosts(numOfPosts);
            ackMessage.setNumOfFollowers(numOfFollowers);
            ackMessage.setNumOfFollowing(numOfFollowing);
            connections.send(connectionId, ackMessage);
        }
    }

    private void doUserList() {
        User currentUser = Users.getUser(connectionId);
        if (currentUser == null || !Users.isLoggedIn(currentUser.getUserName())) {
            //Error
            Error errorMessage = new Error((short)7);
            connections.send(connectionId, errorMessage);
        }
        else {
            Vector<User> registeredUsers = Users.getRegisteredUsers();
            List<String> userNameList = new LinkedList<>();
            for (int i = 0; i < registeredUsers.size(); i++)
                userNameList.add(registeredUsers.get(i).getUserName());
            UserListACK ackMessage = new UserListACK();
            ackMessage.setNumOfUsers((short)userNameList.size());
            ackMessage.setUserNameList(userNameList);
            connections.send(connectionId, ackMessage);
        }
    }


    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }


}
