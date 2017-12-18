package nari.MemCache.btree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Node {
    
    public static final int MIN_KEY_AMOUNT=1;
    public int numKeys;
    public long[] keys=new long[MIN_KEY_AMOUNT*2];
    public long[] valuePointers=new long[MIN_KEY_AMOUNT*2];
    public long[] childrenPointers=new long[MIN_KEY_AMOUNT*2+1];
    public long pointer;
    
    public byte[] serialize() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numKeys);
        for (int i = 0; i < MIN_KEY_AMOUNT*2; i++){
            dos.writeLong(keys[i]);
        }
        for (int i = 0; i < MIN_KEY_AMOUNT*2; i++){
            dos.writeLong(valuePointers[i]);
        }
        for (int i = 0; i < MIN_KEY_AMOUNT*2+1; i++){
            dos.writeLong(childrenPointers[i]);
        }
        dos.writeLong(pointer);
        
        baos.close();
        dos.close();
        return baos.toByteArray();
    }
    
    public static Node deserialize(byte[] b) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        Node newNode=new Node();
        newNode.numKeys=dis.readInt();
        for (int i = 0; i <  MIN_KEY_AMOUNT*2; i++){
            newNode.keys[i]=dis.readLong();
        }
        for (int i = 0; i <  MIN_KEY_AMOUNT*2; i++){
            newNode.valuePointers[i]=dis.readLong();
        }
        for (int i = 0; i <  MIN_KEY_AMOUNT*2+1; i++){
            newNode.childrenPointers[i]=dis.readLong();
        }
        newNode.pointer=dis.readLong();
        
        bais.close();
        dis.close();
        
        return newNode;
    }
    
    public static int size() {
        int size= 8*MIN_KEY_AMOUNT*2*2 + 8*(MIN_KEY_AMOUNT*2+1) + 4 + 8;
        return size;
    }
}
