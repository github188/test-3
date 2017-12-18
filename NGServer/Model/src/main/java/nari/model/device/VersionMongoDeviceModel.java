package nari.model.device;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import nari.Geometry.Polygon;
import nari.MongoClient.interfaces.MongoAdaptor;
import nari.model.bean.ClassDef;
import nari.model.bean.DefaultVerClassDef;
import nari.model.bean.DefaultVerSubClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.SubClassDef;
import nari.model.device.filter.CriteriaQuery;
import nari.model.device.filter.Expression;
import nari.model.device.filter.ModifyFilter;
import nari.model.device.filter.Order;
import nari.model.device.filter.RemoveFilter;

public class VersionMongoDeviceModel extends AbstractDeviceModel implements DeviceModel {

	private DeviceModel base = DeviceModel.NONE;
	
	@SuppressWarnings("unused")
	private MongoAdaptor mongoAdaptor = MongoAdaptor.NONE;
	
	private final AtomicReference<ClassDef> classRef = new AtomicReference<ClassDef>(ClassDef.NONE);
	
	private final AtomicReference<SubClassDef[]> subClassRef = new AtomicReference<SubClassDef[]>();
	
	private final AtomicReference<FieldDef> fieldRef = new AtomicReference<FieldDef>(FieldDef.NONE);
	
	public VersionMongoDeviceModel(DeviceModel base,MongoAdaptor mongoAdaptor){
		this.base = base;
		this.mongoAdaptor = mongoAdaptor;
	}
	
	@Override
	public ClassDef getClassDef() {
		if(classRef.get()==ClassDef.NONE){
			ClassDef classDef = new DefaultVerClassDef(base.getClassDef(),base.getSubClassDef());
			classRef.compareAndSet(ClassDef.NONE, classDef);
		}
		return classRef.get();
	}

	@Override
	public SubClassDef[] getSubClassDef() {
		if(subClassRef.get()==null){
			SubClassDef[] sub = new SubClassDef[base.getSubClassDef().length];
			int i=0;
			for(SubClassDef b:base.getSubClassDef()){
				sub[i++] = new DefaultVerSubClassDef(getClassDef(),b);
			}
			subClassRef.compareAndSet(null, sub);
		}
		
		return subClassRef.get();
	}

	@Override
	public FieldDef getFieldDef() {
		if(fieldRef.get()==FieldDef.NONE){
			FieldDef versionField = new VersionFieldDef(base.getFieldDef());
			fieldRef.compareAndSet(FieldDef.NONE, versionField);
		}
		
		return fieldRef.get();
	}

	@Override
	public boolean add(Device device) throws Exception {
//		if(device.getValue("OID")==null){
//			throw new ModelException("oid is null");
//		}
//		
////		StringBuffer keyBuf = new StringBuffer();
////		StringBuffer valBuf = new StringBuffer();
//		Iterator<FieldDetail> fields = device.keyIterator();
//		FieldDetail fd = null;
//		List<Object> list = new ArrayList<Object>();
//		
//		String tableName = getClassDef().getTableName();
//		CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
//		
//		CriteriaInster inster = builder.createInsert();
//		inster.insert(inster.getRoot().select(tableName));
//		
//		List<Field> field = new ArrayList<Field>();
//		while(fields.hasNext()){
//			fd = fields.next();
////			keyBuf.append(fd.getFieldName()).append(",");
////			valBuf.append("?").append(",");
//			field.add(inster.getRoot().get(fd.getFieldName(), String.class));
//			list.add(device.getValue(fd));
//		}
//		
//		Field[] fa = new Field[field.size()];
//		fa = field.toArray(fa);
//		inster.field(fa);
//		inster.value(list.toArray());
//		
//		SQLCreator creator = (SQLCreator)inster;
//		String sql = creator.createSQL();
//		
////		String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
////		String value = valBuf.toString().substring(valBuf.lastIndexOf(","));
//		
////		String sql = "insert into %s (%s) values(%s)";
////		sql = String.format(filter, tableName, filter, value);
//		boolean suc = false;
//		try {
//			suc = dbAdaptor.save(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException(e.getMessage());
//		}
//		
//		return suc;
		return true;
	}

