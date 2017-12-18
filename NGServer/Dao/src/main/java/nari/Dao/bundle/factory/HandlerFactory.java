package nari.Dao.bundle.factory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nari.Dao.bundle.handler.DefaultMapResultHandler;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 生成handler工厂类
 * @author
 * @version 
 */
public class HandlerFactory {
	
	public static final int ARRAY_LIST 	= 0x10;
	public static final int LINKED_LIST = 0x20;
	
	public static <T> List<T> getResultCollection(int resultType) {
		List<T> collection = null;
		switch (resultType) {
			case ARRAY_LIST : 
				collection = new ArrayList<T>();
			break;
			
			case LINKED_LIST : 
				collection = new LinkedList<T>();
			break;
		}
		return collection;
	}
	
	/** 默认的Map形式结果集处理器 */
	public static ResultSetHandler<List<Map<String,Object>>> getMapHandler() {
		return new DefaultMapResultHandler();
	}
	
	/** 简单的ORM形式的结果集处理器 */
//	public static ResultSetHandler getBeanHandler(Class<?> bean) {
//		return new BeanListResultHandler<>(bean);
//	}
}
