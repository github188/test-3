package nari.model.bean;

import java.util.Iterator;


public interface FieldDef {

	public final static FieldDef NONE = new FieldDef(){

		@Override
		public ClassDef getClassDef() {
			return null;
		}

		@Override
		public Iterator<FieldDetail> details() {
			return null;
		}

		@Override
		public FieldDetail find(String fieldName) {
			return null;
		}

		@Override
		public void addField() {
			
		}

	};
	
	public ClassDef getClassDef();
	
	public Iterator<FieldDetail> details();
	
	public FieldDetail find(String fieldName);
	
	public void addField();
	
}
