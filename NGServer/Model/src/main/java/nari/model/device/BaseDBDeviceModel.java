package nari.model.device;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.bundle.transaction.Transaction;
import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Polygon;
import nari.model.TableName;
import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.FieldObject;
import nari.model.bean.SubClassDef;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.CriteriaDelete;
import nari.model.device.filter.CriteriaInster;
import nari.model.device.filter.CriteriaQuery;
import nari.model.device.filter.CriteriaUpdate;
import nari.model.device.filter.Expression;
import nari.model.device.filter.Field;
import nari.model.device.filter.FieldProcessor;
import nari.model.device.filter.ModifyFilter;
import nari.model.device.filter.Order;
import nari.model.device.filter.RemoveFilter;
import nari.model.device.filter.SQLCreator;

public class BaseDBDeviceModel extends AbstractDeviceModel implements DeviceModel {

	private DbAdaptor dbAdaptor = DbAdaptor.NONE;
	
	private ClassDef classDef = ClassDef.NONE;
	
	private SubClassDef[] subClassDef = new SubClassDef[]{};
	
	private FieldDef fieldDef = FieldDef.NONE;
	
	public BaseDBDeviceModel(ClassDef classDef,SubClassDef[] subClassDef,DbAdaptor dbAdaptor){
		this.dbAdaptor = dbAdaptor;
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
		String sql = "select * from " + TableName.CONF_FIELDDEFINITION + " where classid=?";
		List<FieldObject> fieldObj = null;
		try {
			fieldObj = dbAdaptor.findAll(sql, new Object[]{classDef.getClassId()}, FieldObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(fieldObj==null || fieldObj.size()==0){
			return FieldDef.NONE;
		}
		fieldDef = new DefaultFieldDef(classDef,fieldObj);
		
		return fieldDef;
	}

	@Override
	public boolean add(Device device) throws Exception {
		if(device.getValue("OID")==null){
			throw new Exception("oid is null");
		}
		
//		StringBuffer keyBuf = new StringBuffer();
//		StringBuffer valBuf = new StringBuffer();
		Iterator<FieldDetail> fields = device.keyIterator();
		FieldDetail fd = null;
		List<Object> list = new ArrayList<Object>();
		
		String tableName = getClassDef().getTableName();
		CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
		
		CriteriaInster inster = builder.createInsert();
		inster.insert(inster.getRoot().select(tableName));
		
		List<Field> field = new ArrayList<Field>();
		while(fields.hasNext()){
			fd = fields.next();
//			keyBuf.append(fd.getFieldName()).append(",");
//			valBuf.append("?").append(",");
			field.add(inster.getRoot().get(fd.getFieldName(), String.class));
			list.add(device.getValue(fd));
		}
		
		Field[] fa = new Field[field.size()];
		fa = field.toArray(fa);
		inster.field(fa);
		inster.value(list.toArray());
		
		SQLCreator creator = (SQLCreator)inster;
		String sql = creator.createSQL();
		
//		String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
//		String value = valBuf.toString().substring(valBuf.lastIndexOf(","));
		
//		String sql = "insert into %s (%s) values(%s)";
//		sql = String.format(filter, tableName, filter, value);
		boolean suc = false;
		try {
			suc = dbAdaptor.save(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
		return suc;
	}

	@Override
	public boolean modify(Device device) throws Exception {
		if(device.getValue("OID")==null){
			throw new Exception("oid is null");
		}
		
//		StringBuffer keyBuf = new StringBuffer();
		Iterator<FieldDetail> fields = device.keyIterator();
		FieldDetail fd = null;
		List<Object> list = new ArrayList<Object>();
//		Object value = null;
		
		String tableName = getClassDef().getTableName();;
		
		CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
		CriteriaUpdate up = builder.createUpdate();
		
		List<Field> field = new ArrayList<Field>();
		while(fields.hasNext()){
			fd = fields.next();
//			keyBuf.append(fd.getFieldName()).append("=").append("?").append(",");
//			value = device.getValue(fd);
			list.add(device.getValue(fd));
			field.add(up.getRoot().get(fd.getFieldName(), String.class));
		}
		
//		String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
		
		up.update(up.getRoot().select(tableName));
		
		Field[] fa = new Field[field.size()];
		fa = field.toArray(fa);
		up.field(fa);
		up.value(list.toArray());
		
		Expression exp = builder.equal(up.getRoot().get("OID", String.class), device.getValue("OID"));
		up.where(exp);
		
		SQLCreator creator = (SQLCreator)up;
		String sql = creator.createSQL();
		
//		String sql = "update %s set %s where oid = ?";
//		sql = String.format(tableName, filter, list.toArray());
		boolean suc = false;
		try {
//			suc = dbAdaptor.update(sql,list.toArray());
			suc = dbAdaptor.update(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
		return suc;
	}

	@Override
	public boolean remove(Device device) throws Exception {
		if(device.getValue("OID")==null){
			throw new Exception("oid is null");
		}
		
		CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
		
		String tableName = getClassDef().getTableName();
		
		CriteriaDelete del = builder.createDelete();
		del.delete(del.getRoot().select(tableName));
		Expression exp = builder.equal(del.getRoot().get("OID", String.class), device.getValue("OID"));
		del.where(exp);
		SQLCreator creator = (SQLCreator)del;
		String sql = creator.createSQL();
		
		boolean suc = false;
		try {
			suc = dbAdaptor.delete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
		return suc;
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
		
		SQLCreator creator = (SQLCreator)query;
		String sql = creator.createSQL();
		
		List<Map<String,Object>> maps = null;
		try {
			maps = dbAdaptor.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		if(maps==null || maps.size()==0){
			return ResultSet.NONE;
		}
		
		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
		
		return set;
	}
	
	@Override
	public ResultSet search(CriteriaQuery query) throws Exception {
		if(query==null){
			return null;
		}
		SQLCreator creator = (SQLCreator)query;
		String sql = creator.createSQL();
		
		List<Map<String,Object>> maps = null;
		try {
			maps = dbAdaptor.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
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
		
		SQLCreator creator = (SQLCreator)query;
		String sql = creator.createSQL();
		
		List<Map<String,Object>> maps = null;
		try {
			maps = dbAdaptor.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		if(maps==null || maps.size()==0){
			return ResultSet.NONE;
		}
		
		ResultSet set = new MapResultSet(maps,getFieldDef(),getClassDef(),getSubClassDef());
		
		return set;
	}
	
	@Override
	public boolean add(List<Device> devices) throws Exception {
		if(devices==null || devices.size()==0){
			return true;
		}
		Transaction action = null;
		boolean suc = true;
		try {
			action = dbAdaptor.getTransactionManager();
			action.start();
			
			for(Device device:devices){
				if(device.getValue("OID")==null){
					throw new Exception("oid is null");
				}
				
				StringBuffer keyBuf = new StringBuffer();
				StringBuffer valBuf = new StringBuffer();
				Iterator<FieldDetail> fields = device.keyIterator();
				FieldDetail fd = null;
				List<Object> list = new ArrayList<Object>();
				while(fields.hasNext()){
					fd = fields.next();
					keyBuf.append(fd.getFieldName()).append(",");
					valBuf.append("?").append(",");
					list.add(device.getValue(fd));
				}
				
				String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
				String value = valBuf.toString().substring(valBuf.lastIndexOf(","));
				String tableName = getClassDef().getTableName();
				String sql = "insert into %s (%s) values(%s)";
				sql = String.format(filter, tableName, filter, value);
				try {
					suc = suc && dbAdaptor.save(sql, list.toArray());
				} catch (SQLException e) {
					e.printStackTrace();
					action.rollback();
					throw new Exception(e.getMessage());
				} finally{
					try {
						action.stop();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new Exception(e.getMessage());
					}
				}
			}
			action.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("failed to get Transaction");
		}finally{
			try {
				action.stop();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
		return suc;
	}

	@Override
	public boolean modify(List<Device> devices) throws Exception {
		if(devices==null || devices.size()==0){
			return true;
		}
		Transaction action = null;
		boolean suc = true;
		try {
			action = dbAdaptor.getTransactionManager();
			action.start();
			for(Device device:devices){
				if(device.getValue("OID")==null){
					throw new Exception("oid is null");
				}
				
				StringBuffer keyBuf = new StringBuffer();
				Iterator<FieldDetail> fields = device.keyIterator();
				FieldDetail fd = null;
				List<Object> list = new ArrayList<Object>();
				String oid = "";
				Object value = null;
				while(fields.hasNext()){
					fd = fields.next();
					keyBuf.append(fd.getFieldName()).append("=").append("?").append(",");
					value = device.getValue(fd);
					if("OID".equalsIgnoreCase(fd.getFieldName())){
						oid = String.valueOf(value);
					}
					list.add(value);
				}
				list.add(oid);
				
				String filter = keyBuf.toString().substring(keyBuf.lastIndexOf(","));
				String tableName = getClassDef().getTableName();
				String sql = "update %s set %s where oid = ?";
				sql = String.format(tableName, filter, list.toArray());
				try {
					suc = suc && dbAdaptor.update(sql,list.toArray());
				} catch (SQLException e) {
					e.printStackTrace();
					action.rollback();
					throw new Exception(e.getMessage());
				} finally{
					try {
						action.stop();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new Exception(e.getMessage());
					}
				}
			}
			action.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("failed to get Transaction");
		} finally{
			try {
				action.stop();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
		return suc;
	}

	@Override
	public boolean remove(List<Device> devices) throws Exception {
		if(devices==null || devices.size()==0){
			return true;
		}
		Transaction action = null;
		boolean suc = true;
		try {
			action = dbAdaptor.getTransactionManager();
			action.start();
			
			CriteriaBuilder builder = getEntryManager().getCriteriaBuilder();
			
			String tableName = getClassDef().getTableName();
			CriteriaDelete del = null;
			
			for(Device device:devices){
				if(device.getValue("OID")==null){
					throw new Exception("oid is null");
				}
				
				del = builder.createDelete();
				del.delete(del.getRoot().select(tableName));
				Expression exp = builder.equal(del.getRoot().get("OID", String.class), device.getValue("OID"));
				del.where(exp);
				SQLCreator creator = (SQLCreator)del;
				String sql = creator.createSQL();
//				String sql = "delete from %s where oid = ?";
//				sql = String.format(tableName);
				try {
//					suc = suc && action.delete(sql,new Object[]{device.getValue("OID")});
					suc = suc && dbAdaptor.delete(sql);
				} catch (SQLException e) {
					e.printStackTrace();
					action.rollback();
					throw new Exception(e.getMessage());
				} finally{
					try {
						action.stop();
					} catch (SQLException e) {
						e.printStackTrace();
						throw new Exception(e.getMessage());
					}
				}
			}
			
			action.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("failed to get Transaction");
		} finally{
			try {
				action.stop();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
		return suc;
	}

	@Override
	public Device createDevice() throws Exception {
		int oid = 0;
		try {
			oid = dbAdaptor.getSequence(getClassDef().getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		Device dev = new DefaultDevice(getFieldDef(),getClassDef(),getSubClassDef());
		dev.setValue("OID", oid);
		return dev;
	}

	@Override
	public Device createDevice(Device device) throws Exception {
		int oid = 0;
		try {
			oid = dbAdaptor.getSequence(getClassDef().getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		Device dev = new DefaultDevice(getFieldDef(),getClassDef(),getSubClassDef());
		dev.setValue("OID", oid);
		
		Iterator<FieldDetail> keys = device.keyIterator();
		FieldDetail detail = null;
		while(keys.hasNext()){
			detail = keys.next();
			if(detail.getFieldName().equalsIgnoreCase("OID")){
				continue;
			}
			dev.setValue(detail, device.getValue(detail));
		}
		return dev;
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

}
