package nari.model.device;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.Polygon;
import nari.MongoClient.interfaces.MongoAdaptor;
import nari.model.TableName;
import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.FieldObject;
import nari.model.bean.SubClassDef;
import nari.model.device.filter.CriteriaQuery;
import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;
import nari.model.device.filter.FieldProcessor;
import nari.model.device.filter.ModifyFilter;
import nari.model.device.filter.Order;
import nari.model.device.filter.RemoveFilter;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;

import com.mongodb.client.MongoCollection;

public class BaseMongoDeviceModel extends AbstractDeviceModel implements DeviceModel {

	private MongoAdaptor mongoAdaptor = MongoAdaptor.NONE;
	
	private ClassDef classDef = ClassDef.NONE;
	
	private SubClassDef[] subClassDef = new SubClassDef[]{};
	
	private FieldDef fieldDef = FieldDef.NONE;
	
	public BaseMongoDeviceModel(ClassDef classDef,SubClassDef[] subClassDef,MongoAdaptor mongoAdaptor) {
		this.mongoAdaptor = mongoAdaptor;
		this.classDef = classDef;
		this.subClassDef = subClassDef;
	}
	
	@Override
	public ClassDef getClassDef() {
		return classDef;
	}

	@Override
	public SubClassDef[] getSubClassDef() {
		return subClassDef;
	}

