package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.bidi.ConnectionHandlerImpl;

import java.net.ServerSocket;
import java.util.function.Supplier;

public class ThreadPerClient extends BaseServer<Message> {

    private int port;
    private Supplier<MessageEncoderDecoderImpl> medFact;
    private Supplier<BidiMessagingProtocolImpl> protFact;
    private ServerSocket sock;
    private ConnectionsImpl connections;
    private int currentClientId;

    public ThreadPerClient(int port, Supplier<BidiMessagingProtocol<Message>> protocolFactory, Supplier<MessageEncoderDecoder<Message>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }


    @Override
    protected void execute(ConnectionHandlerImpl handler) {
        new Thread(handler).start();
    }
}
