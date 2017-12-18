package nari.SpatialIndex;

import java.io.File;

import nari.Dao.interfaces.DbAdaptor;
import nari.SpatialIndex.handler.GridInstallHandler;
import nari.SpatialIndex.index.IndexAttribute;
import nari.SpatialIndex.index.IndexType;

import org.apache.cxf.helpers.FileUtils;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.BundleContext;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.tracker.TrackerListener;

@ActivatorReg(name="SpatialIndexActivator")
public class SpatialIndexActivator implements Activator,TrackerListener {

	public static DbAdaptor dbAdaptor = null;
	
	
	@Override
	public boolean init(BundleConfig config) throws BundleException {
		config.getServiceTracker().track(DbAdaptor.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		return true;
	}

	@Override
	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = ref.get();
		if(ins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)ins;
			
			File file = new File("D:\\index\\");
			if(file.exists()){
				FileUtils.removeDir(file);
			}
			
			file.mkdir();
			long s = System.currentTimeMillis();
			IndexAttribute att = new IndexAttribute("spatialQuery","D:\\index",IndexType.GRID);
			try {
				Index.install(att, new GridInstallHandler());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			long e = System.currentTimeMillis();
			System.out.println(e-s+"ms");
			
			
			
			
			
//			File f = new File("D:\\index");
//			File[] fs = f.listFiles();
//			int c = 0;
//			
//			Map<String,String> map = new HashMap<String,String>();
//			
//			for(File ff:fs){
//				File[] list = ff.listFiles();
//				for(File lf:list){
//					if(lf.getName().contains("index")){
//						continue;
//					}
//					
//					try {
//						RandomAccessFile acf = new RandomAccessFile(lf,"rw");
//						FileChannel channel = acf.getChannel();
//						MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
//						int count = buf.getInt();
//						c = c+count;
//						while(buf.hasRemaining()){
//							int len = buf.getInt();
//							byte[] bb = new byte[len];
//							buf.get(bb);
//							
//							ByteArrayInputStream in = new ByteArrayInputStream(bb);
//							ObjectInputStream stream = new ObjectInputStream(in);
//							SerialObject obj = (SerialObject)stream.readObject();
//							
//							stream.close();
//							in.close();
//							
//							map.put(String.valueOf(obj.getValue("sbzlx"))+String.valueOf(obj.getValue("oid")), "1");
//						}
//						BufferHelper.ummap(buf);
//						channel.close();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//						System.out.println(lf.getPath());
//					}
//				}
//			}
//			System.out.println(map.size());
			
			
			
			
			
			
		}
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
	}
}
