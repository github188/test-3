package nari.MemCache.btree;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BTree2 {
    private int flag;
    private int btreeLevel = 0;
    private int btreeCount = 0;
    @SuppressWarnings("unused")
	private int nodeSum = 0;
    private int level;
    private Node newTree;
    
    private long insValue;
    private long insKey;
    
    private NodeOperator file;
    
    public BTree2(String idxFile) throws  IOException {
        try {
            file=new NodeOperator(idxFile);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public Result search(long key,Node t) throws IOException {
        int i,j,m;
        level=btreeLevel-1;
        while (level >= 0){
            j=t.numKeys-1;
            for(i=0; i<j; ) {
                m=(j+i)/2;
                if(key>t.keys[m])
                    i=m+1;
                else
                    j=m;
            }
            
            if (key ==t.keys[i]){
                Result r=new Result();
                r.btreeDisp = i;
                r.resultNode=t;
                return r;
            }
            if (key > t.keys[ i ]){
                i++;
            }
            long pos = t.childrenPointers [i];
            t=file.loadNode(pos);
            level--;
        }
        return null;
    }
    
    public Node insert(long key,long val,Node t) throws IOException, KeyExistsException {
        insValue=val;
        
        level=btreeLevel;
        internalInsert(key, t);
        if (flag == 1){
            t=newRoot(t);
        }
        return t;
    }
    
    private void internalInsert(long key,Node t) throws IOException, KeyExistsException {
        int i,j,m;
        
        level--;
        if (level < 0){
            newTree = null;
            insKey = key;
            btreeCount++;
            flag = 1;
            return;
        }
        j=t.numKeys-1;
        for(i=0; i<j; ) {
            m=(j+i)/2;
            if(key>t.keys[m]){
                i=m+1;
            }else{
                j=m;
            }
        }
        if (key == t.keys[i]) {
            flag = 0;
            throw new KeyExistsException();
        }
        if (key > t.keys[i]){
            i++;
        }
        Node n=file.loadNode(t.childrenPointers[i]);
        internalInsert(key,n);
        
        if (flag == 0){
            return;
        }
        if (t.numKeys < 2*Node.MIN_KEY_AMOUNT) {
            insInNode(t, i);
            flag = 0;
        } else{
            splitNode(t, i);
        }
    }
    
    private void insInNode(Node t, int d) throws IOException {
        int i;
        for(i = t.numKeys; i > d; i--){
            t.keys[ i ] = t.keys[i-1];
            t.valuePointers[ i ] = t.valuePointers[i-1];
            t.childrenPointers[i+1] = t.childrenPointers[ i ];
        }
        t.keys[ i ] = insKey;
        if(newTree!=null){
            t.childrenPointers[i+1] = newTree.pointer;
        }
        t.valuePointers[ i ] = insValue;
        t.numKeys++;
        file.writeNode(t);
    }
    
    private void splitNode(Node t, int d) throws IOException {
        int i,j;
        Node temp;
        long tempK;
        long tempV;
        temp = new Node();
        temp.pointer=file.getAvailableFilePointer();
    /*
     *   +---+--------+-----+-----+--------+-----+
     *   | 0 | ...... |  M  | M+1 | ...... |2*M-1|
     *   +---+--------+-----+-----+--------+-----+
     *   |<-      M+1     ->|<-        M-1     ->|
     */
        if (d > Node.MIN_KEY_AMOUNT) {
            for(i=2*Node.MIN_KEY_AMOUNT-1,j=Node.MIN_KEY_AMOUNT-1; i>=d; i--,j--) {
                temp.keys[j] = t.keys[ i ];
                temp.valuePointers[j] = t.valuePointers[ i ];
                temp.childrenPointers[j+1] = t.childrenPointers[i+1];
            }
            for(i=d-1,j=d-Node.MIN_KEY_AMOUNT-2; j>=0; i--,j--) {
                temp.keys[j] = t.keys[ i ];
                temp.valuePointers[j] = t.valuePointers[ i ];
                temp.childrenPointers[j+1] = t.childrenPointers[i+1];
            }
            temp.childrenPointers[0] = t.childrenPointers[Node.MIN_KEY_AMOUNT+1];
            temp.keys[d-Node.MIN_KEY_AMOUNT-1] = insKey;
            if(newTree!=null){
                temp.childrenPointers[d-Node.MIN_KEY_AMOUNT] = newTree.pointer;
            }
            temp.valuePointers[d-Node.MIN_KEY_AMOUNT-1] = insValue;
            insKey = t.keys[Node.MIN_KEY_AMOUNT];
            insValue = t.valuePointers[Node.MIN_KEY_AMOUNT];
            
        } else {
            for(i=2*Node.MIN_KEY_AMOUNT-1,j=Node.MIN_KEY_AMOUNT-1; j>=0; i--,j--) {
                temp.keys[j] = t.keys[ i ];
                temp.valuePointers[j] = t.valuePointers[ i ];
                temp.childrenPointers[j+1] = t.childrenPointers[i+1];
            }
            if (d == Node.MIN_KEY_AMOUNT) {
                if(newTree!=null){
                    temp.childrenPointers[0] = newTree.pointer;
                }
            } else {
                temp.childrenPointers[0] = t.childrenPointers[Node.MIN_KEY_AMOUNT];
                tempK = t.keys[Node.MIN_KEY_AMOUNT-1];
                tempV = t.valuePointers[Node.MIN_KEY_AMOUNT-1];
                for(i=Node.MIN_KEY_AMOUNT-1; i>d; i--) {
                    t.keys[ i ] = t.keys[i-1];
                    t.valuePointers[ i ] = t.valuePointers[i-1];
                    t.childrenPointers[i+1] = t.childrenPointers[ i ];
                }
                t.keys[d] = insKey;
                if(newTree!=null){
                    t.childrenPointers[d+1] = newTree.pointer;
                }
                t.valuePointers[d] = insValue;
                insKey = tempK;
                insValue = tempV;
            }
        }
        t.numKeys =Node.MIN_KEY_AMOUNT;
        temp.numKeys = Node.MIN_KEY_AMOUNT;
        file.writeNode(t);
        file.writeNode(temp);
        newTree = temp;
        nodeSum++;
    }
    
    private Node newRoot(Node t) throws IOException {
        Node temp=new Node();
        temp.numKeys = 1;
        temp.childrenPointers[0] = t.pointer;
        
        if(newTree!=null){
            temp.childrenPointers[1] = newTree.pointer;
        }
        temp.keys[0] = insKey;
        temp.valuePointers[0] = insValue;
        temp.pointer=file.getAvailableFilePointer();
        file.writeNode(temp);
        btreeLevel++;
        nodeSum++;
        return(temp);
    }
    
    public Node delete(long key, Node t) throws IOException, KeyNotFoundException {
        level=btreeLevel;
        internalDelete(key, t);
        if (t.numKeys == 0){
            t=freeRoot(t);
        }
        return t;
    }
    
    void internalDelete(long key, Node t) throws IOException, KeyNotFoundException {
        
        int i,j,m;
        Node l,r;
        int lvl;
        
        level--;
        if (level < 0) {
            flag = 0;
            throw new KeyNotFoundException();
        }
        j=t.numKeys-1;
        for(i=0; i<j; ) {
            m=(j+i)/2;
            if (key > t.keys[m])
                i=m+1;
            else
                j=m;
        }
        if (key == t.keys[ i ]) {
            
            if (level == 0) {
                delFromNode(t,i);
                btreeCount--;
                flag = 1;
                return;
            } else {
                lvl = level-1;
                r = file.loadNode(t.childrenPointers[ i ]);
                while (lvl > 0)  {
                    r = file.loadNode(r.childrenPointers[r.numKeys]);
                    lvl--;
                }
                t.keys[ i ]=r.keys[r.numKeys-1];
                t.valuePointers[ i ]=r.valuePointers[r.numKeys-1];
                r.valuePointers[r.numKeys-1]=0;
                key = r.keys[r.numKeys-1];
                file.writeNode(t);
                file.writeNode(r);
            }
        } else if (key > t.keys[ i ]) {
            i++;
        }
        internalDelete(key, file.loadNode(t.childrenPointers[ i ]));
        
        if (flag == 0)
            return;
        if ( file.loadNode(t.childrenPointers[ i ]).numKeys < Node.MIN_KEY_AMOUNT) {
            if (i == t.numKeys){
                i--;
            }
            l =  file.loadNode(t.childrenPointers[ i]);
            r = file.loadNode(t.childrenPointers[ i+1 ]);
            if (r.numKeys > Node.MIN_KEY_AMOUNT){
                moveLeftNode(t,i);
            } else if(l.numKeys >  Node.MIN_KEY_AMOUNT){
                moveRightNode(t,i);
            } else {
                joinNode(t,i);
                return;
            }
            flag = 0;
        }
    }
    
    private Node freeRoot(Node t) throws IOException {
        Node temp;
        temp = file.loadNode(t.childrenPointers[0]);
        file.deleteNode(t);
        btreeLevel--;
        nodeSum--;
        return temp;
    }
    
    private void delFromNode(Node t, int d) throws IOException {
        int i;
        for(i=d; i < t.numKeys-1; i++) {
            t.keys[ i ] = t.keys[i+1];
            t.valuePointers[ i ] = t.valuePointers[i+1];
        }
        t.numKeys--;
        file.writeNode(t);
    }
    
    private void moveLeftNode(Node t, int d) throws IOException {
        Node l,r;
        int m;
        int i,j;
        l = file.loadNode(t.childrenPointers[d]);
        r = file.loadNode(t.childrenPointers[d+1]);
        m = (r.numKeys - l.numKeys)/2;
        
        l.keys[l.numKeys] = t.keys[d];
        l.valuePointers[l.numKeys] = t.valuePointers[d];
        for (j=m-2,i=l.numKeys+m-1; j >= 0; j--,i--) {
            l.keys[ i ] = r.keys[ j ];
            l.valuePointers[ i ] = r.valuePointers[ j ];
            l.childrenPointers[ i ] = r.childrenPointers[ j ];
        }
        l.childrenPointers[l.numKeys+m] = r.childrenPointers[m-1];
        t.keys[d] = r.keys[m-1];
        t.valuePointers[d] = r.valuePointers[m-1];
        r.childrenPointers[0] = r.childrenPointers[m];
        for (i=0; i<r.numKeys-m; i++) {
            r.keys[ i ] = r.keys[i+m];
            r.valuePointers[ i ] = r.valuePointers[i+m];
            r.childrenPointers[ i ] = r.childrenPointers[i+m];
        }
        r.childrenPointers[r.numKeys-m] = r.childrenPointers[r.numKeys];
        l.numKeys+=m;
        r.numKeys-=m;
        file.writeNode(l);
        file.writeNode(r);
        file.writeNode(t);
    }
    
    private void joinNode(Node t, int d) throws IOException {
        Node l,r;
        int i,j;
        l = file.loadNode(t.childrenPointers[d]);
        r = file.loadNode(t.childrenPointers[d+1]);
        
        l.keys[l.numKeys] = t.keys[d];
        l.valuePointers[l.numKeys] = t.valuePointers[d];
        for (j=r.numKeys-1,i=l.numKeys+r.numKeys; j >= 0 ; j--,i--) {
            l.keys[ i ] = r.keys[j];
            l.valuePointers[ i ] = r.valuePointers[j];
            l.childrenPointers[ i ] = r.childrenPointers[j];
        }
        l.childrenPointers[l.numKeys+r.numKeys+1] = r.childrenPointers[r.numKeys];
        l.numKeys += r.numKeys+1;
        file.deleteNode(r);
        for (i=d; i < t.numKeys-1; i++) {
            t.keys[ i ] = t.keys[i+1];
            t.valuePointers[ i ] = t.valuePointers[i+1];
            t.childrenPointers[i+1] = t.childrenPointers[i+2];
        }
        t.numKeys--;
        file.writeNode(t);
        file.writeNode(l);
        nodeSum--;
        
    }
    
    private void moveRightNode(Node t, int d) throws IOException {
        Node l,r;
        int m;
        int i,j;
        l = file.loadNode(t.childrenPointers[d]);
        r = file.loadNode(t.childrenPointers[d+1]);
        
        m = (l.numKeys - r.numKeys)/2;
        r.childrenPointers[r.numKeys+m]=r.childrenPointers[r.numKeys];
        for (i=r.numKeys-1; i>=0; i--) {
            r.keys[i+m] = r.keys[ i ];
            r.valuePointers[i+m] = r.valuePointers[ i ];
            r.childrenPointers[i+m] = r.childrenPointers[ i ];
        }
        r.keys[m-1] = t.keys[d];
        r.valuePointers[m-1] = t.valuePointers[d];
        r.childrenPointers[m-1] = l.childrenPointers[l.numKeys];
        for (i=l.numKeys-1,j=m-2; j>=0; j--,i--) {
            r.keys[j] = l.keys[ i ];
            r.valuePointers[j] = l.valuePointers[ i ];
            r.childrenPointers[j] = l.childrenPointers[ i ];
        }
        t.keys[d] = l.keys[ i ];
        t.valuePointers[d] = l.valuePointers[ i ];
        l.numKeys-=m;
        r.numKeys+=m;
        file.writeNode(r);
        file.writeNode(l);
        file.writeNode(t);
    }
    
    public void close() throws IOException {
        file.close();
    }
    
    public int height() {
        return btreeLevel;
    }
    
    public int count() {
        return btreeCount;
    }
}