	@Override
	public FieldDef getFieldDef() {
		if(fieldDef!=FieldDef.NONE){
			return fieldDef;
		}
		
		List<FieldObject> fieldObj = null;
		MongoCollection<BsonDocument> mongoCollection;
		try {
			mongoCollection = mongoAdaptor.getMongoCollection(TableName.CONF_FIELDDEFINITION);
			fieldObj = mongoAdaptor.findList(mongoCollection, new BsonDocument().append("classid", new BsonString(classDef.getClassId())), FieldObject.class,null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		fieldDef = new DefaultFieldDef(classDef,fieldObj);
		return fieldDef;
	}

	@Override
	public boolean add(Device device) throws Exception {
		if(device.getValue("OID")==null){
			throw new Exception("oid is null");
		}
		
		Iterator<FieldDetail> fields = device.keyIterator();
		FieldDetail fd = null;
		
		String tableName = getClassDef().getTableName();
		
		BsonDocument doc = new BsonDocument();
		while(fields.hasNext()){
			fd = fields.next();
			Object v = device.getValue(fd);
			doc.append(fd.getFieldName().toUpperCase(), getBsonString(v));
		}
		
		try {
			MongoCollection<BsonDocument> mongoCollection = mongoAdaptor.getMongoCollection(tableName);
			mongoAdaptor.insert(mongoCollection, doc);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean modify(Device device) throws Exception {
		if(device.getValue("OID")==null){
			throw new Exception("oid is null");
		}
		
		Iterator<FieldDetail> fields = device.keyIterator();
		FieldDetail fd = null;
		
		String tableName = getClassDef().getTableName();
		
		BsonDocument doc = new BsonDocument();
		while(fields.hasNext()){
			fd = fields.next();
			Object v = device.getValue(fd);
			doc.append(fd.getFieldName().toUpperCase(), getBsonString(v));
		}
		
		BsonDocument filter = new BsonDocument().append("OID", new BsonString(String.valueOf(device.getValue("OID"))));
		
		try {
			MongoCollection<BsonDocument> mongoCollection = mongoAdaptor.getMongoCollection(tableName);
			mongoAdaptor.update(mongoCollection, filter, doc);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean remove(Device device) throws Exception {
		if(device.getValue("OID")==null){
			throw new Exception("oid is null");
		}
		
		String tableName = getClassDef().getTableName();
		
		BsonDocument filter = new BsonDocument().append("OID", new BsonString(String.valueOf(device.getValue("OID"))));
		
		try {
			MongoCollection<BsonDocument> mongoCollection = mongoAdaptor.getMongoCollection(tableName);
			mongoAdaptor.delete(mongoCollection, filter);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean add(Device device, boolean join) throws Exception {
		return add(device);
	}

	@Override
	public boolean modify(Device device, boolean join) throws Exception {
		return modify(device);
	}

	@Override
	public boolean remove(Device device, boolean join) throws Exception {
		return remove(device);
	}

	@Override
	public ResultSet search(String[] returnType,Expression exp,Order order) throws Exception {
		CriteriaQuery query = getEntryManager().getCriteriaBuilder().createQuery();
		query = query.select(query.getRoot().select(getClassDef().getTableName()));
		//若不提供返回字段则全查
		if(null != returnType && returnType.length !=0){
			Field[] field = new Field[returnType.length];
			int i=0;
			for(String rt:returnType){
				field[i++] = query.getRoot().get(rt, String.class);
			}
			query = query.field(field);

		}
		//若不提供条件
		if(exp!=Expression.NONE && exp != null){
			query = query.where(exp);
		}
		
		//若order不为空
		if(order!=Order.NONE && order != null){
			query = query.orderBy(order);
		}
		List<Map<String,Object>> maps = null;
		BsonDocument filter = query.createBson();
		try {
			MongoCollection<BsonDocument> mongoCollection = mongoAdaptor.getMongoCollection(getClassDef().getTableName());
			maps = mongoAdaptor.findList(mongoCollection, filter,returnType);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
		return set;
	}
	
	@Override
	public ResultSet search(CriteriaQuery query) throws Exception {
		if(query==null){
			return null;
		}
		List<Map<String,Object>> maps = null;
		BsonDocument filter = query.createBson();
		try {
			MongoCollection<BsonDocument> mongoCollection = mongoAdaptor.getMongoCollection(getClassDef().getTableName());
			maps = mongoAdaptor.findList(mongoCollection, filter,null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(maps==null || maps.size()==0){
			return ResultSet.NONE;
		}
		
		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
		
		return set;
	}

	@Override
	public ResultSet spatialQuery(String[] returnType,Expression exp,Order order, Polygon poly) throws Exception{
		CriteriaQuery query = getEntryManager().getCriteriaBuilder().createQuery();
		Expression shapeExp = getEntryManager().getCriteriaBuilder().spatial(query.getRoot().get("shape", Polygon.class,new FieldProcessor() {
			
			@Override
			public Object convert(Object value) {
				if(value instanceof Polygon){
					Polygon py = (Polygon)value;
					
					Coordinate[] coords = py.getCoordinates();
					StringBuffer bf = new StringBuffer();
					int i = 0;
					for(Coordinate coord:coords){
						bf.append(coord.getX()).append(",").append(coord.getY());
						if(i<coords.length-1){
							bf.append(",");
							i++;
						}
					}
					bf.append(",").append(coords[0].getX()).append(",").append(coords[0].getY());
					String geoStr = " sdo_relate(shape ,mdsys.sdo_geometry(2003,null,null,mdsys.sdo_elem_info_array(1,1003,1),mdsys.sdo_ordinate_array(%s)),'mask=anyinteract querytype=window')='TRUE' ";
					geoStr = String.format(geoStr, bf.toString());
					return geoStr;
				}
				return value;
			}
			
		}), poly);
		//若不提供条件
		if(exp!=Expression.NONE && exp != null){
			exp = getEntryManager().getCriteriaBuilder().and(exp, shapeExp);
		}else{
			exp = shapeExp;
		}
		
		query = query.select(query.getRoot().select(getClassDef().getTableName()));
		
		//若不提供返回字段则全查
		if(null != returnType && returnType.length !=0){
			Field[] field = new Field[returnType.length];
			int i=0;
			for(String rt:returnType){
				field[i++] = query.getRoot().get(rt, String.class);
			}
			query = query.field(field);

		}
		
		query.where(exp);
		
		//若order不为空
		if(order!=Order.NONE && order != null){
			query = query.orderBy(order);
		}
		
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
		
		BsonDocument filter = query.createBson();
		List<Map<String,Object>> maps = null;
		try {
			MongoCollection<BsonDocument> mongoCollection = mongoAdaptor.getMongoCollection(getClassDef().getTableName());
			maps = mongoAdaptor.findList(mongoCollection, filter,returnType);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if(maps==null || maps.size()==0){
			return ResultSet.NONE;
		}
		
		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
		
		return set;
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
//					throw new Exception("oid is null");
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
//					throw new Exception(e.getMessage());
//				} finally{
//					try {
//						action.stopTransaction();
//					} catch (SQLException e) {
//						e.printStackTrace();
//						throw new Exception(e.getMessage());
//					}
//				}
//			}
//			action.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception("failed to get Transaction");
//		}finally{
//			try {
//				if(action.isStarted()){
//					action.stopTransaction();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new Exception(e.getMessage());
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
//					throw new Exception("oid is null");
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
//					throw new Exception(e.getMessage());
//				} finally{
//					try {
//						action.stopTransaction();
//					} catch (SQLException e) {
//						e.printStackTrace();
//						throw new Exception(e.getMessage());
//					}
//				}
//			}
//			action.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception("failed to get Transaction");
//		} finally{
//			try {
//				if(action.isStarted()){
//					action.stopTransaction();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new Exception(e.getMessage());
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
//					throw new Exception("oid is null");
//				}
//				
//				del = builder.createDelete();
//				del.delete(del.getRoot().select(tableName));
//				Expression exp = builder.equal(del.getRoot().get("OID", String.class), device.getValue("OID"));
//				del.where(exp);
//				SQLCreator creator = (SQLCreator)del;
//				String sql = creator.createSQL();
////				String sql = "delete from %s where oid = ?";
////				sql = String.format(tableName);
//				try {
////					suc = suc && action.delete(sql,new Object[]{device.getValue("OID")});
//					suc = suc && action.delete(sql);
//				} catch (SQLException e) {
//					e.printStackTrace();
//					action.rollback();
//					throw new Exception(e.getMessage());
//				} finally{
//					try {
//						action.stopTransaction();
//					} catch (SQLException e) {
//						e.printStackTrace();
//						throw new Exception(e.getMessage());
//					}
//				}
//			}
//			
//			action.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception("failed to get Transaction");
//		} finally{
//			try {
//				if(action.isStarted()){
//					action.stopTransaction();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new Exception(e.getMessage());
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
//			throw new Exception(e.getMessage());
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
//			throw new Exception(e.getMessage());
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
	public boolean modify(ModifyFilter filter) throws Exception {
		return false;
	}

	@Override
	public boolean remove(RemoveFilter filter) throws Exception {
		return false;
	}

	@Override
	public boolean isVersion() throws Exception {
		return false;
	}
	
	private BsonString getBsonString(Object value){
		
		if(value==null){
			return new BsonString("");
		}
		
		BsonString result = null;
		if(value instanceof Geometry){
			Geometry geom = (Geometry)value;
			Coordinate[] coords = geom.getCoordinates();
			BsonDocument coordinates = new BsonDocument();
			
			BsonArray line = new BsonArray();
			BsonArray polygon = new BsonArray();
			for(Coordinate coordinate:coords){
				BsonArray point = new BsonArray(Arrays.asList(new BsonDouble(coordinate.getX()), new BsonDouble(coordinate.getY())));
				line.add(point);
			}
			polygon.add(line);
			
			coordinates.append("coordinates", polygon);
			
			BsonDocument r = new BsonDocument();
			
			r.put("type", new BsonString("Polygon"));
			r.put("coordinates", polygon);
			
			result = r.asString();
		}else{
			result = new BsonString(String.valueOf(value));
		}
		
		return result;
	}
	
}
