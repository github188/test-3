package nari.BaseService.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.CoordinateSequence;
import nari.Geometry.DefaultCoordinateSequence;
import nari.Geometry.GeometryBuilder;
import nari.Geometry.GeometryType;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.model.TableName;
import nari.parameter.BaseService.UpdXLGeoMessage.UpdXLGeoMessageRequest;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

public class UpdXLGeoMessageHandler {

	DbAdaptor db = BaseServiceActivator.dbAdaptor;
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	public boolean updXLGeoMessage(UpdXLGeoMessageRequest request){
		//得到所有线路信息
		String XLSql = "select * from " + TableName.T_TX_ZWYC_XL + " where dydj in (35,37,83,85)";
		List<Map<String,Object>> XLList = new ArrayList<Map<String,Object>>();
		try {
			XLList = db.findAllMap(XLSql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		if(XLList == null || XLList.size() == 0){
			logger.info("无数据");
			return false;
		}
		

//		String[] XLOid = request.getXLOid();
//		int recordNum = XLOid.length;
//		QueryResult[] result = new QueryResult[1];
		int recordNum = XLList.size();
//		QueryRecord[] records = new QueryRecord[recordNum];
		boolean flag = false;
		for(int i=0;i<recordNum;i++){
			//对每个xl进行塞值
//			records[i] = new QueryRecord();
			Map<String,Object> eachXLMap = XLList.get(i);
			String XLOid = String.valueOf(eachXLMap.get("oid"));
			//移除指定线路
			if(XLOid.equalsIgnoreCase("99392")
			   ||XLOid.equalsIgnoreCase("99170")
			   ||XLOid.equalsIgnoreCase("99391")
			   ||XLOid.equalsIgnoreCase("99169")){
				continue;
			}
//			String XLOid = "98588";
			List<Map<String,Object>> DXDList = new ArrayList<Map<String,Object>>();
			String DXDsql = "select * from " + TableName.T_TX_ZWYC_DXD + " where ssxl = " + XLOid;
			try {
				//得到所属线路导线段
				DXDList = db.findAllMap(DXDsql);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			//若该线路无导线段
			if(DXDList == null || DXDList.size() == 0){
				continue;
			}
			
			//将所有导线段取出变成所有的lineString坐标放入list
			List<double[]> ordinatesArrayList = new ArrayList<double[]>();	//放坐标点的List
			for(int j=0;j<DXDList.size();j++){
				String OID = String.valueOf(DXDList.get(j).get("oid"));
				
				//移除指定DXD
				if(OID.equals("110230367")
				   ||OID.equals("110230369")
				   ||OID.equals("110230371")
				   ||OID.equals("110230375")
				   ||OID.equals("110230400")
				   ||OID.equals("110230407")
				   ||OID.equals("110230408")
				   ||OID.equals("110230424")
				   ||OID.equals("110230439")
				   ||OID.equals("110230451")
				   ||OID.equals("110232677")){
					continue;
				}
					
				Object[] eachDXDOrdinatesArray = null;
				double[] eachLineStringOrdArray = null;
				Object shapeObject = DXDList.get(j).get("shape");
				if ("oracle.sql.STRUCT".equals(shapeObject.getClass().getName())){
					try {
						STRUCT struct = (STRUCT)shapeObject;
						JGeometry jGeometry = JGeometry.load(struct);
						eachDXDOrdinatesArray = jGeometry.getOrdinatesOfElements();
						for(Object obj: eachDXDOrdinatesArray){
							eachLineStringOrdArray = (double[])obj;
							//将每组坐标点添加进去
							ordinatesArrayList.add(eachLineStringOrdArray);
						}
							
						}catch(Exception e){
							e.printStackTrace();
						}
				}
			}
//			//对list排序
//			
//			Map<Integer,List<double[]>> orderedMap = new HashMap<Integer,List<double[]>>();	//所有排好序的点的集合
//			Map<Integer,Integer> relationMap = new HashMap<Integer,Integer>();	//头尾关系集合
//			//找到头,尾
//			List<Integer> headIndexList = new ArrayList<Integer>();	//头序号集合
//			List<Integer> tailIndexList = new ArrayList<Integer>();	//尾序号集合
//			if(i==47){
//				System.out.println("a");
//			}
//			for(int j=0;j<ordinatesArrayList.size();j++){
//				System.out.println("j="+j);
//				boolean headFlag = false;	//前面是否有导线段
//				boolean tailFlag = false;
//				double[] ordered = ordinatesArrayList.get(j);
//				for(int k=0;k<ordinatesArrayList.size();k++){
//					double[] compared = ordinatesArrayList.get(k);
//					if(ordered[0] == compared[compared.length-2]
//					&& ordered[1] == compared[compared.length-1]){
//						System.out.println("在j导线段前面的是k="+k);
//						headFlag = true;
//						continue;
//					}else if(ordered[ordered.length-2] == compared[0]
//						&& ordered[ordered.length-1] == compared[1]){
//						System.out.println("在j导线段后面的是k="+k);
//						//若有两个尾则还需判断
//						relationMap.put(j,k);
//						tailFlag = true;
//						continue;
//					}
//				}
//				//比较完了根据flag则说明是头，尾
//				if(!(headFlag)){
//					headIndexList.add(j) ;
//				}
//				if(!(tailFlag)){
//					tailIndexList.add(j);
//				}
//			}
//			//根据关系map将它从头到尾拼起来
//			//每个头都有一次循环
//			int[] startDouble = new int[headIndexList.size()];	//所有起始double数组
//			//所有坐标(double)塞入doubleList中
//			List<Double> doubleList = new ArrayList<Double>();	//排好序的所有坐标double的集合
//			int beforeSize = 0;
//			for(int j=0;j<headIndexList.size();j++){
//				List<double[]> orderedList = new ArrayList<double[]>();	//每个排好序的点的集合
//				int headIndex = headIndexList.get(j);
//				orderedList.add(ordinatesArrayList.get(headIndex));
//				//当没到尾 拼线段
//			while(!(tailIndexList.contains(headIndex))){
//				int backIndex = relationMap.get(headIndex);
//				headIndex = backIndex;
//				orderedList.add(ordinatesArrayList.get(backIndex));
//				}	
//			//每个合并都排好序后塞入map
//			orderedMap.put(j, orderedList);
//			double[] QSOrdinates = orderedMap.get(j).get(0);	//起始导线段
//				for(double a:QSOrdinates){
//				doubleList.add(a);
//				}	
//			
////			//从orderedList中间随机取5个
////			int DXDNum = orderedList.size();
////			for(int j=0;j<5;j++){
////				int randomNum = 1;
////				randomNum = randomNum + (int)((Math.random())*(DXDNum-2)+1);
////				double[] ZJOrdinates = orderedList.get(randomNum);	//中间导线段
////				for(double a:ZJOrdinates){
////					doubleList.add(a);
////				}
////			}
//			
//			
//			//从每个orderedList全取
//			int DXDOrderedNum = orderedList.size();
//				for(int k=1;k<DXDOrderedNum-1;k++){
//				double[] ZJOrdinates = orderedList.get(j);	//中间导线段
//				for(double a:ZJOrdinates){
//					doubleList.add(a);
//				}
//			}
//			
//			double[] ZZOrdinates = orderedList.get(DXDOrderedNum-1);	//终止导线段
//				for(double a:ZZOrdinates){
//				doubleList.add(a);
//				}
//				startDouble[j] = beforeSize+1;
//				beforeSize = doubleList.size();
//				
//			}	//所有dxd已排好序

			
			//用jst对list排序(即排每个lineString)
			GeometryFactory factory = new GeometryFactory();
			LineMerger lineMerger = new LineMerger();	//用于排序
			List<Geometry> geoList = new ArrayList<Geometry>();
			int lineStringNum = ordinatesArrayList.size();
			for(int j=0;j<lineStringNum;j++){
				double[] lineStringDoubles = ordinatesArrayList.get(j);
				int coordsNum = lineStringDoubles.length/2;
				Coordinate[] coords = new Coordinate[coordsNum];
				for(int k=0;k<coordsNum;k++){
					coords[k] = new Coordinate();
					coords[k].x = lineStringDoubles[2*k];
					coords[k].y = lineStringDoubles[2*k+1];
				}
				geoList.add(factory.createLineString(coords));
			}
			lineMerger.add(geoList);
			Collection<Geometry> OrderedCollection = lineMerger.getMergedLineStrings();	//排序完成
			
			int orderedLineStringNum = OrderedCollection.size();	//合并完后每条线路的lineString个数
			//所有坐标(double)塞入doubleList中
			List<Double> doubleList = new ArrayList<Double>();	//排好序的所有坐标double的集合
			//即合并完后对每条线操作
			int[] startDouble = new int[orderedLineStringNum];	//所有起始double数组
			int beforeSize = 0;
			//将每个合完的lineString放入doubleList中
			int startDoubleIndex = 0;
			for(Geometry geometry:OrderedCollection){
				Coordinate[] coords = geometry.getCoordinates();
				int coordsNum = coords.length;
				for(int k=0;k<coordsNum;k++){
					doubleList.add(coords[k].x);
					doubleList.add(coords[k].y);
				}
				
				startDouble[startDoubleIndex] = beforeSize+1;
				beforeSize = doubleList.size();
				startDoubleIndex++;
			}
			
			//得到线路geo数据
			double[] doubleArray = new double[doubleList.size()];
			for(int j=0;j<doubleList.size();j++){
				doubleArray[j] = doubleList.get(j);
			}
//			String doubleString = Arrays.toString(doubleArray).replace("[", "").replace("]", "");
//			String geoString = "";	//地理信息字符串
			CoordinateSequence[] seqs = new CoordinateSequence[orderedLineStringNum];
//			if(orderedMap.size()>1){	//若合并完不止一个线段
//				StringBuilder infoArray = new StringBuilder();
//				infoArray.append(startDouble[0]+",2,1");
//				for(int j=1;j<orderedMap.size();j++){
//					infoArray.append(","+startDouble[j]+",2,1");
//				}
				
				for(int j=0;j<orderedLineStringNum;j++){
					double[] coords = null;
					if(j==orderedLineStringNum-1){
						coords = new double[doubleList.size()-startDouble[j]+1];
					}else{
						
					coords = new double[startDouble[j+1]-startDouble[j]];
					}
					for(int k=0;k<coords.length;k++){
						coords[k] = doubleArray[startDouble[j]+k-1];
					}
					seqs[j] = new DefaultCoordinateSequence(coords);
				}
				GeometryType gtype = null;
				if(orderedLineStringNum>1){
					gtype = GeometryType.MULTIPOLYLINE;
				}else{
					gtype = GeometryType.POLYLINE;
				}
				if(seqs == null || seqs.length==0){
					continue;
				}
				JGeometry jgeom= GeometryBuilder.getBuilder().createGeometry(gtype, seqs);
//				geoString = "sdo_geometry(2002,null,null,sdo_elem_info_array("+infoArray.toString()+"),sdo_ordinate_array("+doubleString+"))";
//			}else{
//			geoString = "sdo_geometry(2002,null,null,sdo_elem_info_array(1,2,1),sdo_ordinate_array("+doubleString+"))";
//			}
				
				//用数据库连接变成Struct放入数据库
				STRUCT shapeSTRUT = null;
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
//					String sql = "";
//					ResultSet rs= conn.createStatement().executeQuery(sql);
//					conn = db.getConnection();
					shapeSTRUT = JGeometry.store(jgeom, conn);
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
			//得到对应字段名
			Object[] paramNames = eachXLMap.keySet().toArray();
			String paramNamesString = Arrays.toString(paramNames).replace("[", "").replace("]", "");
			
			//得到原表对应字段值
			Object[] param = eachXLMap.values().toArray();
			Object[] params = new Object[param.length+1];
			params[0] = shapeSTRUT;	//shape字段值
			for(int j=0;j<param.length;j++){
				if(param[j]!=null){
					params[j+1] = String.valueOf(param[j]);
				}
			}
			String UpdSql = "insert into " + TableName.T_TX_ZWYC_XLGeo + " (Shape,"+paramNamesString+") " +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			try {
				flag = db.save(UpdSql, params);
				if(!flag){
					logger.info("中途出错");
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.info(String.valueOf(params[12]));
				return false;
			}
		}//每次线路循环结束

		//最后事物的处理
		if(flag){
			try {
				db.getConnection().commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		try {
			db.getConnection().rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
