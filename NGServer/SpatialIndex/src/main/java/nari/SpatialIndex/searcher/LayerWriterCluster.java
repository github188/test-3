package nari.SpatialIndex.searcher;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import nari.SpatialIndex.buffer.BufferHelper;
import nari.SpatialIndex.buffer.ByteHelper;

public class LayerWriterCluster {

	private SubIndexLayer layer;
	
	private final List<byte[]> list = new ArrayList<byte[]>();
	
	public static final AtomicInteger ref = new AtomicInteger();
	
	public LayerWriterCluster(SubIndexLayer layer){
		this.layer = layer;
	}
	
	public void addRecord(LayerBridge data){
		list.add(data.getByte());
	}
	
	public boolean write() throws Exception {
		List<byte[]> bl = new ArrayList<byte[]>();
		
		int size = 0;
		for(byte[] data:list){
			byte[] lenBytes = ByteHelper.toByte(data.length);
			byte[] newBytes = new byte[lenBytes.length+data.length];
			
			System.arraycopy(lenBytes, 0, newBytes, 0, lenBytes.length);
			System.arraycopy(data, 0, newBytes, lenBytes.length, data.length);
			size = size + newBytes.length;
			bl.add(newBytes);
		}
		
		byte[] dataBytes = new byte[size];
		int i=0;
		for(byte[] b:bl){
			System.arraycopy(b, 0, dataBytes, i, b.length);
			i+=b.length;
		}
		
		int recordCount = list.size();
		
		File store = new File(layer.getDisk().getFile().getParentFile(),layer.getSubLayerID());
		RandomAccessFile file = new RandomAccessFile(store,"rw");
		FileChannel channel = file.getChannel();
		
		FileLock lock = null;
		MappedByteBuffer buf = null;
		do{
			try{
				long pos = channel.size();
				lock = channel.tryLock(/**pos==0?4:pos, dataBytes.length, false**/);
				
				if(lock==null || !lock.isValid()){
					continue;
				}
				buf = channel.map(FileChannel.MapMode.READ_WRITE, pos==0?4:pos, dataBytes.length);
				buf.put(dataBytes);
				
				BufferHelper.ummap(buf);
				
//				lock = channel.tryLock(0, 4, false);
//				if(lock==null){
//					continue;
//				}
				buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
				
				byte[] dst = new byte[4];
				buf.get(dst);
				int count = ByteHelper.getInt(dst);
				count = count+recordCount;
				buf.position(0);
				buf.put(ByteHelper.toByte(count));
				BufferHelper.ummap(buf);
				
//				buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
//				buf.put(ByteHelper.toByte(count));
//				BufferHelper.ummap(buf);
				
			} catch(Exception e){
				Thread.sleep(10);
			} finally {
				if(lock!=null){
					channel.close();
				}
			}
		} while(lock==null);
		
		return true;
	}
}
