package nari.SpatialIndex.searcher;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import nari.SpatialIndex.buffer.BufferHelper;
import nari.SpatialIndex.index.Boundary;
import nari.SpatialIndex.index.Grid;
import nari.SpatialIndex.loader.Disk;
import nari.SpatialIndex.loader.LayerStream;

public class GridIndexLayerHeader extends AbstractLayerHeader {

	private Disk disk;
	
	private Grid grid;
	
	private String indexId = "";
	
	private File indexFile;
	
	public GridIndexLayerHeader(Grid grid,Disk disk) {
		this.grid = grid;
		this.disk = disk;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doInit() throws Exception {
		indexId = disk.getFile().getName()+"$index";
		File pfile = disk.getFile().getParentFile();
		indexFile = new File(pfile,indexId);
		if(!indexFile.exists()){
			indexFile.createNewFile();
		}
		
		RandomAccessFile file = new RandomAccessFile(indexFile,"rw");
		FileChannel ch = file.getChannel();
		FileLock lock = null;
		
		int count = 0;
		do{
			try{
				lock = ch.tryLock(4,8, false);
				if(lock==null){
					continue;
				}
				MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_WRITE, 4,8);
				if(buf.hasRemaining()){
					count = buf.getInt();
				}
				BufferHelper.ummap(buf);
			} catch (Exception e){
				Thread.sleep(10);
			} finally{
				if(lock!=null){
					ch.close();
				}
			}
		}while(lock==null);
		
		if(count<=0){
			initIndex();
		}
		
	}
	
	private void initIndex() throws Exception{
		File pfile = disk.getFile().getParentFile();
		File f = new File(pfile,getLayerID()+"$base");
		if(f.exists()){
			return;
		}
		Boundary bound = grid.boundary();
		SubIndexLayer layer = new GridSubIndexLayer(bound.getMinX(),bound.getMinY(),bound.getMaxX(),bound.getMaxY(),0,getLayerID()+"$base",getDisk(),this);
		
		byte[] bytes = LayerStream.create(new SubIndexLayer[]{layer});
		
		RandomAccessFile file = new RandomAccessFile(indexFile,"rw");
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
			} catch (Exception e){
				Thread.sleep(10);
			} finally{
				if(lock!=null){
					ch.close();
				}
			}
		}while(lock==null);
	}
	
	@Override
	public String getPath() throws Exception{
		return disk.getFile().getParent();
	}

	@Override
	public String getLayerID()  throws Exception{
		return disk.getFile().getName();
	}
	
	@Override
	public Disk getDisk()  throws Exception{
		return disk;
	}

	@Override
	public String getIndexName() {
		return indexId;
	}
	
	@Override
	protected File getFile() throws Exception {
		return indexFile;
	}
	
	@Override
	protected MappedByteBuffer map(int offset, int length) throws Exception {
		RandomAccessFile file = new RandomAccessFile(indexFile,"rw");
		FileChannel ch = file.getChannel();
		FileLock lock = null;
		
		do{
			try{
				lock = ch.tryLock(offset, length, false);
				if(lock==null){
					continue;
				}
				return ch.map(FileChannel.MapMode.READ_WRITE, offset, length);
			} catch (Exception e){
				Thread.sleep(10);
			} finally{
				if(lock!=null){
					ch.close();
				}
			}
		}while(lock==null);
		
		return null;
	}
	
}
