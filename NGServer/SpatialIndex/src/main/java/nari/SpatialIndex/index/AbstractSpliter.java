package nari.SpatialIndex.index;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.SpatialIndex.buffer.BufferHelper;
import nari.SpatialIndex.buffer.ByteHelper;
import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.loader.Disk;
import nari.SpatialIndex.loader.PhysicsDisk;
import nari.SpatialIndex.searcher.GridIndexLayerHeader;
import nari.SpatialIndex.searcher.LayerHeader;
import nari.SpatialIndex.searcher.SerialObject;

public abstract class AbstractSpliter implements Spliter {

private final int poolSize = Runtime.getRuntime().availableProcessors()+1;
	
	private final int MAX = 10;
	
	private ExecutorService exec = null;
	
	private ExecutorService readExec = null;
	
	private ArrayBlockingQueue<File> queue;
	
	private Disk disk = null;
	
	private final AtomicBoolean b = new AtomicBoolean(false);
	
	@Override
	public boolean init() throws Exception {
		doInit();
		queue = new ArrayBlockingQueue<File>(1000);
		exec = Executors.newFixedThreadPool(poolSize);
		readExec = Executors.newSingleThreadExecutor();
		disk = getDisk();
		return true;
	}

	@Override
	public boolean start() throws Exception {
		doStart();
		
		if(exec==null){
			throw new NullPointerException("thread pool for reader is not init");
		}
		
		if(disk==null){
			throw new NullPointerException("index root not found");
		}
		
		File file = disk.getFile();
		final File[] files = file.listFiles();
		
		
		readExec.submit(new Runnable() {
			
			@Override
			public void run() {
				
				for(File file:files){
					try {
						queue.put(file);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				b.compareAndSet(false, true);
			}
		});
		
		final CountDownLatch latch = new CountDownLatch(poolSize);
		
		for(int i=0;i<poolSize;i++){
			exec.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						File[] fileList = null;
						File file = queue.poll(10, TimeUnit.MILLISECONDS);
						while(file!=null || !b.get()){
							fileList = file.listFiles();
							if(fileList==null || fileList.length==0){
								continue;
							}
							
							for(File f:fileList){
								if(f.getName().contains("index")){
									continue;
								}
								
								int recordCount = getRecordCount(f);
								if(recordCount<=MAX){
									continue;
								}
								System.out.println(f.getPath());
								String layerId = f.getName().split("$")[0];
								
								String indexFileName = layerId+"$index";
								
								File indexFile = new File(file,indexFileName);
								
								FileChannel channel = getChannel(indexFile,"rw");
								SpliterLayerIndex[] layers = getSubLayers(channel,file);

								SpliterLayerIndex indexLayer = null;
								for(SpliterLayerIndex layer:layers){
									if(layer.getSubLayerID().equals(f.getName())){
										indexLayer = layer;
										break;
									}
								}
								
								if(indexLayer==null){
									continue;
								}
								
								File dataFile = new File(file,indexLayer.getSubLayerID());
								List<SerialObject> records = readRecord(dataFile);
								
								split(indexLayer,channel);
								
								channel.close();
								dataFile.delete();
								
								LayerHeader header = new GridIndexLayerHeader(null, new PhysicsDisk(disk,layerId));
								for(SerialObject obj:records){
									Geometry geom = (Geometry)obj.getValue("geometry");
									header.write(geom, obj.getData());
								}
								
							}
							
							file = queue.poll(10, TimeUnit.MILLISECONDS);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						latch.countDown();
					}
				}
			});
		}
		latch.await();
		return true;
	}
	
	private void split(SpliterLayerIndex indexLayer,FileChannel channel) throws Exception{
		double xmin = indexLayer.getXmin();
		double ymin = indexLayer.getYmin();
		double xmax = indexLayer.getXmax();
		double ymax = indexLayer.getYmax();
		
		String layerId = indexLayer.getSubLayerID();
		
		SpliterLayerIndex layer1 = new SpliterLayerIndex(layerId+"$1",xmin,ymin,xmin + (xmax-xmin)/2,ymin + (ymax-ymin)/2,indexLayer.getFile());
		layer1.makeLayer();
		
		SpliterLayerIndex layer2 = new SpliterLayerIndex(layerId+"$2",xmin + (xmax-xmin)/2,ymin,xmax,ymin + (ymax-ymin)/2,indexLayer.getFile());
		layer2.makeLayer();
		
		SpliterLayerIndex layer3 = new SpliterLayerIndex(layerId+"$3",xmin,ymin + (ymax-ymin)/2,xmin + (xmax-xmin)/2,ymax,indexLayer.getFile());
		layer3.makeLayer();
		
		SpliterLayerIndex layer4 = new SpliterLayerIndex(layerId+"$4",xmin + (xmax-xmin)/2,ymin + (ymax-ymin)/2,xmax,ymax,indexLayer.getFile());
		layer4.makeLayer();
		
		SpliterLayerIndex[] layers = new SpliterLayerIndex[]{layer1,layer2,layer3,layer4};
		
		byte[] bytes = create(layers);
		
		MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
		
		buf.position(0);
		buf.put(bytes);
		
		BufferHelper.ummap(buf);
	}
	
	private byte[] create(SpliterLayerIndex[] layers) throws Exception {
		
		List<byte[]> list = new ArrayList<byte[]>();
		for(SpliterLayerIndex layer:layers){
			int index = 0;
			String layerId = layer.getSubLayerID();
			byte[] b = layerId.getBytes("utf-8");
			
			int blockSize = 44+b.length;
			byte[] block = new byte[blockSize];
			
			byte[] bytes = ByteHelper.toByte(blockSize);
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getXmin());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getYmin());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getXmax());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(layer.getYmax());
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
//			bytes =ByteHelper.toByte(layer.getCount());
			bytes =ByteHelper.toByte(0);
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			bytes =ByteHelper.toByte(b.length);
			System.arraycopy(bytes, 0, block, index, bytes.length);
			index +=bytes.length;
			
			System.arraycopy(b, 0, block, index, b.length);
			list.add(block);
		}
		int total = 8;
		for(byte[] b:list){
			total = total + b.length;
		}
		
		byte[] allBytes = new byte[total];
		
		byte[] bytes =ByteHelper.toByte(total);
		System.arraycopy(bytes, 0, allBytes, 0, bytes.length);
		
		
		bytes =ByteHelper.toByte(layers.length);
		System.arraycopy(bytes, 0, allBytes, 4, bytes.length);
		
		int start = 8;
		
		for(byte[] b:list){
			System.arraycopy(b, 0, allBytes, start, b.length);
			start = start + b.length;
		}
		
		return allBytes;
	}
	
	private List<SerialObject> readRecord(File file) throws Exception {
		RandomAccessFile acf = new RandomAccessFile(file,"rw");
		FileChannel channel = acf.getChannel();
		MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		int count = buf.getInt();
		List<SerialObject> list = new ArrayList<SerialObject>();
		for(int i=0;i<count;i++){
			int len = buf.getInt();
			byte[] bb = new byte[len];
			buf.get(bb);
			
			ByteArrayInputStream in = new ByteArrayInputStream(bb);
			ObjectInputStream stream = new ObjectInputStream(in);
			SerialObject obj = (SerialObject)stream.readObject();
			obj.setData(bb);
			list.add(obj);
			stream.close();
			in.close();
		}
		BufferHelper.ummap(buf);
		channel.close();
		return list;
	}
	
	private SpliterLayerIndex[] getSubLayers(FileChannel channel,File file) throws Exception {
		MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,0, 8);
		buf.getInt();
		int subLayerCount = buf.getInt(4);
		
		SpliterLayerIndex[] layers = new SpliterLayerIndex[subLayerCount];
		int off = 0;
		for(int i=0;i<subLayerCount;i++){
			buf.getInt();
			double xmin = buf.getDouble();
			double ymin = buf.getDouble();
			double xmax = buf.getDouble();
			double ymax = buf.getDouble();
			buf.getInt();
			int nameLength = buf.getInt();
			byte[] dst = new byte[nameLength];
			buf.get(dst);
			
			SpliterLayerIndex layer = new SpliterLayerIndex(new String(dst,"utf-8"),xmin,ymin,xmax,ymax,file);
			layers[off++] = layer;
		}
		BufferHelper.ummap(buf);
		return layers;
	}
	
	private int getRecordCount(File file) throws Exception {
		FileChannel channel = getChannel(file,"r");
		if(channel.size()==0){
			return 0;
		}
		MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, 4);
		int count = buf.getInt();
		BufferHelper.ummap(buf);
		return count;
	}
	
	private FileChannel getChannel(File f,String mode) throws Exception {
		RandomAccessFile acf = new RandomAccessFile(f,"r");
		return acf.getChannel();
	}

	@Override
	public boolean stop() throws Exception {
		return doStop();
	}

	protected abstract Disk getDisk() throws Exception;
	
	protected abstract boolean doInit() throws Exception;
	
	protected abstract boolean doStart() throws Exception;
	
	protected abstract boolean doStop() throws Exception;

	private class SpliterLayerIndex {
		
		private double xmax;
		
		private double ymax;
		
		private double xmin;
		
		private double ymin;
		
		private String subLayerID;

		private File parentFile;
		
		public SpliterLayerIndex(String subLayerID,double xmin,double ymin,double xmax,double ymax,File parentFile) {
			this.subLayerID = subLayerID;
			this.xmax = xmax;
			this.ymax = ymax;
			this.xmin = ymin;
			this.ymin = ymin;
			this.parentFile = parentFile;
		}
		
		public boolean makeLayer() throws Exception {
			File file = new File(parentFile,subLayerID);
			if(!file.exists()){
				return file.createNewFile();
			}
			return true;
		}
		
		public double getXmax() {
			return xmax;
		}

		public double getYmax() {
			return ymax;
		}

		public double getXmin() {
			return xmin;
		}

		public double getYmin() {
			return ymin;
		}

		public String getSubLayerID() {
			return subLayerID;
		}

		public File getFile() {
			return parentFile;
		}
		
	}
}
