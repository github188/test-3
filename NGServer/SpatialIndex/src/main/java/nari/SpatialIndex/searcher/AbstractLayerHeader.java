package nari.SpatialIndex.searcher;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.SpatialIndex.buffer.BufferHelper;
import nari.SpatialIndex.geom.CoordinateSequence;
import nari.SpatialIndex.geom.DefaultCoordinateSequence;
import nari.SpatialIndex.geom.DefaultPolygon;
import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.loader.LayerStream;

public abstract class AbstractLayerHeader implements LayerHeader {

	private int subLayerCount;
	
	private final ConcurrentMap<String,SubIndexLayer> layerMap = new ConcurrentHashMap<String,SubIndexLayer>();
	
	private final AtomicBoolean init = new AtomicBoolean(false);
	
	public AbstractLayerHeader(){
		
	}
	
	@Override
	public int getLayerCount() throws Exception {
		return subLayerCount;
	}

	@Override
	public SubIndexLayer[] getSubLayers() throws Exception {
		SubIndexLayer[] arr = new SubIndexLayer[layerMap.size()];
		arr = layerMap.values().toArray(arr);
		return arr;
	}

	@Override
	public SubIndexLayer[] match(Geometry geom) throws Exception {
		return null;
	}
	
	private void initSubLayer(MappedByteBuffer buf) throws Exception{
		int off = 0;
		for(int i=0;i<getLayerCount();i++){
			int blockSize = buf.getInt();
			byte[] dst = new byte[blockSize-4];
			buf.get(dst);
			off = off + blockSize;
			SubIndexLayer layer = new GridSubIndexLayer(dst,getDisk(),this);
			layer.makeLayer();
			addIndex(layer.getSubLayerID(), layer);
		}
		
		BufferHelper.ummap(buf);
	}
	
	protected void init() throws Exception {
		if(init.get()){
			return ;
		}
		doInit();
		
		MappedByteBuffer buf = map(0, 8);
		if(!buf.hasRemaining()){
			return;
		}
		int len = buf.getInt(0);
		subLayerCount = buf.getInt(4);
		BufferHelper.ummap(buf);
		
		
		RandomAccessFile file = new RandomAccessFile(getFile(),"rw");
		FileChannel ch = file.getChannel();
		FileLock lock = null;
		
		do{
			try{
				lock = ch.tryLock(8, len-8, false);
				if(lock==null){
					continue;
				}
				
				MappedByteBuffer bf = ch.map(FileChannel.MapMode.READ_WRITE, 8, len-8);
				
				initSubLayer(bf);
				
				BufferHelper.ummap(bf);
			} catch (Exception e){
				Thread.sleep(10);
			} finally{
				if(lock!=null){
					ch.close();
				}
			}
		}while(lock==null);
		
		init.compareAndSet(false, true);
	}
	
	@Override
	public boolean write(Geometry geom, byte[] data) throws Exception {
		SubIndexLayer layer = null;
		for(Map.Entry<String, SubIndexLayer> entry:layerMap.entrySet()){
			layer = entry.getValue();
			CoordinateSequence pyseq = new DefaultCoordinateSequence(new double[]{layer.getXMin(),layer.getYMin(),layer.getXMin(),layer.getYMax(),layer.getXMax(),layer.getYMax(),layer.getXMax(),layer.getYMin(),layer.getXMin(),layer.getYMin()});
			Geometry g = new DefaultPolygon(pyseq);
			if(!geom.intersects(g)){
				continue;
			}
			layer.write(geom,data);
		}
		
		return true;
	}
	
	@Override
	public boolean updateIndex(String layerId,SubIndexLayer layer) throws Exception {
		layerMap.putIfAbsent(layerId, layer);
		return true;
	}
	
	@Override
	public boolean addIndex(String layerId,SubIndexLayer layer) throws Exception {
		layerMap.putIfAbsent(layerId, layer);
		return true;
	}
	
	@Override
	public boolean removeIndex(String layerId) throws Exception {
		layerMap.remove(layerId);
		return true;
	}
	
	@Override
	public boolean store() throws Exception {
		byte[] bytes = LayerStream.create(getSubLayers());
		
		RandomAccessFile file = new RandomAccessFile(getFile(),"rw");
		FileChannel ch = file.getChannel();
		FileLock lock = null;
		
		do{
			try{
				lock = ch.tryLock(0,bytes.length, false);
				if(lock==null){
					continue;
				}
				
				MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_WRITE, 0,bytes.length);
				buf.put(bytes);
				BufferHelper.ummap(buf);
			} catch (Exception e){
				Thread.sleep(10);
			} finally{
				if(lock!=null){
					ch.close();
				}
			}
		}while(lock==null);
		
		return true;
	}
	
	protected abstract MappedByteBuffer map(int offset,int length) throws Exception;
	
	protected abstract void doInit() throws Exception;
	
	protected abstract File getFile() throws Exception;
}
