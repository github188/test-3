package nari.parameter.bean;

import java.io.Serializable;

public class Field implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6512272827302929635L;
		//该字段名称
		private String fieldName = "";
		
		//该字段别名
		private String fieldAlias = "";
		
		//该字段数据类型
		private String dataType = "";
		
		
		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldAlias() {
			return fieldAlias;
		}

		public void setFieldAlias(String fieldAlias) {
			this.fieldAlias = fieldAlias;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}
	}


