package bgu.spl.net.api.bidi;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl implements Connections<Message>{

    private ConcurrentHashMap<Integer, ConnectionHandler<Message>> connectionsPerClient;

    public ConnectionsImpl(){
        this.connectionsPerClient = new ConcurrentHashMap<>();
    }


    @Override
    public boolean send(int connectionId, Message msg) {
        if (!connectionsPerClient.containsKey(connectionId))
            return false;
        connectionsPerClient.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(Message msg) {
        for (Integer ConnectionId: connectionsPerClient.keySet())
            connectionsPerClient.get(ConnectionId).send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        connectionsPerClient.remove(connectionId);
    }

}

