package nari.MapService;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MapService.bean.LineObject;
import nari.MemCache.AddCacheTicket;
import nari.MemCache.Cache;
import nari.MemCache.CacheHandler;
import nari.MemCache.CacheListener;
import nari.MemCache.CacheTicket;
import nari.MemCache.HandlerChain;
import nari.MemCache.Key;
import nari.MemCache.NextHandler;
import nari.MemCache.Shape;
import nari.MemCache.config.CacheInitAttribute;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.model.TableName;
import oracle.spatial.geometry.JGeometry;

import org.apache.commons.lang.StringUtils;

import com.application.plugin.BundleContext;

public class LineGeometryCacheListener implements CacheListener {

	private BundleContext context;
	
	private boolean offsetValid = false;
	
	private double offSetX = 0;
	
	private double offSetY = 0;
	
	private DbAdaptor dbAdaptor;
	
	private List<String> list = new ArrayList<String>();
	
	public LineGeometryCacheListener(BundleContext context,DbAdaptor dbAdaptor) {
		this.context = context;
		this.dbAdaptor = dbAdaptor;
		
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Key key, Cache cache) throws Exception {
		if(key.getKey().startsWith("10100000")){
			
//			cache.registFieldProcesser("shape", new FieldProcesser() {
//				
//				@Override
//				public Object process(Object obj, Object att) throws Exception {
//					return createShape(obj,String.valueOf(att));
//				}
//			});
			
			cache.getHandlerChain().addFirst("mergerHandler", new CacheHandler() {
				
				@Override
				public void onPreRemove(HandlerChain arg0, String arg1) throws Exception {
					
				}
				
				@Override
				public void onPreAdd(HandlerChain arg0, String arg1) throws Exception {
					
				}
				
				@Override
				public void onPostRemove(HandlerChain arg0, String arg1) throws Exception {
					
				}
				
				@Override
				public void onPostAdd(HandlerChain arg0, String arg1) throws Exception {
					
				}
				
				@Override
				public boolean handle(CacheTicket ticket, NextHandler next) throws Exception {
					boolean suc = doHandle(ticket);
					if(!suc){
						return true;
					}
					return next.handle(ticket);
				}

			});
		}
		
	}
	
	private boolean doHandle(CacheTicket ticket) throws Exception{
		if(ticket instanceof AddCacheTicket){
			AddCacheTicket tic = (AddCacheTicket)ticket;
			Object obj = tic.getPair();
			
			Field oidField = obj.getClass().getDeclaredField("oid");
			oidField.setAccessible(true);
			Object val = oidField.get(obj);
			String dxdOid = val==null?"":String.valueOf(oidField.get(obj));
			if(StringUtils.isEmpty(dxdOid)){
				return false;
			}
			
//			String ssxl = getDxd(dxdOid);
			Field ssxlField = obj.getClass().getDeclaredField("ssxl");
			ssxlField.setAccessible(true);
			
			String ssxl = ssxlField.get(obj)==null?"":String.valueOf(ssxlField.get(obj));
			if(StringUtils.isEmpty(ssxl)){
				return false;
			}
//			Device dev = getXl(ssxl);
			
			LineObject lineObj = MapServiceActivator.getLine(ssxl);
			
			if(lineObj==null){
				return true;
			}
			
			String xlOid = String.valueOf(lineObj.getOid());
			if(list.contains(xlOid)){
				return false;
			}
			
			list.add(xlOid);
			JGeometry geom = MapServiceActivator.getGeometry(xlOid);
			if(geom==null){
				return false;
			}
			
			Field sbzlxField = obj.getClass().getDeclaredField("sbzlx");
			sbzlxField.setAccessible(true);
			int sbzlx = Integer.parseInt(String.valueOf(lineObj.getSbzlx()));
			sbzlxField.set(obj, sbzlx);
			
			oidField.set(obj, Integer.parseInt(xlOid));
//			
			Field sbmcField = obj.getClass().getDeclaredField("sbmc");
			sbmcField.setAccessible(true);
			String sbmc = String.valueOf(lineObj.getSbmc());
			sbmcField.set(obj, sbmc);
			
			Field shapeField = obj.getClass().getDeclaredField("shape");
			shapeField.setAccessible(true);
			Shape shape = createShape(geom);
			shapeField.set(obj, shape);
		}
		
		return true;
	}
	
//	private String getDxd(String oid){
//		DeviceModel model = DeviceModel.NONE;
//		try {
//			model = MapViewerActivator.modelService.fromClass("101000", false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
//		Expression exp = builder.equal(builder.getRoot().get("oid", String.class), oid);
//		
//		ResultSet set = null;
//		try {
//			set = model.search(new String[]{"ssxl"}, exp, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if(set==null){
//			return null;
//		}
//		
//		Device dev = set.getSingle();
//		if(dev==null){
//			return null;
//		}
//		
//		return dev.getValue("ssxl")==null?"":String.valueOf(dev.getValue("ssxl"));
//	}
//	
//	private Device getXl(String ssxl){
//		
//		DeviceModel model = DeviceModel.NONE;
//		try {
//			model = MapViewerActivator.modelService.fromClass("100000", false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
//		Expression exp = builder.equal(builder.getRoot().get("oid", String.class), ssxl);
//		
//		ResultSet set = null;
//		try {
//			set = model.search(null, exp, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if(set==null){
//			return null;
//		}
//		
//		return set.getSingle();
//	}
	
	private Shape createShape(JGeometry geom){
		Shape shape = new Shape();
		shape.setType(geom.getType());
		shape.setElementInfo(geom.getElemInfo());
		if(geom.getType()==1){
			if(offsetValid){
				double[] mbr = geom.getMBR();
				double[] newMbr = new double[mbr.length];
				for(int k=0;k<mbr.length;k=k+2){
					newMbr[k] = mbr[k] - offSetX;
					newMbr[k+1] = mbr[k+1] - offSetY;
				}
				
				shape.setCoordinates(newMbr);
			}else{
				shape.setCoordinates(geom.getMBR());
			}
		}else{
			if(offsetValid){
				double[] coords = geom.getOrdinatesArray();
				double[] newCoords = new double[coords.length];
				for(int k=0;k<coords.length;k=k+2){
					newCoords[k] = coords[k] - offSetX;
					newCoords[k+1] = coords[k+1] - offSetY;
				}
				
				shape.setCoordinates(newCoords);
			}else{
				shape.setCoordinates(geom.getOrdinatesArray());
			}
		}
		return shape;
	}
	
	public void init() throws Exception{
		InputStream stream = context.getResourceAsStream("config/cache_mapservice.xml");
		
		ConfigSearch searcher = new ConfigSearchService();
		CacheInitAttribute att = searcher.loadConfigCache("cache_mapservice",stream,"xml",CacheInitAttribute.class);
		offsetValid = att.isOffsetValid();
		
		if(offsetValid){
			String offSql = "select param_name, param_value from " + TableName.CONF_SERVICEPARAMS + " where param_name='OFFSETX' or param_name='OFFSETY'";
			List<Map<String,Object>> omaps = dbAdaptor.findAllMap(offSql);
			
			if(omaps!=null){
				for(Map<String,Object> m:omaps){
					if("OFFSETX".equalsIgnoreCase(String.valueOf(m.get("param_name")))){
						offSetX = m.get("param_value")==null?0:Double.parseDouble(String.valueOf(m.get("param_value")));
					}else if("OFFSETY".equalsIgnoreCase(String.valueOf(m.get("param_name")))){
						offSetY = m.get("param_value")==null?0:Double.parseDouble(String.valueOf(m.get("param_value")));
					}
				}
			}
		}
	}
}
