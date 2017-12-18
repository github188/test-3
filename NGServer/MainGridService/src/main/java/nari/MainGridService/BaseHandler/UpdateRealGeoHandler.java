package nari.MainGridService.BaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.MainGridServiceActivator;
import nari.parameter.bean.YXDWMessage;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class UpdateRealGeoHandler {
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	Map<String,YXDWMessage> YXDWMap = MainGridServiceActivator.YXDWMap;
	
	public boolean updateRealGeoHandler(String tableName){
		boolean flag = false;
		if(tableName.equalsIgnoreCase("t_TX_ZWYC_DXD")){	//纠偏线
			List<Map<String,Object>> fakeList = new ArrayList<Map<String,Object>>();
			
			//查询元数据
			String fakeSql = "select a.shape.SDO_ORDINATES,a.oid,d.bmmc from "+ tableName +
					" a,(select b.isc_id,c.bmmc  from " +
					"ISC_SPECIALORG_UNIT_LOCEXT b left join ISC_SPECIALORG_UNIT_LOCEXT c on b.sswsid = c.isc_id) d " +
					"where a.yxdw = d.isc_id and a.DYDJ in (85,37)";
			try {
				fakeList = db.findAllMap(fakeSql);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("查询数据出错");
				return false;
			}
			if(fakeList == null || fakeList.size() == 0){
				System.out.println("查询无数据");
				return false;
			}
			//对每条记录进行纠偏
			for(int i=0;i<fakeList.size();i++){
				Map<String,Object> eachFakeMap = fakeList.get(i);
				//若记录属于江苏，浙江，上海，福建，湖南，西藏则不作处理
				String SSWS = String.valueOf(eachFakeMap.get("bmmc"));
				
				if(SSWS.contains("江苏")
					||SSWS.contains("浙江")
					||SSWS.contains("上海")
					||SSWS.contains("福建")
					||SSWS.contains("湖南")
					||SSWS.contains("西藏")){
					continue;
				}
				ARRAY odinates = (ARRAY)eachFakeMap.get("shape.sdo_ordinates");
				double[] odinatesArray = null;
				try {
					odinatesArray = odinates.getDoubleArray();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				for(int j=0;j<odinatesArray.length/2;j++){
					//将x还原(-0.002)
					odinatesArray[2*j] = odinatesArray[2*j]-0.002;
					//将y还原(-0.003)
					odinatesArray[2*j+1] = odinatesArray[2*j+1]-0.003;
				}
				//得到数据库对应字段
				ARRAY oracleOdinatesArray = null;
				Connection conn = null;
				try {
					String OracleDriver = "oracle.jdbc.driver.OracleDriver";
					String OracleURL = "jdbc:oracle:thin:@10.144.15.216:1521/pms";
					String OracleUserName = "dwzy";
					String OracleUserPassword = "dwzy";
					try {
						Class.forName(OracleDriver);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					conn = DriverManager.getConnection(OracleURL, OracleUserName, OracleUserPassword);
					ArrayDescriptor adescriptor = new ArrayDescriptor("MDSYS.SDO_ORDINATE_ARRAY",conn);
					oracleOdinatesArray = new ARRAY(adescriptor, conn, odinatesArray);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					if(conn!=null){
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				//将纠偏完得点更新至数据库(根据oid更新对应位置)
				String oid = String.valueOf(eachFakeMap.get("oid"));
				String updSql = "update "+tableName+"real a set a.shape.sdo_ordinates = ? where DYDJ in (85,37) and a.oid = "+oid;
				Object[] params = new Object[1];
				params[0] = oracleOdinatesArray;
				try {
					flag = db.update(updSql, params);
					if(!flag){
						System.out.println("中途出错");
						db.getConnection().rollback();
						break;	//退出纠偏
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(i%100==0){
				System.out.println("以纠偏"+i+"条");
				}//每100个输出一次
			}	//纠偏结束
			try {
				db.getConnection().commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{	//纠偏点
			List<Map<String,Object>> fakeList = new ArrayList<Map<String,Object>>();
			
			//查询元数据
			String fakeSql = "select a.shape.SDO_POINT.X,a.shape.SDO_POINT.y,a.oid,a.yxdw from "+ tableName +
					" a where a.dydj in (85,37)";
			try {
				fakeList = db.findAllMap(fakeSql);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("查询数据出错");
				return false;
			}
			if(fakeList == null || fakeList.size() == 0){
				System.out.println("查询无数据");
				return false;
			}
			//对每条记录进行纠偏
			for(int i=0;i<fakeList.size();i++){
				Map<String,Object> eachFakeMap = fakeList.get(i);
				//若记录属于江苏，浙江，上海，福建，湖南，西藏则不作处理
				String yxdw = String.valueOf(eachFakeMap.get("yxdw"));
				if(YXDWMap.get(yxdw) == null){
					continue;
				}
				String SSWS = YXDWMap.get(yxdw).getBmmc();
				if(SSWS.contains("江苏")
					||SSWS.contains("浙江")
					||SSWS.contains("上海")
					||SSWS.contains("福建")
					||SSWS.contains("湖南")
					||SSWS.contains("西藏")){
					continue;
				}
				double doubleX = Double.valueOf(String.valueOf(eachFakeMap.get("shape.sdo_point.x")));
				//将y还原(-0.002)
				String doubleXString = String.valueOf(doubleX-0.002);
				double doubleY = Double.valueOf(String.valueOf(eachFakeMap.get("shape.sdo_point.y")));
				//将y还原(-0.003)
				String doubleYString = String.valueOf(doubleY-0.003);
				//将纠偏完得点更新至数据库(根据oid更新对应位置)
				String oid = String.valueOf(eachFakeMap.get("oid"));
				String updSql = "update "+tableName+"real a set a.shape.SDO_POINT.X = "+doubleXString+",a.shape.SDO_POINT.y = "+doubleYString+" where DYDJ in (85,37) and a.oid = "+oid;
				try {
					flag = db.update(updSql);
					if(!flag){
						System.out.println("中途出错");
						db.getConnection().rollback();
						break;	//退出纠偏
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(i%100==0){
				System.out.println("以纠偏"+i+"条");
				}//每100个输出一次
			}	//纠偏结束
			try {
				db.getConnection().commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		return flag;
	}
}