	@Override
	public boolean modify(Device device) throws Exception {
//		if(device.getValue("OID")==null){
//			throw new ModelException("oid is null");
//		}
//		
////		StringBuffer keyBuf = new StringBuffer();
//		Iterator<FieldDetail> fields = device.keyIterator();
//		FieldDetail fd = null;
//		List<Object> list = new ArrayList<Object>();
////		Object value = null;
//		
//		String tableName = getClassDef().getTableName();
//		
//		CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
//		CriteriaUpdate up = builder.createUpdate();
//		
//		List<Field> field = new ArrayList<Field>();
//		while(fields.hasNext()){
//			fd = fields.next();
////			keyBuf.append(fd.getFieldName()).append("=").append("?").append(",");
////			value = device.getValue(fd);
//			list.add(device.getValue(fd));
//			field.add(up.getRoot().get(fd.getFieldName(), String.class));
//		}
//		
////		String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
//		
//		up.update(up.getRoot().select(tableName));
//		
//		Field[] fa = new Field[field.size()];
//		fa = field.toArray(fa);
//		up.field(fa);
//		up.value(list.toArray());
//		
//		Expression exp = builder.equal(up.getRoot().get("OID", String.class), device.getValue("OID"));
//		up.where(exp);
//		
//		SQLCreator creator = (SQLCreator)up;
//		String sql = creator.createSQL();
//		
////		String sql = "update %s set %s where oid = ?";
////		sql = String.format(tableName, filter, list.toArray());
//		boolean suc = false;
//		try {
////			suc = dbAdaptor.update(sql,list.toArray());
//			suc = dbAdaptor.update(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException(e.getMessage());
//		}
//		
//		return suc;
		return true;
	}

	@Override
	public boolean remove(Device device) throws Exception {
//		if(device.getValue("OID")==null){
//			throw new ModelException("oid is null");
//		}
//		
//		CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
//		
//		String tableName = getClassDef().getTableName();
//		
//		CriteriaDelete del = builder.createDelete();
//		del.delete(del.getRoot().select(tableName));
//		Expression exp = builder.equal(del.getRoot().get("OID", String.class), device.getValue("OID"));
//		del.where(exp);
//		SQLCreator creator = (SQLCreator)del;
//		String sql = creator.createSQL();
//		
////		String tableName = getClassDef().getTableName();
////		String sql = "delete from %s where oid = ?";
////		sql = String.format(tableName);
//		boolean suc = false;
//		try {
//			suc = dbAdaptor.delete(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException(e.getMessage());
//		}
//		
//		return suc;
		return true;
	}

	@Override
	public boolean add(Device device, boolean join) throws Exception {
		return add(device);
	}

	@Override
	public boolean modify(Device device, boolean join) throws Exception {
		base.modify(device,join);
		return modify(device);
	}

	@Override
	public boolean remove(Device device, boolean join) throws Exception {
		base.remove(device, join);
		return remove(device);
	}

	@Override
	public boolean add(List<Device> devices) throws Exception {
//		if(devices==null || devices.size()==0){
//			return true;
//		}
//		Transaction action = null;
//		boolean suc = true;
//		try {
//			action = dbAdaptor.getTransactionManager();
//			action.startTransaction();
//			
//			for(Device device:devices){
//				if(device.getValue("OID")==null){
//					throw new ModelException("oid is null");
//				}
//				
//				StringBuffer keyBuf = new StringBuffer();
//				StringBuffer valBuf = new StringBuffer();
//				Iterator<FieldDetail> fields = device.keyIterator();
//				FieldDetail fd = null;
//				List<Object> list = new ArrayList<Object>();
//				while(fields.hasNext()){
//					fd = fields.next();
//					keyBuf.append(fd.getFieldName()).append(",");
//					valBuf.append("?").append(",");
//					list.add(device.getValue(fd));
//				}
//				
//				String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
//				String value = valBuf.toString().substring(valBuf.lastIndexOf(","));
//				String tableName = getClassDef().getTableName();
//				String sql = "insert into %s (%s) values(%s)";
//				sql = String.format(filter, tableName, filter, value);
//				try {
//					suc = suc && action.save(sql, list.toArray());
//				} catch (SQLException e) {
//					e.printStackTrace();
//					action.rollback();
//					throw new ModelException(e.getMessage());
//				} finally{
//					try {
//						action.stopTransaction();
//					} catch (SQLException e) {
//						e.printStackTrace();
//						throw new ModelException(e.getMessage());
//					}
//				}
//			}
//			action.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException("failed to get Transaction");
//		} finally{
//			try {
//				if(action.isStarted()){
//					action.stopTransaction();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new ModelException(e.getMessage());
//			}
//		}
//		
//		return suc;
		return true;
	}

