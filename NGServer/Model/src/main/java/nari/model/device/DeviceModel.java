package nari.model.device;

import java.util.List;

import nari.Geometry.Polygon;
import nari.model.bean.ClassDef;
import nari.model.bean.FieldDef;
import nari.model.bean.SubClassDef;
import nari.model.device.filter.CriteriaQuery;
import nari.model.device.filter.EntryManager;
import nari.model.device.filter.Expression;
import nari.model.device.filter.ModifyFilter;
import nari.model.device.filter.Order;
import nari.model.device.filter.RemoveFilter;

public interface DeviceModel {
	
	public static final DeviceModel NONE = new DeviceModel(){

		@Override
		public ClassDef getClassDef() {
			return ClassDef.NONE;
		}

		@Override
		public SubClassDef[] getSubClassDef() {
			return new SubClassDef[]{};
		}

		@Override
		public boolean add(Device device) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean modify(Device device) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean remove(Device device) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean add(List<Device> devices) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean modify(List<Device> device) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean remove(List<Device> device) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public ResultSet search(String[] returnType,Expression exp,Order order) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public ResultSet search(CriteriaQuery query) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}
		
		@Override
		public FieldDef getFieldDef() {
			return FieldDef.NONE;
		}

		@Override
		public boolean add(Device device, boolean join) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean modify(Device device, boolean join) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean remove(Device device, boolean join) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public Device createDevice() throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public Device createDevice(Device device) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean modify(ModifyFilter filter) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean remove(RemoveFilter filter) throws Exception {
			throw new Exception("can not invoke this method on a null deviceModel");
		}

		@Override
		public boolean isVersion() throws Exception {
			return false;
		}

		@Override
		public EntryManager getEntryManager() {
			return null;
		}

		@Override
		public ResultSet spatialQuery(String[] returnType,Expression exp,Order order, Polygon poly) throws Exception{
			return null;
		}

	};
	
	public ClassDef getClassDef();
	
	public SubClassDef[] getSubClassDef();

	public FieldDef getFieldDef();
	
	public boolean add(Device device) throws Exception;
	
	public boolean modify(Device device) throws Exception;
	
	public boolean remove(Device device) throws Exception;
	
	public boolean add(Device device,boolean join) throws Exception;
	
	public boolean modify(Device device,boolean join) throws Exception;
	
	public boolean remove(Device device,boolean join) throws Exception;
	
	public ResultSet search(String[] returnType,Expression exp,Order order) throws Exception;
	
	public ResultSet search(CriteriaQuery query) throws Exception;
	
	public ResultSet spatialQuery(String[] returnType,Expression exp,Order order,Polygon poly) throws Exception;
	
	public boolean add(List<Device> devices) throws Exception;
	
	public boolean modify(List<Device> device) throws Exception;
	
	public boolean remove(List<Device> device) throws Exception;
	
	public Device createDevice() throws Exception;
	
	public Device createDevice(Device device) throws Exception;
	
	public boolean modify(ModifyFilter filter) throws Exception;
	
	public boolean remove(RemoveFilter filter) throws Exception;
	
	public boolean isVersion() throws Exception;
	
	public EntryManager getEntryManager();
}
