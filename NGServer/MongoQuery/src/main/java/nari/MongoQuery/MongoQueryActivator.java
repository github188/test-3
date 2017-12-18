package nari.MongoQuery;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonValue;
import org.opengis.util.CodeList;

import nari.Dao.interfaces.DbAdaptor;
import nari.MongoQuery.MapService.Service.interfaces.MapService;
import nari.MongoQuery.MapService.Service.impl.MapServiceImpl;
import nari.MongoQuery.QueryService.Service.impl.QueryServiceImpl;
import nari.MongoQuery.QueryService.Service.interfaces.QueryService;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.model.TableName;
import nari.model.device.ModelService;
import nari.model.symbol.SymbolAdapter;


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

@ActivatorReg(name="MongoQueryActivator")
public class MongoQueryActivator implements Activator,TrackerListener{
	
	public static Map<String, String> tables;// <classid-modelname>
	public static Map<Integer, Integer> RELATIONS;// <modelid-classid>
	public static Map<String, Map<String, String>> ALIAS;//<tclassid,<fieldname,fieldalias>>;
	public static Map<String, String> CODEDEFID; //<codedefid, codename>
	public static Map<String, String> PSRTYPE;   // 设备类型
	public static Map<String, Map<String, Integer>> FIELDTYPES;
	public static SymbolAdapter symbolAdapter; 
	public static DbAdaptor dbAdaptor;
	public static ModelService modelService = ModelService.NONE;
	
	public static MongoConfig mongoConfig = null;
	
	private BundleContext context;
	public enum ServiceKind {
		getVectorMap, queryByCondition, queryByStationId, spatialQuery
	};
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(DbAdaptor.class, this);
		context.getServiceTracker().track(SymbolAdapter.class, this);
		context.getServiceTracker().track(ModelService.class, this);
		return true;
	}
	@Override
	public boolean start(BundleContext context) throws BundleException {
		this.context = context;
		mongoConfig = initMongoConfig();
		
		List<String> collectionNames = MongoDBUtil.instance.getDefaultCollectionNames(); 	// by pang
		FIELDTYPES = MongoDBUtil.instance.getSchemas(collectionNames);					// by pang
		
		ExportConfig config = new ExportConfig(){
		
			
			
			@Override
			public ServiceType getServiceType() {
				// TODO Auto-generated method stub
				return ServiceType.HTTP;
			}
			
		};

		
		context.export(MapService.class,new Provider<MapService>() {

			@Override
			public MapService get() throws BundleException {
				// TODO Auto-generated method stub
				return new MapServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(MapService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return  Version.defaultVersion();
			}


			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				// TODO Auto-generated method stub
				return null;
			}
			
			
		}, config);
		
		context.export(QueryService.class,new Provider<QueryService>() {

			@Override
			public QueryService get() throws BundleException {
				// TODO Auto-generated method stub
				return new QueryServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(QueryService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return  Version.defaultVersion();
			}


			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				// TODO Auto-generated method stub
				return null;
			}
			
			
		}, config);
		
		return true;
	}
	@Override
	public <T> void serviceAdd(ServiceReference<T> ref, BundleContext context)
			throws BundleException {
//		mongoConfig = initMongoConfig();
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			DbAdaptor dbAdaptor = (DbAdaptor)dbins;
			tables = new HashMap<String, String>();
			// 电压等级转义List
			String sql = "select classid, modelname from " + TableName.CONF_MODELMETA;
			List<Map<String, Object>> codeFieldList = null;
			try {
				codeFieldList = dbAdaptor.findAllMap(sql);
				if(codeFieldList == null || codeFieldList.isEmpty()){
					System.out.println("no data, query database error!");
				}
			} catch (SQLException e) {
				System.out.println("数据库查询出错");
				e.printStackTrace();
			}
			int size = codeFieldList.size();
			for(int i = 0; i < size; ++i){
				String classid = String.valueOf(codeFieldList.get(i).get("classid"));
				String modelname = (String)codeFieldList.get(i).get("modelname");
				tables.put(classid, modelname);
			}
			
			sql = "select classid, modelid from " + TableName.CONF_MODELMETA;
			codeFieldList.clear();
			codeFieldList = null;
			try {
				codeFieldList = dbAdaptor.findAllMap(sql);
				if(codeFieldList == null || codeFieldList.isEmpty()){
					System.out.print("no data, query database error!");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("数据库查询出错");
				e.printStackTrace();	
			}
			RELATIONS = new HashMap<Integer, Integer>();
			size = codeFieldList.size();
			for(int i = 0; i < size; ++i){
				int classid = Integer.parseInt(codeFieldList.get(i).get("classid").toString());
				int modelid = Integer.parseInt(codeFieldList.get(i).get("modelid").toString());
				RELATIONS.put(modelid, classid);
			}
			
			codeFieldList.clear();
			codeFieldList = null;
			sql = "select classid, fieldname, fieldalias from " + TableName.CONF_FIELDDEFINITION;
			try{
				codeFieldList = dbAdaptor.findAllMap(sql);
				if(codeFieldList == null || codeFieldList.isEmpty()){
					System.out.print("no data, query database error!");
				}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("数据库查询出错");
				e.printStackTrace();	
			}
			ALIAS = new HashMap<String, Map<String, String>>();
			size = codeFieldList.size();
			for(int i = 0; i < size; ++i){
				String classid = String.valueOf(codeFieldList.get(i).get("classid"));
				String fieldname = String.valueOf(codeFieldList.get(i).get("fieldname"));
				String fieldAlias = String.valueOf(codeFieldList.get(i).get("fieldalias"));
				if(fieldAlias.equals("null")){
					fieldAlias = fieldname;
				}
				if(ALIAS.containsKey(classid)){
					ALIAS.get(classid).put(fieldname, fieldAlias);
				}else{
					Map<String, String> map = new HashMap<String, String>();
					map.put(fieldname, fieldAlias);
					ALIAS.put(classid, map);
				}
			}
			
			codeFieldList.clear();
			codeFieldList = null;
			sql = "select codedefid, codename from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401";
			try{
				codeFieldList = dbAdaptor.findAllMap(sql);
				if(codeFieldList == null || codeFieldList.isEmpty()){
					System.out.print("no data, query database error!");
				}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("数据库查询出错");
				e.printStackTrace();	
			}
			size = codeFieldList.size();
			CODEDEFID = new HashMap<>();
			for(int i = 0; i < size; ++i){
				String codedefid = codeFieldList.get(i).get("codedefid").toString();
				String codename = codeFieldList.get(i).get("codename").toString();
				CODEDEFID.put(codedefid, codename);
			}
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symbolAdapter = (SymbolAdapter)symIns;
		}
		
		T dbIns =(T)ref.get();
		if(dbIns instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbIns;
		}
		
	}
	@Override
	public <T> void serviceModify(ServiceReference<T> ref, BundleContext context)
			throws BundleException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public <T> void serviceRemove(ServiceReference<T> ref, BundleContext context)
			throws BundleException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean stop(BundleContext context) throws BundleException {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	private MongoConfig initMongoConfig(){
		InputStream stream = context.getResourceAsStream("config/mongo.xml");
		ConfigSearch searcher = new ConfigSearchService();
		MongoConfig mongo = searcher.loadConfigCache("mongo",stream,"xml",MongoConfig.class);
		if(mongo==null){
			return MongoConfig.NONE;
		}
		return mongo;
	}
	
}