	@Override
	public boolean modify(List<Device> devices) throws Exception {
//		if(devices==null || devices.size()==0){
//			return true;
//		}
//		Transaction action = null;
//		boolean suc = true;
//		try {
//			action = dbAdaptor.getTransactionManager();
//			action.startTransaction();
//			for(Device device:devices){
//				if(device.getValue("OID")==null){
//					throw new ModelException("oid is null");
//				}
//				
//				StringBuffer keyBuf = new StringBuffer();
//				Iterator<FieldDetail> fields = device.keyIterator();
//				FieldDetail fd = null;
//				List<Object> list = new ArrayList<Object>();
//				String oid = "";
//				Object value = null;
//				while(fields.hasNext()){
//					fd = fields.next();
//					keyBuf.append(fd.getFieldName()).append("=").append("?").append(",");
//					value = device.getValue(fd);
//					if("OID".equalsIgnoreCase(fd.getFieldName())){
//						oid = String.valueOf(value);
//					}
//					list.add(value);
//				}
//				list.add(oid);
//				
//				String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
//				String tableName = getClassDef().getTableName();
//				String sql = "update %s set %s where oid = ?";
//				sql = String.format(tableName, filter, list.toArray());
//				try {
//					suc = suc && action.update(sql,list.toArray());
//				} catch (SQLException e) {
//					e.printStackTrace();
//					action.rollback();
//					throw new ModelException(e.getMessage());
//				} finally{
//					try {
//						action.stopTransaction();
//					} catch (SQLException e) {
//						e.printStackTrace();
//						throw new ModelException(e.getMessage());
//					}
//				}
//			}
//			action.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException("failed to get Transaction");
//		} finally{
//			try {
//				if(action.isStarted()){
//					action.stopTransaction();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new ModelException(e.getMessage());
//			}
//		}
//		
//		return suc;
		return true;
	}

	@Override
	public boolean remove(List<Device> devices) throws Exception {
//		if(devices==null || devices.size()==0){
//			return true;
//		}
//		Transaction action = null;
//		boolean suc = true;
//		try {
//			action = dbAdaptor.getTransactionManager();
//			action.startTransaction();
//			
//			CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
//			
//			String tableName = getClassDef().getTableName();
//			CriteriaDelete del = null;
//			
//			for(Device device:devices){
//				if(device.getValue("OID")==null){
//					throw new ModelException("oid is null");
//				}
//				
//				del = builder.createDelete();
//				del.delete(del.getRoot().select(tableName));
//				Expression exp = builder.equal(del.getRoot().get("OID", String.class), device.getValue("OID"));
//				del.where(exp);
//				SQLCreator creator = (SQLCreator)del;
//				String sql = creator.createSQL();
//				
////				String sql = "delete from %s where oid = ?";
////				sql = String.format(tableName);
//				try {
////					suc = suc && action.delete(sql,new Object[]{device.getValue("OID")});
//					suc = suc && action.delete(sql);
//				} catch (SQLException e) {
//					e.printStackTrace();
//					action.rollback();
//					throw new ModelException(e.getMessage());
//				} finally{
//					try {
//						action.stopTransaction();
//					} catch (SQLException e) {
//						e.printStackTrace();
//						throw new ModelException(e.getMessage());
//					}
//				}
//			}
//			
//			action.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException("failed to get Transaction");
//		} finally{
//			try {
//				if(action.isStarted()){
//					action.stopTransaction();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new ModelException(e.getMessage());
//			}
//		}
//		
//		return suc;
		return true;
	}

	@Override
	public Device createDevice() throws Exception {
//		int oid = 0;
//		try {
//			oid = dbAdaptor.getSequence(getClassDef().getTableName());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException(e.getMessage());
//		}
//		Device dev = new DefaultDevice(getFieldDef(),getClassDef(),getSubClassDef());
//		dev.setValue("OID", oid);
//		return dev;
		return null;
	}

