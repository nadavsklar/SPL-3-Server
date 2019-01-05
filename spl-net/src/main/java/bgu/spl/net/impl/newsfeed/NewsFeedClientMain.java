package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.bidi.Users;
import bgu.spl.net.impl.rci.RCIClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

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

        Scanner scan= new Scanner(System.in);
        String userName= scan.nextLine();
        String password = scan.nextLine();

        byte[] bytesForRegister;
        byte[] bytesForLogin;
        byte[] bytesForLogout;
        byte[] bytesForUserList;
        byte[] userNameBytes = userName.getBytes("utf-8");
        byte[] passwordBytes = password.getBytes("utf-8");
        bytesForRegister = new byte[userNameBytes.length + passwordBytes.length + 4];
        bytesForLogin = new byte[userNameBytes.length + passwordBytes.length + 4];
        bytesForLogout = new byte[3];
        bytesForUserList = new byte[3];
        bytesForRegister[0] = 0;
        bytesForRegister[1] = 1;
        bytesForLogin[0] = 0;
        bytesForLogin[1] = 2;
        bytesForLogout[0] = 0;
        bytesForLogout[1] = 3;
        bytesForLogout[2] = '\0';
        bytesForUserList[0] = 0;
        bytesForUserList[1] = 7;
        bytesForUserList[2] = '\0';
        int index = 2;
        for(int i = 0; i < userNameBytes.length; i++) {
            bytesForRegister[index] = userNameBytes[i];
            bytesForLogin[index] = userNameBytes[i];
            index++;
        }
        bytesForRegister[index] = '\0';
        bytesForLogin[index] = '\0';
        index++;
        for(int i = 0; i < passwordBytes.length; i++) {
            bytesForRegister[index] = passwordBytes[i];
            bytesForLogin[index] = passwordBytes[i];
            index++;
        }
        bytesForRegister[index] = '\0';
        bytesForLogin[index] = '\0';

        Socket s = new Socket(args[0], 7777);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

        for (byte b : bytesForRegister) {
            System.out.println("sending Register Message to server");
            out.write(b);
            out.flush();
            Message m = x.decodeNextByte(b);
            if(m != null) {
                System.out.println(m);
            }
        }

        for (byte b : bytesForLogin) {
            System.out.println("sending Login Message to server");
            out.write(b);
            out.flush();
            Message m = x.decodeNextByte(b);
            if(m != null) {
                System.out.println(m);
            }
        }

        /*for(byte b: bytesForUserList){
            System.out.println("sending UserList Message to server");
            out.write(b);
            out.flush();
            Message m = x.decodeNextByte(b);
            if(m != null) {
                System.out.println(m);
            }
        }*/

        for(byte b: bytesForLogout){
            System.out.println("sending Logout Message to server");
            out.write(b);
            out.flush();
            Message m = x.decodeNextByte(b);
            if(m != null) {
                System.out.println(m);
            }
        }


        //runFirstClient(args[0]);
        //runSecondClient(args[0]);
        //runThirdClient(args[0]);


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
