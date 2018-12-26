package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.Register;
import bgu.spl.net.srv.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        //Trying to commit
        MessageEncoderDecoderImpl x = new MessageEncoderDecoderImpl();
        String userName = "Tomer";
        String password = "1234";
        byte[] bytes;
        byte[] userNameBytes = userName.getBytes("utf-8");
        byte[] passwordBytes = password.getBytes("utf-8");
        bytes = new byte[userNameBytes.length + passwordBytes.length + 4];
        bytes[0] = 0;
        bytes[1] = 1;
        int index = 2;
        for(int i = 0; i < userNameBytes.length; i++) {
            bytes[index] = userNameBytes[i];
            index++;
        }
        bytes[index] = '\0';
        index++;
        for(int i = 0; i < passwordBytes.length; i++) {
            bytes[index] = passwordBytes[i];
            index++;
        }
        bytes[index] = '\0';
        for(int i = 0; i < bytes.length; i++)
        {
            Message m = x.decodeNextByte(bytes[i]);
            if(m!= null && m instanceof Register){
                Register r = (Register) m;
                if(r.getUserName() != null & r.getPassword() != null) {
                    System.out.println(r.getUserName());
                    System.out.println(r.getPassword());
                }
            }
        }


        if (args.length == 0) {
            args = new String[]{"localhost", "hello"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

            System.out.println("sending message to server");
            out.write(args[1]);
            out.newLine();
            out.flush();

            System.out.println("awaiting response");
            String line = in.readLine();
            System.out.println("message from server: " + line);
        }
    }
}
