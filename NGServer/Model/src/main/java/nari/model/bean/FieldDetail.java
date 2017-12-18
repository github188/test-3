package nari.model.bean;

import nari.model.device.FieldProcessor;

public interface FieldDetail {

	public static final FieldDetail NONE = new FieldDetail(){

		@Override
		public String getFieldId() {
			return null;
		}

		@Override
		public String getFieldName() {
			return null;
		}

		@Override
		public String getFieldAlias() {
			return null;
		}

		@Override
		public int getFieldIndex() {
			return 0;
		}

		@Override
		public int getDisplayOrder() {
			return 0;
		}

		@Override
		public String getDataType() {
			return null;
		}

		@Override
		public boolean isVisible() {
			return false;
		}

		@Override
		public boolean isEditable() {
			return false;
		}

		@Override
		public boolean isNullable() {
			return false;
		}

		@Override
		public boolean isCode() {
			return false;
		}

		@Override
		public int getFieldLength() {
			return 0;
		}

		@Override
		public FieldProcessor getProcessor() {
			return null;
		}
		
	};
	
	public String getFieldId();
	
	public String getFieldName();
	
	public String getFieldAlias();
	
	public int getFieldIndex();
	
	public int getDisplayOrder();
	
	public String getDataType();
	
	public boolean isVisible();
	
	public boolean isEditable();
	
	public boolean isNullable();
	
	public boolean isCode();
	
	public int getFieldLength();
	
	public FieldProcessor getProcessor();
}
