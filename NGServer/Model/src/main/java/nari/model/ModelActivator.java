package nari.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import nari.Dao.interfaces.DbAdaptor;
import nari.MongoClient.interfaces.MongoAdaptor;
import nari.model.cluster.ModelProviderCluster;
import nari.model.cluster.ModelSymbolProviderCluster;
import nari.model.cluster.ProviderCluster;
import nari.model.device.DbProvider;
import nari.model.device.DefaultModelService;
import nari.model.device.ModelService;
import nari.model.device.MongoProvider;
import nari.model.device.SelectorProvider;
import nari.model.device.SymbolProviderCluster;
import nari.model.geometry.DefaultGeometryService;
import nari.model.geometry.GeometryService;
import nari.model.symbol.DbSymbolProvider;
import nari.model.symbol.DefaultSymbolAdapter;
import nari.model.symbol.SymbolAdapter;
import nari.model.symbol.SymbolProvider;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.Provider;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistration;
import com.application.plugin.tracker.TrackerListener;

@ActivatorReg(name="ModelActivator")
public class ModelActivator implements Activator, TrackerListener {

	/**
	 * 数据库适配器
	 */
	public static DbAdaptor dbAdaptor = null;
	
	/**
	 * MongoDB适配器
	 */
	private MongoAdaptor mongoAdapter = null;
	
	/**
	 * 要发布的模型服务
	 */
	private ServiceRegistration<ModelService> modelService = null;
	
	/**
	 * 要发布的几何服务
	 */
	private ServiceRegistration<GeometryService> geometryService = null;
	
	/**
	 * 要发布的符号服务
	 */
	private ServiceRegistration<SymbolAdapter> symbolAdapter = null;
	
	/**
	 * equipmentId与classId之间的映射关系，一个equipmentId可能会有多个classId与之对应
	 * key：equipmentId，String类型
	 * value：与之相关联的classId的列表
	 */
	private static Map<String, List<Integer>> equipClassID;
	
	/**
	 * 通过一个equId，查找对应的classId的集合
	 * @param equId
	 * @return
	 */
	public static List<Integer> getClassIdByEquId(String equId) {
		if (null == equipClassID) {
			return null;
		}
		return equipClassID.get(equId);
	}
	
