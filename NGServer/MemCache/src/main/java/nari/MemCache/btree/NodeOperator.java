package nari.MemCache.btree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Stack;

public class NodeOperator {
    private RandomAccessFile idxFile;
    private Stack<Long> gapStack;
    @SuppressWarnings("unchecked")
	public NodeOperator(String fileName) throws IOException {
        try {
            idxFile=new RandomAccessFile(fileName,"rw");
        } catch (FileNotFoundException ex) {
        	
        }
        
        try {
            FileInputStream fis=new FileInputStream("idx.gap");
            ObjectInputStream ois=new ObjectInputStream(fis);
            gapStack=(Stack<Long>)ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            gapStack=new Stack<Long>();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void writeNode(Node node) throws IOException {
        idxFile.seek(node.pointer);
        idxFile.write(node.serialize());
    }
    
    public Node loadNode(long pos) throws IOException {
        idxFile.seek(pos);
        byte []b=new byte[Node.size()];
        idxFile.read(b);
        return Node.deserialize(b);
    }
    
    public void deleteNode(Node node) {
        gapStack.push(node.pointer);
    }
    
    public long getAvailableFilePointer() throws IOException {
        if(gapStack.size()!=0){
            return gapStack.pop();
        }else{
            return (long)idxFile.length();
        }
    }
    
    public void close() throws IOException {
        FileOutputStream fos=new FileOutputStream("idx.gap");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(gapStack);
        oos.close();
        fos.close();
        idxFile.close();
    }
}
