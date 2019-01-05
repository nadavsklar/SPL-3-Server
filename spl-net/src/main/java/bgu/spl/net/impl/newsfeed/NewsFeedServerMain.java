package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

import java.util.function.Supplier;

public class NewsFeedServerMain {

    public static void main(String[] args) {
        //NewsFeed feed = new NewsFeed(); //one shared object

        Supplier<BidiMessagingProtocol<Message>> protSupplier = new Supplier<BidiMessagingProtocol<Message>>() {
            @Override
            public BidiMessagingProtocol<Message> get() {
                return new BidiMessagingProtocolImpl();
            }
        };
        Supplier<MessageEncoderDecoder<Message>> medSupplier = new Supplier<MessageEncoderDecoder<Message>>() {
            @Override
            public MessageEncoderDecoder<Message> get() {
                return new MessageEncoderDecoderImpl();
            }
        };
        Server<Message> Server = bgu.spl.net.srv.Server.reactor(10,7777, protSupplier, medSupplier);
        Server.serve();

// you can use any server... 
        /*Server.threadPerClient(
                7777, //port
                () -> new BidiMessagingProtocolImpl(), //protocol factory
                MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();*/

        /*Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
                ObjectEncoderDecoder::new //message encoder decoder factory
        ).serve();*/

    }
}
