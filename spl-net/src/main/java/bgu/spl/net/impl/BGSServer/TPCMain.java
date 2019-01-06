package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;

import java.util.function.Supplier;

public class TPCMain {
    public static void main(String[] args) {

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
        Server<Message> Server = bgu.spl.net.srv.Server.threadPerClient(Integer.parseInt(args[0]), protSupplier, medSupplier);
        Server.serve();
    }
}
