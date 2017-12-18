package nari.MainGridService.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.MainGridServiceActivator;
import nari.model.device.ModelService;
import nari.parameter.MainGridService.FuzzyQuery.FuzzyQueryRequest;
import nari.parameter.MainGridService.FuzzyQuery.FuzzyQueryResponse;
import nari.parameter.bean.FuzzyQueryDevice;
import nari.parameter.bean.YXDWMessage;
import nari.parameter.code.ReturnCode;

public class FuzzyQueryHandler {
	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
//	Map<String,Object> YXDWMap = MainGridServiceActivator.YXDWMap;
//	DeviceModel model = DeviceModel.NONE;
//	Expression exp = Expression.NONE; // 总条件

	public FuzzyQueryResponse fuzzyQuery(FuzzyQueryRequest req) {
		FuzzyQueryResponse resp = new FuzzyQueryResponse();
		String fuzzyString = req.getFuzzyString();
		String[] eachString = fuzzyString.trim().split(" ");
		String[] provinceCondition = req.getProvinceCondition();
		StringBuffer provinceConditionSql = new StringBuffer();
		String sql = "";
		//线路单独sql
		String XLSql = "select * from T_TX_ZWYC_XL where SBMC like '%"
				+ eachString[0]
				+ "%' and dydj in (85，84,83,82，37,36,35) order by SBMC";
		//网省sql
		String provinceSql = "select a.bmmc,a.isc_id,b.bmmc as wsgs from ISC_SPECIALORG_UNIT_LOCEXT a left join ISC_SPECIALORG_UNIT_LOCEXT b on a.sswsid = b.isc_id";
		//根据所属网省条件得到对应运行单位条件
		if(provinceCondition == null || provinceCondition.length == 0){
			sql = "select * from ("+provinceSql+") c,("+XLSql+") d "+"where c.isc_id = d.yxdw";
		}else{
			provinceConditionSql.append("c.wsgs like '%"+provinceCondition[0]+"%'");
			for(int i=1;i<provinceCondition.length;i++){
				provinceConditionSql.append(" or c.wsgs like '%"+provinceCondition[i]+"%'");
			}
			sql = "select * from ("+provinceSql+") c,("+XLSql+") d "+
					" where "+provinceConditionSql.toString()+" and c.isc_id = d.yxdw";
		}
		//首先查询线路
		List<Map<String, Object>> XLList = null;
		try {
			XLList = db.findAllMap(sql);
		} catch (SQLException e) {
			System.out.println("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		// 一个代表线路(或者电站)
		if (eachString.length == 1) {
			// 若为电站
			List<Map<String, Object>> DZList = null;
			String DZSql = "select * from T_TX_ZNYC_DZ where SBMC like '%"
					+ eachString[0]
					+ "%' and dydj in (85，84,83,82，37,36,35) order by SBMC";
			if(provinceCondition == null || provinceCondition.length == 0){
				sql = "select * from ("+provinceSql+") c,("+DZSql+") d "+"where c.isc_id = d.yxdw";
			}else{
				sql = "select * from ("+provinceSql+") c,("+DZSql+") d "+
						" where "+provinceConditionSql.toString()+" and c.isc_id = d.yxdw";
			}
			try {
				DZList = db.findAllMap(sql);
			} catch (SQLException e) {
				System.out.println("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			// 得到设备所需相应属性
			int XLCount = 0;
			if (XLList != null) {
				XLCount = XLList.size();
			}
			int DZCount = 0;
			if (DZList != null) {
				DZCount = DZList.size();
			}
			FuzzyQueryDevice[] fuzzyDevices = new FuzzyQueryDevice[(XLCount + DZCount)];
			// 线路的
			for (int i = 0; i < XLCount; i++) {
				fuzzyDevices[i] = new FuzzyQueryDevice();
				fuzzyDevices[i].setSbmc(String.valueOf(XLList.get(i)
						.get("sbmc")));
				YXDWMessage yxdwMessage = new YXDWMessage();
				yxdwMessage.setBmmc(String.valueOf(XLList.get(i)
						.get("bmmc")));
				yxdwMessage.setWsgs(String.valueOf(XLList.get(i)
						.get("wsgs")));
				fuzzyDevices[i].setYxdwMessage(yxdwMessage);
				fuzzyDevices[i]
						.setoId(String.valueOf(XLList.get(i).get("oid")));
				fuzzyDevices[i].setSearchClassid("100000");
			}
			// 电站的
			for (int j = 0; j < DZCount; j++) {
				fuzzyDevices[j + XLCount] = new FuzzyQueryDevice();
				fuzzyDevices[j + XLCount].setSbmc(String.valueOf(DZList.get(j)
						.get("sbmc")));
				YXDWMessage yxdwMessage = new YXDWMessage();
				yxdwMessage.setBmmc(String.valueOf(DZList.get(j)
						.get("bmmc")));
				yxdwMessage.setWsgs(String.valueOf(DZList.get(j)
						.get("wsgs")));
				fuzzyDevices[j + XLCount].setYxdwMessage(yxdwMessage);
				fuzzyDevices[j + XLCount].setoId(String.valueOf(DZList.get(j)
						.get("oid")));
				fuzzyDevices[j + XLCount].setSearchClassid("300000");
			}
			resp.setFuzzyDevice(fuzzyDevices);
			return resp;
		}

		// 2,3个代表运行杆塔
		if (XLList == null) {
			System.out.println("查无数据");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if (eachString.length == 2 || (eachString.length == 3)) {
			List<Map<String, Object>> startGTList = null;
			List<Map<String, Object>> endGTList = null;
			
//			//先查杆塔
//			try {
//				model = ms.fromClass("102000", false);
//			} catch (ModelException e) {
//				System.out.println("模型创建时出错");
//				resp.setCode(ReturnCode.BUILDMODEL);
//				return resp;
//			}
//			// 杆塔模糊查询条件
//			CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
//			Expression SSXLExp = Expression.NONE; 
			// 得到所属线路OID的集合
			int XLCount = XLList.size();
			// 由于sql原因 最多取90个
			if (XLList.size() > 90) {
				XLCount = 90;
			}
//			String[] XLOid = new String[XLCount];
//			for (int i = 0; i < XLCount; i++) {
//				XLOid[i] = String.valueOf(XLList.get(i).get("oid"));
//			}
//			SSXLExp = builder.in(builder.getRoot().get("SSXL", String.class),XLOid);	// 所属线路条件
//			Order nameOrder = Order.NONE; 	// 排序条件
//			nameOrder = builder.asc(builder.getRoot().get("SBMC", String.class));
			
			//创所属线路条件sql语句
			StringBuffer ssxlbf = new StringBuffer();
			ssxlbf.append("(ssxl in ("+String.valueOf(XLList.get(0).get("oid")));
			for (int i = 1; i < XLCount; i++) {
				ssxlbf.append(","+String.valueOf(XLList.get(i).get("oid")));
			}
			ssxlbf.append(")) ");
			for (int i = 1; i < eachString.length; i++) {
			String GTSql = "select * from T_TX_ZWYC_YXGT where SBMC like '%"+eachString[i]+ "%' " +
					"and " +ssxlbf.toString()+
					" and dydj in (85，84,83,82，37,36,35) order by SBMC";
			if(provinceCondition == null || provinceCondition.length == 0){
				sql = "select * from ("+provinceSql+") c,("+GTSql+") d "+"where c.isc_id = d.yxdw";
			}else{
				sql = "select * from ("+provinceSql+") c,("+GTSql+") d "+
						" where "+provinceConditionSql.toString()+" and c.isc_id = d.yxdw";
			}
			try {
				if (i == 1) {
					startGTList = db.findAllMap(sql);
				}else{
					endGTList = db.findAllMap(sql);
				}
			} catch (SQLException e) {
				System.out.println("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			}
			//若无终止杆，即查杆塔
			if (startGTList == null) {
				System.out.println("查无数据");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			//返回杆塔数据
			FuzzyQueryDevice[] fuzzyDevices = new FuzzyQueryDevice[(startGTList.size())];
			if (endGTList == null || endGTList.isEmpty()) {
					for(int i=0;i<startGTList.size();i++){
						fuzzyDevices[i] = new FuzzyQueryDevice();
						fuzzyDevices[i].setSbmc(String.valueOf(startGTList.get(i)
								.get("sbmc")));
						YXDWMessage yxdwMessage = new YXDWMessage();
						yxdwMessage.setBmmc(String.valueOf(startGTList.get(i)
								.get("bmmc")));
						yxdwMessage.setWsgs(String.valueOf(startGTList.get(i)
								.get("wsgs")));
						fuzzyDevices[i].setYxdwMessage(yxdwMessage);
						fuzzyDevices[i]
								.setoId(String.valueOf(startGTList.get(i).get("oid")));
						fuzzyDevices[i].setSearchClassid("102000");
					}
					resp.setFuzzyDevice(fuzzyDevices);
					return resp;
				}else{
					//否则就是查导线段
					List<Map<String, Object>> DXDList = null;
					String qsNameString = "sbmc like '%" +eachString[0]+"%"+eachString[1]+"%' ";
					String zzNameString = "sbmc like '%" +eachString[0]+"%"+eachString[2]+"%' ";
					String optimumCondition = qsNameString + " and " + zzNameString;	//最适条件
					String DXDSql = "select * from T_TX_ZWYC_XL where "
							+ optimumCondition+ " and dydj in (85，84,83,82，37,36,35) order by SBMC";
					if(provinceCondition == null || provinceCondition.length == 0){
						sql = "select * from ("+provinceSql+") c,("+DXDSql+") d "+"where c.isc_id = d.yxdw";
					}else{
						sql = "select * from ("+provinceSql+") c,("+DXDSql+") d "+
								" where "+provinceConditionSql.toString()+" and c.isc_id = d.yxdw";
					}
					try {
						DXDList = db.findAllMap(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					//若无数据，则模糊查导线段
					if(DXDList == null){
						String DXDFuzzyCondition = qsNameString + " or " + zzNameString;	//模糊条件
						DXDSql = "select * from T_TX_ZWYC_XL where "
								+ DXDFuzzyCondition+ " and dydj in (85，84,83,82，37,36,35) order by SBMC";
						if(provinceCondition == null || provinceCondition.length == 0){
							sql = "select * from ("+provinceSql+") c,("+DXDSql+") d "+"where c.isc_id = d.yxdw";
						}else{
							sql = "select * from ("+provinceSql+") c,("+DXDSql+") d "+
									" where "+provinceConditionSql.toString()+" and c.isc_id = d.yxdw";
						}
						try {
							DXDList = db.findAllMap(sql);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						if(DXDList == null){
							System.out.println("查无数据");
							resp.setCode(ReturnCode.SQLERROR);
							return resp;
						}
					}
					//返回导线段数据
					fuzzyDevices = new FuzzyQueryDevice[(DXDList.size())];
					for(int i=0;i<DXDList.size();i++){
						fuzzyDevices[i] = new FuzzyQueryDevice();
						fuzzyDevices[i].setSbmc(String.valueOf(DXDList.get(i)
								.get("sbmc")));
						YXDWMessage yxdwMessage = new YXDWMessage();
						yxdwMessage.setBmmc(String.valueOf(DXDList.get(i)
								.get("bmmc")));
						yxdwMessage.setWsgs(String.valueOf(DXDList.get(i)
								.get("wsgs")));
						fuzzyDevices[i].setYxdwMessage(yxdwMessage);
						fuzzyDevices[i]
								.setoId(String.valueOf(DXDList.get(i).get("oid")));
						fuzzyDevices[i].setSearchClassid("101000");
					}
					resp.setFuzzyDevice(fuzzyDevices);
					return resp;
				}
		}

		 else {
			System.out.println("输入有误");
		}
		return resp;
	}
}
