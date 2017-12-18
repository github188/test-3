package nari.SpatialIndex.searcher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import nari.SpatialIndex.buffer.BufferHelper;
import nari.SpatialIndex.buffer.ByteHelper;
import nari.SpatialIndex.loader.Disk;

public class GridSubIndexLayer extends PooledSubIndexLayer {

	private double xmax;
	
	private double ymax;
	
	private double xmin;
	
	private double ymin;
	
//	private int count;
	
	private String subLayerID;
	
	private Disk disk;
	
	private LayerHeader header;
	
	private File store = null;
	
//	private FileChannel channel = null;
	
	public GridSubIndexLayer(byte[] src,Disk disk,LayerHeader header) {
		ByteBuffer buf = ByteBuffer.wrap(src, 0, 8);
		xmin = buf.getDouble();
		
		buf = ByteBuffer.wrap(src, 8, 8);
		ymin = buf.getDouble();
		
		buf = ByteBuffer.wrap(src, 16, 8);
		xmax = buf.getDouble();
		
		buf = ByteBuffer.wrap(src, 24, 8);
		ymax = buf.getDouble();
		
//		buf = ByteBuffer.wrap(src, 32, 4);
//		count = buf.getInt();
		
		buf = ByteBuffer.wrap(src, 36, 4);
		int nameLength = buf.getInt();
		
		buf = ByteBuffer.wrap(src, 40, nameLength);
		byte[] b = new byte[nameLength];
		buf.get(b,0,nameLength);
		
		try {
			subLayerID = new String(b,"utf-8");
		} catch (UnsupportedEncodingException e) {
			
		}
		this.disk = disk;
		this.header = header;
	}
	
	public GridSubIndexLayer(double xmin,double ymin,double xmax,double ymax,int count,String subLayerID,Disk disk,LayerHeader header) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
		this.subLayerID = subLayerID;
		this.disk = disk;
		this.header = header;
	}
	
	@Override
	public double getXMax()  throws Exception{
		return xmax;
	}

	@Override
	public double getYMax()  throws Exception{
		return ymax;
	}

	@Override
	public double getXMin()  throws Exception{
		return xmin;
	}

	@Override
	public double getYMin()  throws Exception{
		return ymin;
	}

	@Override
	public String getSubLayerID()  throws Exception{
		return subLayerID;
	}

	@Override
	public int getCount()  throws Exception{
		RandomAccessFile file = new RandomAccessFile(store,"rw");
		FileChannel channel = file.getChannel();
		FileLock lock = null;
		int count = 0;
		do{
			try{
				lock = channel.tryLock(0, 4, true);
				if(lock==null){
					continue;
				}
				MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
				
				byte[] dst = new byte[4];
				buf.get(dst);
				count = ByteHelper.getInt(dst);
				
				BufferHelper.ummap(buf);
				
			} catch(Exception e){
				Thread.sleep(10);
			} finally {
				if(lock!=null){
					channel.close();
				}
			}
		} while(lock==null);
		
		return count;
	}
	
	@Override
	public Disk getDisk()  throws Exception{
		return disk;
	}
	
	@Override
	public String getPath()  throws Exception{
		File file = new File(getDisk().getFile().getParentFile(),getSubLayerID());
		return file.getPath();
	}
	
	@Override
	public LayerHeader getHeader() throws Exception {
		return header;
	}

	@Override
	protected boolean doWrite(byte[] data,int length) throws Exception {
		byte[] lenBytes = ByteHelper.toByte(data.length);
		byte[] newBytes = new byte[lenBytes.length+data.length];
		
		System.arraycopy(lenBytes, 0, newBytes, 0, lenBytes.length);
		System.arraycopy(data, 0, newBytes, lenBytes.length, data.length);
		RandomAccessFile file = new RandomAccessFile(store,"rw");
		FileChannel channel = file.getChannel();
				
		FileLock lock = null;
		MappedByteBuffer buf = null;
		do{
			try{
				lock = channel.tryLock(channel.size(), newBytes.length, false);
				
				if(lock==null || !lock.isValid()){
					continue;
				}
				buf = channel.map(FileChannel.MapMode.READ_WRITE, channel.size(), newBytes.length);
				buf.put(newBytes);
				
				BufferHelper.ummap(buf);
				
				lock = channel.tryLock(0, 4, false);
				if(lock==null){
					continue;
				}
				buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
				
				byte[] dst = new byte[4];
				buf.get(dst);
				int count = ByteHelper.getInt(dst);
				count = count+1;
				BufferHelper.ummap(buf);
				
				buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
				buf.put(ByteHelper.toByte(count));
				BufferHelper.ummap(buf);
				
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

	@Override
	public boolean exist() throws Exception {
		store = new File(getDisk().getFile().getParentFile(),getSubLayerID());
		return store.exists();
	}

	@Override
	public boolean makeLayer() throws Exception {
		if(store==null){
			store = new File(getDisk().getFile().getParentFile(),getSubLayerID());
		}
		
		if(!store.exists()){
			store.createNewFile();
			
			RandomAccessFile file = new RandomAccessFile(store,"rw");
			FileChannel channel = file.getChannel();
			
			FileLock lock = null;
			do{
				try {
					lock = channel.tryLock(0,4,false);
					if(lock==null){
						continue;
					}
					final MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4);
					buf.put(ByteHelper.toByte(0));
					BufferHelper.ummap(buf);
				} catch(Exception e){
					Thread.sleep(10);
				} finally {
					if(lock!=null){
						channel.close();
					}
				}
			} while(lock==null);
		}
		
		return true;
	}

	@Override
	protected ResultSet doRead() throws Exception {
		RandomAccessFile file = new RandomAccessFile(store,"rw");
		FileChannel channel = file.getChannel();
		
		FileLock lock = null;
		MappedByteBuffer buf = null;
		byte[] allBytes = null;
		
		do{
			try {
				lock = channel.tryLock(0, channel.size(), true);
				if(lock==null){
					continue;
				}
				
				buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
				if(buf.hasRemaining()){
					if(buf.hasArray()){
						allBytes = buf.array();
					}else{
						allBytes = new byte[buf.capacity()];
						buf.get(allBytes);
					}
				}
				BufferHelper.ummap(buf);
			} catch(Exception e){
				Thread.sleep(10);
			} finally {
				if(lock!=null){
					channel.close();
				}
			}
			
		}while(lock==null);
		
		if(allBytes==null){
			return null;
		}
		ByteBuffer bf = ByteBuffer.wrap(allBytes);
		int size = bf.getInt();
		int len = 0;
		List<SerialObject> objs = new ArrayList<SerialObject>();
		for(int i=0;i<size;i++){
			len = bf.getInt();
			if(len==0){
				break;
			}
			byte[] bt = new byte[len];
			bf.get(bt);
			ByteArrayInputStream in = new ByteArrayInputStream(bt);
			ObjectInputStream stream = new ObjectInputStream(in);
			SerialObject obj = (SerialObject)stream.readObject();
			obj.setData(bt);
			objs.add(obj);
		}
		
		return new GridDataResultSet(objs);
	}
	
}
