package bgu.spl.net.api.bidi;

import javafx.util.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Users {

    public static ConcurrentHashMap<Integer, User> usersConnections = new ConcurrentHashMap<>();
    public static Vector<User> registeredUsers = new Vector<>();
    public static Vector<User> connectedUsers = new Vector<>();

    public static void connect(Integer connectionId){
        printData();
        User user = new User(false, "", "");
        usersConnections.put(connectionId, user);
    }

    public static boolean register(String userName, String password){
        synchronized (registeredUsers) {
            printData();
            if (!isRegistered(userName)) {
                User user = new User(true, userName, password);
                registeredUsers.add(user);
                return true;
            }
            return false;
        }
    }

    public static boolean login(Integer connectionId, String userName, String password){
        printData();
        if(usersConnections.containsKey(connectionId) && isRegistered(userName) && !isLoggedIn(userName) && isPasswordMatched(userName, password)) {
            User user = usersConnections.get(connectionId);
            user.setConnected(true);
            user.setUserName(userName);
            user.setPassWord(password);
            usersConnections.replace(connectionId, user);
            connectedUsers.add(user);
            return true;
        }
        return false;
    }

    public static boolean logout(Integer connectionId, User user) {
        clearSpamUsers();
        printData();
        if(user != null && user.isConnected() && isLoggedIn(user.getUserName())){
            connectedUsers.remove(user);
            usersConnections.remove(connectionId);
            return true;
        }
        return false;
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
        synchronized (usersConnections) {
            if (usersConnections.containsKey(connectionId))
                return usersConnections.get(connectionId);
            return null;
        }
    }

    public static User getUser(String userName){
        synchronized (registeredUsers) {
            for (int i = 0; i < registeredUsers.size(); i++) {
                if (registeredUsers.get(i).getUserName().equals(userName))
                    return registeredUsers.get(i);
            }
            return null;
        }
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

    public static void printData(){
        System.out.println("Users in the System: ");
        for(int i = 0; i < registeredUsers.size(); i++){
            User currentUser = registeredUsers.get(i);
            System.out.println("Name = " + currentUser.getUserName() + ", Password = " + currentUser.getPassWord());
        }

        System.out.println("Users connected: ");
        Iterator it = usersConnections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("Connection Id = " + pair.getKey()+ ", Name = " + ((User)pair.getValue()).getUserName() + ", Password = " + ((User)pair.getValue()).getPassWord());
        }
    }

    public static void clearSpamUsers(){
        Iterator it = usersConnections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(((User)pair.getValue()).getUserName() == "" && (((User) pair.getValue()).getPassWord() == ""))
                usersConnections.remove(pair.getKey());
        }
    }

    public static Vector<User> getRegisteredUsers() { return registeredUsers; }

}
