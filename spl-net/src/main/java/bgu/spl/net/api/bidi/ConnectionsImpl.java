package bgu.spl.net.api.bidi;
import bgu.spl.net.srv.bidi.ConnectionHandler;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T>{

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionsPerClient;



    public ConnectionsImpl(){
        this.connectionsPerClient = new ConcurrentHashMap<>();
    }

    public void addClientConnection(int connectionId, ConnectionHandler<T> handler){
        if(connectionsPerClient.containsKey(connectionId)){
            //Return Error
        }
        else {
            connectionsPerClient.put(connectionId, handler);

        }
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if (!connectionsPerClient.containsKey(connectionId))
            return false;
        connectionsPerClient.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for (Integer ConnectionId: connectionsPerClient.keySet())
            connectionsPerClient.get(ConnectionId).send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        connectionsPerClient.remove(connectionId);
    }
}

