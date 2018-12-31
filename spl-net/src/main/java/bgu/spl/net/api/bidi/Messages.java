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
    private static Vector<PM> pmMessages = new Vector<>();

    public static void addPost(Post message, String userName) {
        postMessages.put(message, userName);
    }

    public static void addPM(PM message) {
        pmMessages.add(message);
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
}