	@Override
	public boolean init(BundleConfig config) throws BundleException {
		config.getServiceTracker().track(DbAdaptor.class, this);
		config.getServiceTracker().track(MongoAdaptor.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		
		// 发布几何服务
		geometryService = context.registService(GeometryService.class, new Provider<GeometryService>() {

			private final AtomicReference<GeometryService> ref = new AtomicReference<GeometryService>(GeometryService.NONE); 
			
			@Override
			public GeometryService get() throws BundleException {
				if(ref.get() == GeometryService.NONE){
					GeometryService geometryService = new DefaultGeometryService();
					ref.compareAndSet(GeometryService.NONE, geometryService);
				}
				return ref.get();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(GeometryService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return Version.defaultVersion();
			}

			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				return null;
			}
		});
		
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		
		if(geometryService!=null){
			context.unRegisterService(geometryService.getReference());
		}
		
		if(modelService!=null){
			context.unRegisterService(modelService.getReference());
		}
		
		if(symbolAdapter!=null){
			context.unRegisterService(symbolAdapter.getReference());
		}
		return true;
	}

	@Override
	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = ref.get();
		
		if(ins instanceof DbAdaptor) {
			dbAdaptor = (DbAdaptor)ins;
			
			String sql = "select classid, equipmentid from " + TableName.CONF_OBJECTMETA;
			List<Map<String, Object>> codeFieldList = null;
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
			int size = codeFieldList.size();
			equipClassID = new HashMap<String, List<Integer>>();
			
			for(int i = 0; i < size; ++i) {
				int classid = Integer.parseInt(codeFieldList.get(i).get("classid").toString());
				String equipmentid = null;
				Object objEid = codeFieldList.get(i).get("equipmentid");
				if(objEid == null){
					continue;
				}else{
					equipmentid = codeFieldList.get(i).get("equipmentid").toString();
				}
				if(equipmentid == null || equipmentid.isEmpty() || equipmentid.equalsIgnoreCase("null")){
				    continue;	
				}
				
				List<Integer> classIds = equipClassID.get(equipmentid);
				if (null == classIds) {
					classIds = new ArrayList<Integer>();
					classIds.add(classid);
					equipClassID.put(equipmentid, classIds);
				} else {
					classIds.add(classid);
				}
			}
		}
		
		if(ins instanceof MongoAdaptor)  {
			mongoAdapter = (MongoAdaptor)ins;
		}
		
		if(null != dbAdaptor && null != mongoAdapter) {
			
			// 发布模型服务
			modelService = context.registService(ModelService.class, new Provider<ModelService>() {
				private final AtomicReference<ModelService> ref = new AtomicReference<ModelService>(ModelService.NONE); 
				
				@Override
				public ModelService get() throws BundleException {
					if(ref.get() == ModelService.NONE){
						List<SelectorProvider> pros = new ArrayList<SelectorProvider>();
						
						if(dbAdaptor.isActive()){
							pros.add(new DbProvider(dbAdaptor,1));
						}
						
						if(mongoAdapter.isActive()){
							pros.add(new MongoProvider(mongoAdapter,99));
						}
						SelectorProvider[] arr = new SelectorProvider[pros.size()];
						arr = pros.toArray(arr);
						
						ProviderCluster cluster = new ModelProviderCluster(arr);
						ModelService modelService = new DefaultModelService(cluster);
						ref.compareAndSet(ModelService.NONE, modelService);
					}
					return ref.get();
				}

				@Override
				public AttributeKey getKey() throws BundleException {
					return AttributeKey.key(ModelService.class, Version.defaultVersion());
				}

				@Override
				public Version version() throws BundleException {
					return Version.defaultVersion();
				}

				@Override
				public ServiceFilter[] getFilter() throws BundleException {
					return null;
				}
				
			});
			
			// 发布符号服务
			symbolAdapter = context.registService(SymbolAdapter.class, new Provider<SymbolAdapter>(){

				private final AtomicReference<SymbolAdapter> ref = new AtomicReference<SymbolAdapter>(SymbolAdapter.NONE);
				
				@Override
				public SymbolAdapter get() throws BundleException {
					if(ref.get()==SymbolAdapter.NONE){
						List<SymbolProvider> pros = new ArrayList<SymbolProvider>();
						
						if(dbAdaptor.isActive()){
							pros.add(new DbSymbolProvider(dbAdaptor,1));
						}
						
//						if(mongoAdapter.isActive()){
//							pros.add(new MongoSymbolProvider(mongoAdapter,99));
//						}
						SymbolProvider[] arr = new SymbolProvider[pros.size()];
						arr = pros.toArray(arr);
						
						SymbolProviderCluster cluster = new ModelSymbolProviderCluster(arr);
						
						SymbolAdapter symbolService = new DefaultSymbolAdapter(cluster);
						ref.compareAndSet(SymbolAdapter.NONE, symbolService);
					}
					return ref.get();
				}

				@Override
				public AttributeKey getKey() throws BundleException {
					return AttributeKey.key(SymbolAdapter.class, Version.defaultVersion());
				}

				@Override
				public Version version() throws BundleException {
					return Version.defaultVersion();
				}

				@Override
				public ServiceFilter[] getFilter() throws BundleException {
					return null;
				}
				
			});
			
			// for test
			/*
			StringBuilder modelJson = new StringBuilder("{");
			int[] codeIds = new int[] {912, 916, 917, 918, 919};
			for (int codeId : codeIds) {
				List<Map<String, Object>> codeNames = null;
				
				try {
					codeNames = dbAdaptor.findAllMap("select CODEDEFID, CODENAME from conf_codedefinition where CODEID = " + codeId);
				} catch (SQLException e) {
					continue;
				}
				
				if (codeNames == null || codeNames.isEmpty()) {
					continue;
				}
				
				List<Map<String, Object>> modelIds = null;
				try {
					modelIds = dbAdaptor.findAllMap("select modelid from conf_modelcode t where t.codeexpress = 'CODEID=" + codeId
							+ "' and fielddefid in (select fielddefid from conf_fielddefinition where fieldalias = '开关状态')");
				} catch (SQLException e) {
					continue;
				}
				
				if (modelIds == null || modelIds.isEmpty()) {
					continue;
				}
				
				for (Map<String, Object> modelIdMap : modelIds) {
					
					int modelId = Integer.valueOf(modelIdMap.get("modelid").toString());
					List<Map<String, Object>> modelIdSymbols = null;
					try {
						modelIdSymbols = dbAdaptor.findAllMap("select symbolid, symbolvalue, symbolname from conf_modelsymbol where modelid = " + modelId);
					} catch (SQLException e) {
						continue;
					}
					
					if (modelIdSymbols == null || modelIdSymbols.isEmpty()) {
						continue;
					}
					
					List<String> jsonArray = new ArrayList<String>();
					for (Map<String, Object> modelIdSymbol : modelIdSymbols) {
						Object oSymbolValue = modelIdSymbol.get("symbolvalue");
						if (oSymbolValue == null) {
							continue;
						}
						String symbolValue = oSymbolValue.toString();
						if (symbolValue.isEmpty()) {
							continue;
						}
						int symbolId = Integer.valueOf(modelIdSymbol.get("symbolid").toString());
						String symbolName = String.valueOf(modelIdSymbol.get("symbolname"));
						
						int switchCase = -1;
						String typeValue = null;
						String[] symbolValueArray = symbolValue.split(";");
						for (String symbolValueItem : symbolValueArray) {
							if (symbolValueItem.contains("KGZT=")) {
								switchCase = Integer.valueOf(symbolValueItem.substring(5, symbolValueItem.length()));
							} else if (symbolValueItem.contains("TYPE=")) {
								int type = Integer.valueOf(symbolValueItem.substring(5, symbolValueItem.length()));
								typeValue = "TYPE:" + type;
							}
						}
						if (switchCase == -1) {
							continue;
						}
						
						String codeName = null;
						for (Map<String, Object> codeItem : codeNames) {
							int nCodeId = Integer.valueOf(codeItem.get("codedefid").toString());
							if (nCodeId == switchCase) {
								codeName = codeItem.get("codename").toString();
								break;
							}
						}
						if (null == codeName) {
							continue;
						}
						StringBuilder jsonArrayItem = new StringBuilder("{KGZT:" + switchCase 
								+ ", SYMBOLID:" + symbolId 
								+ ", SYMBOLNAME:" + symbolName
								+ ", CODENAME:'" + codeName + "'");
						if (null != typeValue) {
							jsonArrayItem.append(", ");
							jsonArrayItem.append(typeValue);
						}
						jsonArrayItem.append("}");
						jsonArray.add(jsonArrayItem.toString());
					}
					
					if (jsonArray.isEmpty()) {
						continue;
					}
					
					modelJson.append(modelId);
					modelJson.append(":");
					modelJson.append("[");
					for (int i = 0; i < jsonArray.size(); i++) {
						modelJson.append(jsonArray.get(i));
						if (i != jsonArray.size() - 1) {
							modelJson.append(",");
						}
					}
					modelJson.append("]");
					modelJson.append(",");
				}
			}
			String sModelJson = modelJson.toString().subSequence(0, modelJson.length() - 1) + "}";
			System.out.println(sModelJson);
			int stop = 1;

			stop++;
			*/
			// for test end
		}
			
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
	}
}
