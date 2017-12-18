package nari.MainGridService;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.DbLinkBean.CharSet;
import nari.MainGridService.DbLinkBean.DBLink;
import nari.MainGridService.Service.impl.MainGridServiceImpl;
import nari.MainGridService.Service.interfaces.MainGridService;
import nari.MemCache.CacheService;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.bean.YXDWMessage;

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

@ActivatorReg(name="MainGridServiceActivator")
public class MainGridServiceActivator implements Activator,TrackerListener{
	public static ModelService modelService = ModelService.NONE;
	public static GeometryService geoService = GeometryService.NONE;
	public static DbAdaptor dbAdaptor215 = DbAdaptor.NONE;
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	public static SymbolAdapter symboladapter = SymbolAdapter.NONE;
//	public static CacheService cacheService = null;
	public static Map<String, String> DYDJCodeMap = new HashMap<String, String>();	//鐢靛帇绛夌骇缂撳瓨
	public static ResultSet XLset = ResultSet.NONE;	//瀵肩嚎娈电粍鎴愭墍鏈夌嚎璺紦瀛�
	public static Map<String, String> BDZReflectMap = new HashMap<String, String>();	//鍙樼數绔欏悕瀛楁槧灏勭紦瀛�
	public static Map<String,YXDWMessage> YXDWMap= new HashMap<String,YXDWMessage>();	//杩愯鍗曚綅涓庣綉鐪佺紦瀛�
	public static Map<String,String> SBIDtoZZGTOidMap = new HashMap<String, String>();	//缁堟鏉嗗oid缂撳瓨
	public static String charSetConfig = "";	//鑾峰彇瀛楃闆嗛厤缃�
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(GeometryService.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this,AttributeKey.key(DbAdaptor.class, Version.version("pms_db")));
		context.getServiceTracker().track(SymbolAdapter.class, this);
		context.getServiceTracker().track(CacheService.class, this);
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
		context.export(MainGridService.class, new Provider<MainGridService>() {

			@Override
			public MainGridServiceImpl get() throws BundleException {
				return new MainGridServiceImpl();
			}
			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(MainGridService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return Version.defaultVersion();
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
			
			//绾胯矾缂撳瓨
			DeviceModel model = DeviceModel.NONE;
			try {
				model = modelService.fromClass("101000", false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Expression dydjExpression = Expression.NONE;
			CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
			String[] DYDJ = new String[]{"85","37"};
			dydjExpression = builder.in(builder.getRoot().get("dydj", String.class), DYDJ);
			try {
				XLset = model.search(null, dydjExpression, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		
//		T cains = (T)ref.get();
//		if(cains instanceof CacheService){
//			cacheService = (CacheService)cains;
//		}
		
		T dbins = (T)ref.get();
		
		//鍒ゆ柇鏄惁涓�15鏁版嵁婧�
		if(dbins instanceof DbAdaptor && ref.getKey().equals(AttributeKey.key(DbAdaptor.class, Version.version("pms_db"))) ){
			dbAdaptor215 = (DbAdaptor)dbins;
			//鍙樼數绔欏悕瀛楃紦瀛�
			String BDZSql = "select obj_id,ysz from zwgk_pz_sjys";
			List<Map<String, Object>> BDZList = null;
			long a =System.currentTimeMillis();
			try {
				BDZList = dbAdaptor215.findAllMap(BDZSql);
				if(BDZList == null || BDZList.isEmpty()){
					System.out.println("鏃犳暟鎹�鏁版嵁搴撶數鍘嬭浆鎰忔煡璇㈠嚭閿�);
				}
			} catch (SQLException e1) {
				System.out.println("鏁版嵁搴撴煡璇㈠嚭閿�);
				e1.printStackTrace();
			}
			// 灏嗗叾杞负Map(sbid锛氭槧灏勫悕)(闃叉鍚庨潰閬嶅巻)
			for (int i = 0; i < BDZList.size(); i++) {
				BDZReflectMap.put(
				String.valueOf(BDZList.get(i).get("obj_id")),
				String.valueOf(BDZList.get(i).get("ysz")));
			}
			long b =System.currentTimeMillis();
			System.out.println("鍒濆鍖栫數绔欏悕瀛楁槧灏勪俊鎭�"+(b-a)+"ms");
			
			
			
			//鍒ゆ柇鏄惁涓�16鏁版嵁婧�
		}else if(dbins instanceof DbAdaptor && ref.getKey().equals(AttributeKey.key(DbAdaptor.class, Version.defaultVersion()))){
			dbAdaptor = (DbAdaptor)dbins;
			
			//杩愯鍗曚綅缂撳瓨
			String sql = "select a.bmmc,a.isc_id,b.bmmc as wsgs from ISC_SPECIALORG_UNIT_LOCEXT a left join ISC_SPECIALORG_UNIT_LOCEXT b on a.sswsid = b.isc_id";
			List<Map<String,Object>> YXDWList = null;
			long a =System.currentTimeMillis();
			try {
				YXDWList = dbAdaptor.findAllMap(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//灏嗗叾鏀惧嚭map涓�Id:YXDWMessage)
			for(int i=0;i<YXDWList.size();i++){
				Map<String,Object> eachMap = YXDWList.get(i);
				YXDWMessage yxdwMessage = new YXDWMessage();
				String iscId = String.valueOf(eachMap.get("isc_id"));
				String bmmc = String.valueOf(eachMap.get("bmmc"));
				String wsgs = String.valueOf(eachMap.get("wsgs"));
				yxdwMessage.setBmmc(bmmc);
				yxdwMessage.setWsgs(wsgs);
				YXDWMap.put(iscId, yxdwMessage);
			}
			long b =System.currentTimeMillis();
			System.out.println("鍒濆鍖栬繍琛屽崟浣嶇綉鐪佷俊鎭�"+(b-a)+"ms");
			
			
			// 鐢靛帇绛夌骇杞箟List
			String DYDJsql = "select codedefid,codename from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401 ";
			List<Map<String, Object>> codeFieldList = null;
			a =System.currentTimeMillis();
			try {
				codeFieldList = dbAdaptor.findAllMap(DYDJsql);
				
				if(codeFieldList == null || codeFieldList.isEmpty()){
					System.out.println("鏃犳暟鎹�鏁版嵁搴撶數鍘嬭浆鎰忔煡璇㈠嚭閿�);
				}
			} catch (SQLException e1) {
				System.out.println("鏁版嵁搴撴煡璇㈠嚭閿�);
				e1.printStackTrace();
			}
			// 灏嗗叾杞负Map(浠ｇ爜锛氫腑鏂囧悕褰㈠紡)(闃叉鍚庨潰閬嶅巻)
			for (int i = 0; i < codeFieldList.size(); i++) {
				DYDJCodeMap.put(
						String.valueOf(codeFieldList.get(i).get("codedefid")),
						String.valueOf(codeFieldList.get(i).get("codename")));
			}
			DYDJCodeMap.put("0", "0");
			b =System.currentTimeMillis();
			System.out.println("鍒濆鍖栫數鍘嬬瓑绾ц浆涔変俊鎭�"+(b-a)+"ms");
			
			
			//鑾峰彇瀛楃闆嗛厤缃�
			try {
//			SAXReader reader = new SAXReader();
//			Document document = reader.read(new File("dblink.xml"));
//			DBLinkName = document.getRootElement().getStringValue();
				
				InputStream stream = context.getResourceAsStream("config/charSet.xml");
				ConfigSearch searcher = new ConfigSearchService();
				CharSet charSet = searcher.loadConfigCache("Name",stream,"xml",CharSet.class);
				charSetConfig = charSet.getMethod();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
//			//sbid鍒癦ZGToid缂撳瓨
//			String getTXOidSql = "select a.sbid,b.olddevoid from t_TX_ZWYC_YXGT a ,dev_oid_mapping b where a.oid = b.newdevoid";	//寰楀埌鏉嗗鍥惧舰oid
//			List<Map<String, Object>> GTList = null;
//			a =System.currentTimeMillis();
//			try {
//				GTList = dbAdaptor.findAllMap(getTXOidSql);
//				if(GTList == null || GTList.isEmpty()){
//					System.out.println("鏃犳暟鎹�鏁版嵁搴搒bid鍒癦ZGToid缂撳瓨鍑洪敊");
//				}
//			} catch (SQLException e) {
//				System.out.println("鏁版嵁搴撴煡璇㈠嚭閿�);
//				e.printStackTrace();
//			}
//			//灏嗗緱鍒扮殑鍊煎鍏ョ紦瀛�
//			for(Map<String, Object> eachMap:GTList){
//				SBIDtoZZGTOidMap.put(String.valueOf(eachMap.get("sbid")),String.valueOf(eachMap.get("olddevoid")));
//			}
//			b =System.currentTimeMillis();
//			System.out.println("鍒濆鍖栫粓姝㈡潌濉攐id缂撳瓨淇℃伅:"+(b-a)+"ms");
			
		}	//缂撳瓨缁撴潫
		
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		

	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		

	}
	

}

