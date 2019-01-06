package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.ClientToServer.PM;
import bgu.spl.net.api.Messages.ClientToServer.Post;
import bgu.spl.net.api.Messages.Message;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Messages {

    private static ConcurrentHashMap<Post, String> postMessages = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<PM, String> pmMessages = new ConcurrentHashMap<>();

    public static void addPost(Post message, String postingUser) {
        postMessages.put(message, postingUser);
    }

    public static void addPM(PM message, String postingUser) {
        pmMessages.put(message, postingUser);
    }

    public static short getNumOfPosts(String userName) {
        short result = 0;
        Iterator it = postMessages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if ((pair.getValue()).equals(userName))
                result++;
        }
        return result;
    }

    public static String getSender(Post message) { return postMessages.get(message); }

    public static String getSender(PM message) { return  pmMessages.get(message); }

    public static void printData(){
        System.out.println("Post Messages: ");
        Iterator it1 = postMessages.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry)it1.next();
            System.out.println("Content = " + ((Post)pair.getKey()).getContent() + ", Sender = " + pair.getValue());
        }

        System.out.println("PM Messages: ");
        Iterator it2 = pmMessages.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry)it2.next();
            System.out.println("Content = " + ((PM)pair.getKey()).getContent() + ", Sender = " + pair.getValue());
        }

    }



}
