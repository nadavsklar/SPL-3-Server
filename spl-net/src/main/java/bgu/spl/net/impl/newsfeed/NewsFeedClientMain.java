package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.impl.rci.RCIClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class NewsFeedClientMain {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[]{"127.0.0.1"};
        }

        /*System.out.println("running clients");
        runFirstClient(args[0]);
        runSecondClient(args[0]);
        runThirdClient(args[0]);*/
        MessageEncoderDecoderImpl x = new MessageEncoderDecoderImpl();

        String userName = "Tomer";
        String password = "1234";
        byte[] bytesForRegister;
        byte[] userNameBytes = userName.getBytes("utf-8");
        byte[] passwordBytes = password.getBytes("utf-8");
        bytesForRegister = new byte[userNameBytes.length + passwordBytes.length + 4];
        bytesForRegister[0] = 0;
        bytesForRegister[1] = 1;
        int index = 2;
        for(int i = 0; i < userNameBytes.length; i++) {
            bytesForRegister[index] = userNameBytes[i];
            index++;
        }
        bytesForRegister[index] = '\0';
        index++;
        for(int i = 0; i < passwordBytes.length; i++) {
            bytesForRegister[index] = passwordBytes[i];
            index++;
        }
        bytesForRegister[index] = '\0';
        Socket s = new Socket(args[0], 7777);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        int i = 0;
        while(i < 2) {
            for (byte b : bytesForRegister) {
                System.out.println("sending message to server");
                out.write(b);
                out.flush();
                Message m = x.decodeNextByte(b);
                if(m != null) {
                    System.out.println(m);
                }
            }
            i++;
        }

        runFirstClient(args[0]);
        runSecondClient(args[0]);
        runThirdClient(args[0]);


    }

    private static void runFirstClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(new PublishNewsCommand(
                    "jobs",
                    "System Programmer, knowledge in C++, Java and Python required. call 0x134693F"));

            c.receive(); //ok

            c.send(new PublishNewsCommand(
                    "headlines",
                    "new SPL assignment is out soon!!"));

            c.receive(); //ok

            c.send(new PublishNewsCommand(
                    "headlines",
                    "THE CAKE IS A LIE!"));

            c.receive(); //ok
        }

    }

    private static void runSecondClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(new FetchNewsCommand("jobs"));
            System.out.println("second client received: " + c.receive());
        }
    }

    private static void runThirdClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(new FetchNewsCommand("headlines"));
            System.out.println("third client received: " + c.receive());
        }
    }
}