	@Override
	public Device createDevice(Device device) throws Exception {
//		int oid = 0;
//		try {
//			oid = dbAdaptor.getSequence(getClassDef().getTableName());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new ModelException(e.getMessage());
//		}
//		Device dev = new DefaultDevice(getFieldDef(),getClassDef(),getSubClassDef());
//		dev.setValue("OID", oid);
//		
//		Iterator<FieldDetail> keys = device.keyIterator();
//		FieldDetail detail = null;
//		while(keys.hasNext()){
//			detail = keys.next();
//			if(detail.getFieldName().equalsIgnoreCase("OID")){
//				continue;
//			}
//			dev.setValue(detail, device.getValue(detail));
//		}
//		return dev;
		return null;
	}

	@Override
	public ResultSet search(String[] returnType,Expression exp,Order order) throws Exception {
//		CriteriaQuery query = getEntryManager().getCriteriaBuilder().createQuery();
//		query = query.select(query.getRoot().select(getClassDef().getTableName()));
//		Field[] field = new Field[returnType.length];
//		int i=0;
//		for(String rt:returnType){
//			field[i++] = query.getRoot().get(rt, String.class);
//		}
//		query = query.field(field);
//		SQLCreator creator = (SQLCreator)query;
//		String sql = creator.createSQL();
//		
//		List<Map<String,Object>> maps = null;
//		try {
//			maps = dbAdaptor.findAllMap(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		}
//		if(maps==null || maps.size()==0){
//			return ResultSet.NONE;
//		}
//		
//		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
//		
//		return set;
		return base.search(returnType, exp, order);
	}
	
	@Override
	public ResultSet search(CriteriaQuery query) throws Exception {
//		SQLCreator creator = (SQLCreator)query;
//		String sql = creator.createSQL();
//		
//		List<Map<String,Object>> maps = null;
//		try {
//			maps = dbAdaptor.findAllMap(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		}
//		if(maps==null || maps.size()==0){
//			return ResultSet.NONE;
//		}
//		
//		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
//		
//		return set;
		return base.search(query);
	}
	
	@Override
	public ResultSet spatialQuery(String[] returnType,Expression exp,Order order, Polygon poly) throws Exception{
//		CriteriaQuery query = getEntryManager().getCriteriaBuilder().createQuery();
//		Expression shapeExp = getEntryManager().getCriteriaBuilder().spatial(query.getRoot().get("shape", Polygon.class,new FieldProcessor() {
//			
//			@Override
//			public Object convert(Object value) {
//				if(value instanceof Polygon){
//					Polygon py = (Polygon)value;
//					
//					Coordinate[] coords = py.getCoordinates();
//					StringBuffer bf = new StringBuffer();
//					int i = 0;
//					for(Coordinate coord:coords){
//						bf.append(coord.getX()).append(",").append(coord.getY());
//						if(i<coords.length-1){
//							bf.append(",");
//							i++;
//						}
//					}
//					bf.append(",").append(coords[0].getX()).append(",").append(coords[0].getY());
//					String geoStr = " sdo_relate(shape ,mdsys.sdo_geometry(2003,null,null,mdsys.sdo_elem_info_array(1,1003,1),mdsys.sdo_ordinate_array(%s)),'mask=anyinteract querytype=window')='TRUE' ";
//					geoStr = String.format(geoStr, bf.toString());
//					return geoStr;
//				}
//				return value;
//			}
//			
//		}), poly);
//		exp = getEntryManager().getCriteriaBuilder().and(exp, shapeExp);
//		query = query.select(query.getRoot().select(getClassDef().getTableName()));
//		Field[] field = new Field[returnType.length];
//		int i=0;
//		for(String rt:returnType){
//			field[i++] = query.getRoot().get(rt, String.class);
//		}
//		query = query.field(field);
//		query.where(exp);
//		
//		SQLCreator creator = (SQLCreator)query;
//		String sql = creator.createSQL();
//		
//		List<Map<String,Object>> maps = null;
//		try {
//			maps = dbAdaptor.findAllMap(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		}
//		if(maps==null || maps.size()==0){
//			return ResultSet.NONE;
//		}
//		
//		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
//		
//		return set;
		return base.spatialQuery(returnType, exp, order, poly);
	}

	@Override
	public boolean modify(ModifyFilter filter) throws Exception {
		return true;
	}

	@Override
	public boolean remove(RemoveFilter filter) throws Exception {
		return true;
	}

	@Override
	public boolean isVersion() throws Exception {
		return true;
	}

}
