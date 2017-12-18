package nari.distribution.TopoAnalysis.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.Dao.interfaces.DbAdaptor;
import nari.distribution.TopoAnalysis.DistributionTopoServiceActivator;
import nari.distribution.TopoAnalysis.bean.DeviceInfo;
import nari.model.ModelActivator;
import nari.model.bean.ClassDef;
import nari.model.bean.SubClassDef;
import nari.model.device.Device;
import nari.model.device.DeviceKey;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.symbol.SymbolAdapter;
import nari.network.interfaces.NetworkAdaptor;

public abstract class BaseTopoAnalyzeHandler {
	
	protected ModelService modelService = DistributionTopoServiceActivator.modelService;
	protected DbAdaptor dbAdaptor = DistributionTopoServiceActivator.dbAdaptor;
	protected SymbolAdapter symbolAdaptor = DistributionTopoServiceActivator.symbolAdapter;
	protected NetworkAdaptor networkAdaptor = DistributionTopoServiceActivator.networkAdaptor;
	
	/**
	 * 根据EqumentID获取ClassID
	 * @param equmentId
	 * @return
	 */
	protected Iterator<Integer> getClassIdsByEqumentId(String equmentId) {
		List<Integer> classIds = ModelActivator.getClassIdByEquId(equmentId);
		if (null == classIds) {
			return null;
		}
		return classIds.iterator();
	}
	
	protected List<String> getModelIdsByClassId(String classId) {
		
		DeviceModel model = null;
		try {
			model = modelService.fromClass(classId, false);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		SubClassDef[] modelDefs = model.getSubClassDef();
		if (null == modelDefs) {
			return null;
		}
		List<String> modelIds = new ArrayList<String>(modelDefs.length);
		for (SubClassDef modelDef : modelDefs) {
			modelIds.add(modelDef.getModelId());
		}
		
		return modelIds;
	}
	
	protected String getClassIdByModelId(String modelId) {
		SubClassDef subClassDef = null;
		try {
			subClassDef = modelService.getSubClassDef(modelId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (null == subClassDef) {
			return null;
		}
		return subClassDef.getClassDef().getClassId();
	}
	
	protected String getEqumentIdByModelId(String modelId) {
		SubClassDef subClassDef = null;
		try {
			subClassDef = modelService.getSubClassDef(modelId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (null == subClassDef) {
			return null;
		}
		ClassDef classDef = subClassDef.getClassDef();
		return classDef.getEquipmentID();
	}
	
	/**
	 * 根据ClassID和SBID获取拓扑设备主键
	 * @param classId
	 * @param sbId
	 * @return
	 */
	protected DeviceKey getDeviceKeyByClassId(String classId, String sbId) {
		
		// 查找模型
		DeviceModel model = null;
		try {
			model = modelService.fromClass(classId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return getDeviceKeyByModel(model, sbId);
	}
	
	/**
	 * 根据ModelID和SBID获取拓扑设备主键
	 * @param modelId
	 * @param sbId
	 * @return
	 */
	protected DeviceKey getDeviceKeyByModelId(String modelId, String sbId) {
		
		// 查找模型
		DeviceModel model = null;
		try {
			model = modelService.fromSubClass(modelId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return getDeviceKeyByModel(model, sbId);
	}

	/**
	 * 根据EqumentID和SBID获取拓扑设备主键
	 * @param equmentId
	 * @param sbId
	 * @return
	 */
	protected DeviceKey getDeviceKeyByEqumentId(String equmentId, String sbId) {

		Iterator<Integer> classIds = getClassIdsByEqumentId(equmentId);
		if (null == classIds) {
			return null;
		}
		
		while (classIds.hasNext()) {
			String classId = classIds.next().toString();
			DeviceKey deviceInfo = getDeviceKeyByClassId(classId, sbId);
			if (null != deviceInfo) {
				return deviceInfo;
			}
		}
		return null;
	}
	
	private DeviceKey getDeviceKeyByModel(DeviceModel model, String sbId) {
		
		if (null == model) {
			return null;
		}
		
		// 搜索设备
		String[] returnField = new String[] { "OID", "SBZLX" };
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.equal(builder.getRoot().get("SBID", String.class), sbId);
		ResultSet results = null;
		try {
			results = model.search(returnField, exp, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// 结果判空
		if (null == results) {
			return null;
		}
		Iterator<Device> iter = results.resultList();
		if (null == iter) {
			return null;
		}
		
		// 获取结果
		while (iter.hasNext()) {
			Device device = iter.next();
			String modelId = String.valueOf(device.getValue("SBZLX"));
			String oid = String.valueOf(device.getValue("OID"));
			DeviceKey deviceInfo = new DeviceKey(
					Integer.valueOf(modelId), Integer.valueOf(oid));
			return deviceInfo;
		}
		return null;
	}
	
	protected void getDeviceInfosFromOids(DeviceModel model, String psrType, String[] oids, List<DeviceInfo> deviceInfos) {
		
		if (null == model) {
			return;
		}
		
		String[] returnField = new String[] { "SBID", "SBMC" };
		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
		Expression exp = builder.in(builder.getRoot().get("OID", String.class), oids);
		ResultSet results = null;
		try {
			results = model.search(returnField, exp, null);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// 结果判空
		if (null == results) {
			return;
		}
		Iterator<Device> iter = results.resultList();
		if (null == iter) {
			return;
		}
		
		// 获取结果
		while (iter.hasNext()) {
			Device device = iter.next();
			String sbId = String.valueOf(device.getValue("SBID"));
			String sbName = String.valueOf(device.getValue("SBMC"));
			deviceInfos.add(new DeviceInfo(psrType, sbId, sbName));
		}
	}
}
