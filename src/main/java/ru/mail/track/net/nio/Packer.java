package ru.mail.track.net.nio;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by aliakseisemchankau on 3.12.15.
 */
public class Packer {

    public static byte[] pack (byte[] data) {

        //System.err.println("data.length=" + data.length);

        byte[] len = longToBytes(data.length);

        //for(int i = 0 ; i < len.length; ++i) {
        //    System.err.println("len[" + i + "]=" + len[i]);
        //}
        //System.err.println("len.length=" + len.length);
        byte[] dataCopy = new byte[len.length + data.length];

        //System.err.println(new String(len) + "=len");
        try {
            System.arraycopy(len, 0, dataCopy, 0, len.length);
            System.arraycopy(data, 0, dataCopy, len.length, data.length);
        } catch (Exception exc) {
            System.err.println("kek");
            exc.printStackTrace();
        }

        return dataCopy;
    }

    public static byte[] unpack(byte[] data) {
        byte[] dataCopy = new byte[data.length - 8];
        System.arraycopy(data, 8, dataCopy, 0, data.length - 8);
        return dataCopy;
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static long defineLength(List<Byte> data) {
        byte[] len = new byte[8];
        for(int i= 0 ; i < 8; ++i) {
            len[i] = data.get(i).byteValue();
        }
        return bytesToLong(len);
    }



}
