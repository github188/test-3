package nari.parameter.code;

import java.util.HashMap;
import java.util.Map;

public class TopoResult {

	private static Map<Integer,String> map = new HashMap<Integer,String>();
	
	private int code;
	
	private String msg;
	
	static{
		map.put(0, "没有错误");
		map.put(401,"后台服务结果文件未更新");
		map.put(403,"前台参数异常无法处理");
		map.put(1000,"一般参数错误");          
		map.put(1001,"无效的拓扑类型");
		map.put(1002,"无效的检测类型");
		map.put(1003,"无效的最大层数");
		map.put(1004,"找不到EID");
		map.put(1005,"没有找到相应的结果");
		map.put(1006,"无效的拓扑对象类型：边/点");
		map.put(1007,"无效的拓扑对象模型：站内/站外/自动装置等(即设备表中的flag字段值)");
		map.put(1008,"无效的电压等级");
		map.put(1009,"无效的表名");
		map.put(1010,"找到多个电源点");
		map.put(1011,"找到单个电源点");
		map.put(1012,"没有找到电源点");
		map.put(1013,"初始化失败");
		map.put(1014,"数据预处理失败");
		map.put(1015,"找不到相应的图实例");
		map.put(1016,"无效的对象设备（对象ID错误）");
		map.put(1017,"无效的搜索模式（图形拓扑/电气拓扑）");
		map.put(1018,"无效的电压等级条件设置");
		map.put(1019,"无效的电源点设置");
		map.put(1020,"无效的模拟设备状态");
		map.put(1021,"无效的主干线模式参数");
		map.put(1022,"无效的拓扑搜索条件");
		map.put(1023,"找不到对应的对象属性");
		map.put(1024,"没有发现通路（最短路径搜索）");
		map.put(1025,"无法定位电流方向");
		map.put(1026,"停电范围分析失败没有符合条件搜索到所有入参设备");
		map.put(1027,"设备没有对应关联的拓扑设备");
		map.put(1028,"变压器直接相连的周边设备电压等级有问题");
		map.put(1029,"设备输入格式有误");
		map.put(1030,"服务内存控制没初始化");
		map.put(1031,"服务找不到对应图类型的内存块");
		map.put(1032,"服务根据图实例找不到对应图类型");
		map.put(1033,"入参版本名称找不到");
		map.put(1034,"服务未初始化完");
		map.put(1035,"服务不支持此接口");
		map.put(1036,"找不到对应的NODEID");
		map.put(1037,"接口入参有问题");
		map.put(1038,"设备已有供电电源，不能将入参设备设为电源点");
		map.put(1039,"设备本身不是电源点");
	}

	public TopoResult(int code) {
		super();
		this.code = code;
	}

	public static Map<Integer, String> getMap() {
		return map;
	}

	public static void setMap(Map<Integer, String> map) {
		TopoResult.map = map;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
