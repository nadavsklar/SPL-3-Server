package bgu.spl.net.api.bidi;


import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ConnectionsImpl<T> implements Connections<T>{

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionsPerClient;
    private Supplier<MessagingProtocol<T>> messagingProtocolSupplier;
    private Supplier<MessageEncoderDecoder<T>> messageEncoderDecoderSupplier;


    public ConnectionsImpl(Supplier<MessageEncoderDecoder<T>> messageEncoderDecoderSupplier, Supplier<MessagingProtocol<T>> messagingProtocolSupplier){
        this.connectionsPerClient = new ConcurrentHashMap<>();
        this.messageEncoderDecoderSupplier = messageEncoderDecoderSupplier;
        this.messagingProtocolSupplier = messagingProtocolSupplier;
    }

    public void addClientConnection(int connectionId){
        if(connectionsPerClient.containsKey(connectionId)){
            //Return Error
        }
        else {
            connectionsPerClient.put(connectionId, new BlockingConnectionHandler<T>(new Socket(), messageEncoderDecoderSupplier.get(), messagingProtocolSupplier.get()));
        }
    }

    @Override
    public boolean send(int connectionId, T msg) {
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

