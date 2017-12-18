package nari.network.creator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import nari.Dao.bundle.bean.ResultHandler;
import nari.Dao.interfaces.DbAdaptor;
import nari.network.device.BigFeeder;
import nari.network.device.Station;
import nari.network.device.TopoDeviceFactory;

/**
 * 一个拓扑网络构建的单元
 * @author birderyu
 *
 */
public class NetworkRunnable implements Runnable {
	
	private NetworkGenerator networkGenerator;
	
	private NetworkAssignmenter networkAssignmenter;

	private DbAdaptor dbAdaptor;
	
	private int threadId;
	
	private CountDownLatch threadSignal = null;
	
	private TopoDeviceFactory factory = null;
	
	private Map<Integer, Station> stations = null;

	private Map<Integer, BigFeeder> bigFeeders = null;
	
	private int[] voltageLevelList = null;
	
	public NetworkRunnable(
			int threadId,
			CountDownLatch threadSignal,
			DbAdaptor dbAdaptor,
			NetworkAssignmenter networkAssignmenter,
			NetworkGenerator networkGenerator,
			TopoDeviceFactory factory,
			int[] voltageLevelList,
			Map<Integer, Station> stations,
			Map<Integer, BigFeeder> bigFeeders) {
		
		this.threadId = threadId;
		this.threadSignal = threadSignal;
		this.dbAdaptor = dbAdaptor;
		this.networkAssignmenter = networkAssignmenter;
		this.networkGenerator = networkGenerator;
		this.factory = factory;
		this.voltageLevelList = voltageLevelList;
		this.stations = stations;
		this.bigFeeders = bigFeeders;
	}

	@Override
	public void run() {
		
		System.out.println(networkAssignmenter.progress() + " 线程" + String.valueOf(threadId + 1) + "开始运行...");
		
		while(!networkAssignmenter.isEmpty()) {
			
			NetworkAssignmenter.TopoTask task = networkAssignmenter.pop();
			if (null == task) {
				break;
			}
			
			int modelId = task.getModelId();
			String tableName = task.getTableName();

			final Set<String> fieldNames = new HashSet<String>();
			String sql = "select * from dwzy." + tableName + " where 1=0";

			try {
				this.dbAdaptor.query(sql, new ResultHandler<Object>() {

					@Override
					public Object handle(ResultSet result) throws SQLException {

						ResultSetMetaData meta = result.getMetaData();

						int num = meta.getColumnCount();
						for (int i = 1; i <= num; i++) {
							fieldNames.add(meta.getColumnName(i).toUpperCase());
						}
						return null;

					}
				});
			} catch (SQLException e) {
				e.printStackTrace();
				continue;
			}

			if (!fieldNames.contains("CONNECTION")) {
				// 不包含Connection字段
				continue;
			}

			if (!fieldNames.contains("DYDJ")) {
				// 不包含电压等级字段
				continue;
			}

			// 是否是母线
			boolean isBus = false;
			if (31100000 == modelId || 212500000 == modelId || 321700000 == modelId) {
				isBus = true;
			}

			// 站内设备
			boolean isInStation = false;
			if (tableName.contains("T_TX_ZNYC_")) {
				isInStation = true;
			}

			// 变压器
			boolean isTransformer = false;
			if (tableName.contains("BYQ")) {
				isTransformer = true;
			}

			// 开关状态
			boolean isSwitch = false;
			if (fieldNames.contains("KGZT")) {
				isSwitch = true;
			}
			
			// 常开状态
			boolean hasUsualSwitchCase = false;
			if (fieldNames.contains("CKZT")) {
				hasUsualSwitchCase = true;
			}

			// 导线段 or 电缆断
			boolean isConductor = false;
			if (modelId == 10100000 || modelId == 20100000 ||
					modelId == 310100000 || modelId == 320100000) {
				isConductor = true;
			}

			boolean hasBigFeeder = false;
            if (fieldNames.contains("SSDKX")) {
				hasBigFeeder = true;
            }
            hasBigFeeder = false;

			StringBuilder dev_sql_builder = new StringBuilder("select OID, SBZLX, DYDJ, CONNECTION");
			if (isInStation) {
				dev_sql_builder.append(", SSDZ");
			}
			if (isSwitch) {
				dev_sql_builder.append(", KGZT");
			}
			if (hasBigFeeder) {
                dev_sql_builder.append(", SSDKX");
            }
			if (hasUsualSwitchCase) {
				dev_sql_builder.append(", CKZT");
			}

			dev_sql_builder.append(" from dwzy." + tableName + " where SBZLX = " + modelId);
			if (null != voltageLevelList && voltageLevelList.length > 0) {
				dev_sql_builder.append(" and DYDJ in (");
				for (int i = 0; i < voltageLevelList.length - 1; i++) {
					dev_sql_builder.append(voltageLevelList[i]);
					dev_sql_builder.append(", ");
				}
				dev_sql_builder.append(voltageLevelList[voltageLevelList.length - 1]);
				dev_sql_builder.append(")");
			}
			
			
			try {

				System.out.println(networkAssignmenter.progress() + " 线程" + String.valueOf(threadId + 1) + "开始导入："
						+ modelId + "（" + tableName + "）。");
				
				dbAdaptor.query(dev_sql_builder.toString(),
						new NetworkDBHandler(networkGenerator, factory, dbAdaptor,
								stations,  bigFeeders,
								isBus, isInStation, isTransformer, isSwitch,
								isConductor, hasBigFeeder, hasUsualSwitchCase));
				
				System.out.println(networkAssignmenter.progress() + " 线程" + String.valueOf(threadId + 1) + "导入成功："
						+ modelId + "（" + tableName + "）。");
				
			} catch (SQLException e) {
				System.out.println(networkAssignmenter.progress() + " 线程" + String.valueOf(threadId + 1) + "导入失败："
						+ modelId + "（" + tableName + "）。");
				e.printStackTrace();
			}
		}
		
		threadSignal.countDown();
		System.out.println(networkAssignmenter.progress() + " 线程" + String.valueOf(threadId + 1) + "运行结束。");
	}

}
