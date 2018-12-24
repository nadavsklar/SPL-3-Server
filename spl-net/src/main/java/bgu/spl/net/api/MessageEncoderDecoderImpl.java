package bgu.spl.net.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageEncoderDecoderImpl<Short> implements MessageEncoderDecoder<Short> {

    private List<Byte> bytes;
    private String encoding;
    private int bytesReaded;
    private HashMap<Integer, String> symbolTable;

    public MessageEncoderDecoderImpl(){
        bytes = new LinkedList<>();
        encoding = "utf-8";
        bytesReaded = 0;
    }

    @Override
    public Short decodeNextByte(byte nextByte) {
        bytesReaded++;
        bytes.add(nextByte);
        if(bytesReaded == 2) {
            short messageOpcode = bytesToShort((Byte[]) bytes.toArray());
        }
        return null;
    }

    public short bytesToShort(Byte[] byteArr) {
        short result = (short)((byteArr[0].byteValue() & 0xff) << 8);
        result += (short)(byteArr[1].byteValue() & 0xff);
        return result;
    }

    @Override
    public byte[] encode(Short message) {
        return new byte[0];
    }
}
