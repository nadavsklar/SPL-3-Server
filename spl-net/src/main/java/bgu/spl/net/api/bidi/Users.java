package bgu.spl.net.api.bidi;

import javafx.util.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Users {

    private static ConcurrentHashMap<Integer, User> usersConnections = new ConcurrentHashMap<>();
    private static Vector<User> registeredUsers = new Vector<>();
    private static Vector<User> connectedUsers = new Vector<>();

    public static void connect(Integer connectionId){
        User user = new User(false, "", "");
        usersConnections.put(connectionId, user);
    }

    public static void register(String userName, String password){
        User user = new User(true, userName, password);
        registeredUsers.add(user);
    }

    public static void login(Integer connectionId, String userName, String password){
        User user = usersConnections.get(connectionId);
        user.setConnected(true);
        user.setUserName(userName);
        user.setPassWord(password);
        connectedUsers.add(user);
    }

    public static void logout(Integer connectionId, User user) {
        connectedUsers.remove(user);
        usersConnections.remove(connectionId);
    }

    public static Integer getConnectionIdByUser(String userName) {
        Iterator it = usersConnections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (((User)pair.getValue()).getUserName().equals(userName))
                return (Integer) pair.getKey();
        }
        return -1;
    }

    public static User getUser(Integer connectionId){
        return usersConnections.get(connectionId);
    }

    public static User getUser(String userName){
        for (int i = 0; i < registeredUsers.size(); i++) {
            if (registeredUsers.get(i).getUserName().equals(userName))
                return registeredUsers.get(i);
        }
        return null;
    }

    public static boolean isRegistered(String userName){
        for (int i = 0; i < registeredUsers.size(); i++)
            if (registeredUsers.get(i).getUserName().equals(userName))
                return true;
        return false;
    }

    public static boolean isLoggedIn(String userName){
        for (int i = 0; i < connectedUsers.size(); i++)
            if (connectedUsers.get(i).getUserName().equals(userName))
                return true;
        return false;
    }

    public static boolean isPasswordMatched(String userName, String password) {
        for (int i = 0; i < registeredUsers.size(); i++) {
            if (registeredUsers.get(i).getUserName().equals(userName)) {
                if (registeredUsers.get(i).getPassWord().equals(password))
                    return true;
            }
        }
        return false;
    }

    public static Vector<User> getRegisteredUsers() { return registeredUsers; }

}
