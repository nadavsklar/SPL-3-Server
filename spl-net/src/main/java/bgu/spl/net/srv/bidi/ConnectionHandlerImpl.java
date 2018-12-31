package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandlerImpl implements ConnectionHandler<Message>, Runnable {

    private Socket socket;
    private BidiMessagingProtocolImpl protocol;
    private MessageEncoderDecoderImpl med;
    private volatile boolean connected = true;

    public ConnectionHandlerImpl(Socket socket, BidiMessagingProtocolImpl protocol, MessageEncoderDecoderImpl med) {
        this.socket = socket;
        this.protocol = protocol;
        this.med = med;
    }


    @Override
    public void send(Message msg) {
        try {
            socket.getOutputStream().write(med.encode(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        connected = false;
        socket.close();
    }

    @Override
    public void run() {
        try {
            byte nextByte = 0;
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            nextByte = (byte) in.read();
            while(!protocol.shouldTerminate() && connected && !(nextByte != -1)) {
                Message request = med.decodeNextByte(nextByte);
                if(request != null && request.isReaded())
                    protocol.process(request);
                nextByte = (byte) in.read();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
