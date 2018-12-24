package bgu.spl.net.api.bidi;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private boolean shouldTerminate = false;
    private Connections connections = null;
    private int connectionId = -1;

    @Override
    public void start(int connectionId, Connections connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(T message) {

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
