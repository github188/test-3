package nari.model;

public class TableName {
	
	public final static String DB_USER_NAME = "dwzy.";											// 数据库用户名
	//public final static String DB_USER_NAME = "";											// 数据库用户名

	public final static String CONF_OBJECTMETA = DB_USER_NAME + "CONF_OBJECTMETA";				// 对象定义表
	public final static String CONF_MODELMETA = DB_USER_NAME + "CONF_MODELMETA";					// 模型定义表
	public final static String CONF_MODELRELATION = DB_USER_NAME + "CONF_MODELRELATION"; 			// 模型关系表
	public final static String CONF_MODELSYMBOL = DB_USER_NAME + "CONF_MODELSYMBOL";				// 模型与符号映射表
	public final static String CONF_DOCUMENTMETA = DB_USER_NAME + "CONF_DOCUMENTMETA";			// 图定义表
	public final static String CONF_DOCUMENTMODEL = DB_USER_NAME + "CONF_DOCUMENTMODEL";			// 图类型与模型映射表
	public final static String CONF_DOCUMENTINSTANCE = DB_USER_NAME + "CONF_DOCUMENTINSTANCE";	// 图实例表
	
	public final static String CONF_RELATIONDEFINITION = DB_USER_NAME + "CONF_RELATIONDEFINITION";	// 关系定义表
	
	public final static String CONF_FIELDDEFINITION = DB_USER_NAME + "CONF_FIELDDEFINITION";		// 字段定义表
	public final static String CONF_CODEDEFINITION = DB_USER_NAME + "CONF_CODEDEFINITION";		// 码值定义表
	
	public final static String CONF_DISPLAYSCHEMA = DB_USER_NAME + "CONF_DISPLAYSCHEMA";			// 显示方案表
	public final static String CONF_RENDERRULE = DB_USER_NAME + "CONF_RENDERRULE";				// 渲染规则表
	public final static String CONF_LABELSTYLE = DB_USER_NAME + "CONF_LABELSTYLE";				// 标签风格表
	public final static String CONF_SYMBOLSTYLE = DB_USER_NAME + "CONF_SYMBOLSTYLE";				// 符号风格表
	
	public final static String CONF_BOOKMARK = DB_USER_NAME + "CONF_BOOKMARK";					// 书签表
	public final static String CONF_TILESCHEMA = DB_USER_NAME + "CONF_TILESCHEMA";
	public final static String CONF_TOPORELATION = DB_USER_NAME + "CONF_TOPORELATION";
	
	public final static String CONF_SERVICEPARAM = DB_USER_NAME + "CONF_SERVICEPARAM";			// 服务参数表
	public final static String CONF_SERVICEPARAMS = DB_USER_NAME + "CONF_SERVICEPARAMS";			// 服务参数表2
	
	public final static String T_TX_ZWYC_XL = DB_USER_NAME + "T_TX_ZWYC_XL";						// 线路表
	public final static String T_TX_ZWYC_XLGeo = DB_USER_NAME + "T_TX_ZWYC_XLGeo";				// 线路几何表
	public final static String T_TX_ZWYC_DXD = DB_USER_NAME + "T_TX_ZWYC_DXD";					// 导线段表
	public final static String T_TX_ZWYC_DLD = DB_USER_NAME + "T_TX_ZWYC_DLD";
	public final static String T_TX_ZWYC_ZSBYQ = DB_USER_NAME + "T_TX_ZWYC_ZSBYQ";
	public final static String T_TX_ZWYC_ZWLJX = DB_USER_NAME + "T_TX_ZWYC_ZWLJX";
	
	public final static String T_TX_ZNYC_DZ = DB_USER_NAME + "T_TX_ZNYC_DZ";
	public final static String T_TX_ZNYC_ZBYQ = DB_USER_NAME + "T_TX_ZNYC_ZBYQ";
	public final static String T_TX_ZNYC_PDBYQ = DB_USER_NAME + "T_TX_ZNYC_PDBYQ";
	
	public final static String T_TX_DYSB_DYDXD = DB_USER_NAME + "T_TX_DYSB_DYDXD";
	public final static String T_TX_DYSB_DYDLD = DB_USER_NAME + "T_TX_DYSB_DYDLD";

}
