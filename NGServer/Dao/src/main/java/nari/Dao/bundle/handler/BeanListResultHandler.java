package nari.Dao.bundle.handler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 简易的ORM工具，实现数据库结果集到自定义JavaBean的映射
 * 
 * <p><b>映射规则及注意事项：</b></p>
 * <ul>
 * 	<li>如果sql语句中的字段不使用别名，则使用数据库字段映射，否则使用别名来映射。</li>
 * 	<li>如果字段不含下划线("_")，将直接映射到JavaBean中的同名属性。</li>
 * 	<li>如果字段的各单词间以下划线连接，则首先会去掉将下划线，并使后一个单词的首字母大写来映射JavaBean中的属性。</li>
 * 	<li>字段或别名均不能以下划线("_")开头或结尾，否则将无法完成bean属性的设置。</li>
 * </ul>
 * 
 * <p>使用此类可以直接使用int,long,float,double来声明JavaBean的属性，而不用关心从BigDecimal的转换。
 * 
 * @see ListResultHandler
 * @author 
 * @since
 */
public class BeanListResultHandler<T> extends ListResultHandler<T> {
	
	private Class<?> beanClass;
	
	private Map<String, PropertyDescriptor> propMap;
	
	public BeanListResultHandler(Class<?> beanClass) {
		this.beanClass 	= beanClass;
		initPropertyDescriptorMap();
	}
	
	@Override
	protected T processRow(ResultSet rs) throws SQLException {
		T bean = newBeanInstance();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			int columnType = rsmd.getColumnType(i);
			String propertyName = mapPropertyName(rsmd.getColumnLabel(i));
			PropertyDescriptor prop = propMap.get(propertyName);
			if (prop != null) {
				Method setMethod   = prop.getWriteMethod();
				Class<?> propertyType = prop.getPropertyType();
				Object columnValue = getColumnValue(rs, i, columnType);
				setPropertyValue(setMethod, bean, smartTransfer(columnValue, propertyType));
			}
		}
		return bean;
	}
	
	/**
	 * 将列名映射为JavaBean属性名
	 */
	protected String mapPropertyName(String column) {
		String columnName = column.toLowerCase();
		if (columnName.contains("_")) {
			String[] nameArr = columnName.split("_");
			StringBuilder buff = new StringBuilder();
			buff.append(nameArr[0]);
			for (int i = 1; i < nameArr.length; i++) {
				char[] nameParts = nameArr[i].toCharArray();
				nameParts[0] = Character.toUpperCase(nameParts[0]);
				buff.append(nameParts);
			}
			return buff.toString();
		}
		return columnName;
	}
	
	/**
	 * 将BigDecimal转成对应的数字类型
	 */
	protected Object smartTransfer(Object columnValue, Class<?> propertyType) {
		if (columnValue != null && columnValue instanceof BigDecimal) {
			BigDecimal afterTransfer = (BigDecimal)columnValue;
			if ("int".equals(propertyType.getName())) {
				return afterTransfer.intValue();
			}
			if ("long".equals(propertyType.getName())) {
				return afterTransfer.longValue();
			}
			if ("float".equals(propertyType.getName())) {
				return afterTransfer.floatValue();
			}
			if ("double".equals(propertyType.getName())) {
				return afterTransfer.doubleValue();
			}
		}
		return columnValue;
	}
	
	
	/**
	 * 返回指定类型的结果集
	 */
	protected Object getColumnValue(ResultSet rs, int index, int columnType) throws SQLException {
		if (rs.getObject(index) == null) {
			return null;
		}
		switch (columnType) {
			case Types.CHAR		 	:	return rs.getString(index);
			case Types.VARCHAR	 	:	return rs.getString(index);
			case Types.INTEGER	 	: 	return rs.getInt(index);
			case Types.BIGINT	 	: 	return rs.getLong(index);
			case Types.FLOAT	 	: 	return rs.getFloat(index);
			case Types.DOUBLE	 	: 	return rs.getDouble(index);
			case Types.BOOLEAN	 	: 	return rs.getBoolean(index);
			case Types.DATE		 	: 	return rs.getTimestamp(index);
			case Types.BLOB		 	: 	return rs.getBlob(index);
			case Types.CLOB		 	: 	return rs.getClob(index);
			case Types.DECIMAL	 	: 	return rs.getBigDecimal(index);
			case Types.SMALLINT	 	: 	return rs.getShort(index);
			case Types.TIMESTAMP	: 	return rs.getTimestamp(index);
			default					:	return rs.getObject(index);
		}
	}
	
	
	/**
	 * 获取beanClass的属性描述符，并以属性名称为key放置在hashmap中
	 */
	private void initPropertyDescriptorMap() {
		if (propMap == null) {
			propMap = new HashMap<String, PropertyDescriptor>();
			BeanInfo beanInfo = null;
			try {
				beanInfo = Introspector.getBeanInfo(beanClass);
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}		
			for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
				propMap.put(prop.getName(), prop);
			}
		}
	}
	
	/**
	 * 创建bean对象
	 */
	@SuppressWarnings("unchecked")
	private T newBeanInstance() {
		T bean = null;
		try {
			bean = (T) beanClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 设置bean的属性值
	 */
	private void setPropertyValue(Method setMethod, T bean, Object value) {
		try {
			setMethod.invoke(bean, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
}
