package bgu.spl.net.api.bidi;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private boolean shouldTerminate = false;

    @Override
    public void start(int connectionId, Connections connections) {
        ((ConnectionsImpl)connections).addClientConnection(connectionId);
    }

    @Override
    public void process(T message) {

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
