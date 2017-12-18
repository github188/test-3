package nari.BaseService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nari.BaseService.Service.impl.BaseServiceImpl;
import nari.BaseService.Service.interfaces.BaseService;
import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.model.device.ModelService;
import nari.model.user.UserService;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.referencing.crs.CRSAuthorityFactory;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.ExportConfig;
import com.application.plugin.Provider;
import com.application.plugin.ServiceType;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.tracker.TrackerListener;
import com.vividsolutions.jts.geom.Geometry;

@ActivatorReg(name="BaseServiceActivator")
public class BaseServiceActivator implements Activator,TrackerListener{

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public static ModelService modelService = ModelService.NONE;
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	public static UserService userService = UserService.NONE;
	
	public static Map<String,String> areaCacheMap = new HashMap<String,String>();
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(UserService.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		ExportConfig config = new ExportConfig(){

			@Override
			public ServiceType getServiceType() {
				return ServiceType.HTTP;
			}
			
		};
		context.export(BaseService.class, new Provider<BaseService>() {

			@Override
			public BaseService get() throws BundleException {
				return new BaseServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(BaseService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return  Version.defaultVersion();
			}

			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				return null;
			}
			
		}, config);
		
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		return true;
	}

	@Override
	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T userIns = (T)ref.get();
		if(userIns instanceof UserService){
			userService = (UserService)userIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;			
			//各地区地理信息缓存
			System.out.println("开始加载地区缓存。");
			long t1 = System.currentTimeMillis();
			areaCacheMap = cacheArea(context);
			long t2 = System.currentTimeMillis();
			System.out.println("地区缓存加载完成，共用时：" + (t2 - t1) / 1000 + "秒。");
		}
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof UserService){
			userService = (UserService)ins;
		}
		
		T userIns = (T)ref.get();
		if(userIns instanceof UserService){
			userService = (UserService)userIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
		}
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof UserService){
			userService = null;
		}
		
		T userIns = (T)ref.get();
		if(userIns instanceof UserService){
			userService = null;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
		}
	}

	
	private Map<String,String> cacheArea(BundleContext context){
		Map<String,String> map = new HashMap<String,String>();
		InputStream in = null;
		URL url = context.gettResourceURL("config/areaCache");
		URI uri = null;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File[] files = new File(uri).listFiles();
		
		int hh = 0;
		for(File geofile :files){	//对每个地理文件进行处理
			InputStreamReader inReader = null;
			try {
				try {
					inReader = new InputStreamReader(new FileInputStream(geofile),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			BufferedReader buReader = new BufferedReader(inReader);
			StringBuffer fileString = new StringBuffer();
			try {
				String target = "";
				while((target = buReader.readLine()) != null){
					fileString.append(new String(target.getBytes()));
				}	//读完
				
//				if(hh == 3){
////					hh++;
////					continue;
//					System.out.println(fileString.substring(310, 320));	//311
//				}
				
//				FeatureJSON fj = new FeatureJSON();
//				FeatureCollection fc = fj.readFeatureCollection(new FileInputStream(geofile));
//				FeatureIterator it = fc.features();
//				while(it.hasNext()){
//					Feature feature = it.next();
//					Property pro = feature.getProperty("ISC_ID");
//					System.out.println(String.valueOf(pro.getValue()));
//				}
				
				
//				GeometryJSON ss = new GeometryJSON();
//				Reader reader = new StringReader(fileString.toString());
//				Geometry geo = ss.read(reader);
//				System.out.println(geo.getGeometryType());
//				
				//变成geoJSON
				JSONObject geojson = new JSONObject(fileString.toString());
				JSONArray features = geojson.getJSONArray("features");
				for(int i=0;i<features.length();i++){
					JSONObject featureJSON = features.getJSONObject(i);
					JSONObject propertiesJSON = featureJSON.getJSONObject("properties");
					String isc_id = "";
					if(!propertiesJSON.isNull("ISC_ID")){
						isc_id = propertiesJSON.getString("ISC_ID");
					}
					JSONObject geometryJSON = featureJSON.getJSONObject("geometry");
					String geometryString = geometryJSON.toString();	//geojson形式string{geometry:...}
					map.put(isc_id, geometryString);
				}	//所有features数据组装完成
			} catch (IOException e) {
				e.printStackTrace();
			}finally{	
				try {
					inReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
}
