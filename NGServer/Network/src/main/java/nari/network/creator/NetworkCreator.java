package nari.network.creator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.Dao.interfaces.DbAdaptor;
import nari.network.bean.NetworkConfig;
import nari.network.device.BigFeeder;
import nari.network.device.Station;
import nari.network.device.TopoDeviceFactory;
import nari.network.structure.Network;

/**
 * 拓扑网络构建器
 * 
 * @author birderyu
 *
 */
public class NetworkCreator {
	
	private DbAdaptor dbAdaptor = DbAdaptor.NONE;
	private NetworkConfig networkConfig = null;
	
	// 缓存所有的电站
	private Map<Integer, Station> stations = null;

	// 缓存所有的大馈线
	private Map<Integer, BigFeeder> bigFeeders = null;
	
	public NetworkCreator(DbAdaptor dbAdaptor, NetworkConfig networkConfig) {
		this.dbAdaptor = dbAdaptor;
		this.networkConfig = networkConfig;
		if (networkConfig.getThreadNum() > 1) {
			stations = new ConcurrentHashMap<Integer, Station>();
			bigFeeders = new ConcurrentHashMap<Integer, BigFeeder>();
		} else {
			stations = new HashMap<Integer, Station>();
			bigFeeders = new HashMap<Integer, BigFeeder>();
		}
	}

	public Network createNetwork() {

		long t1 = System.currentTimeMillis(); // 
		
		if (DbAdaptor.NONE == dbAdaptor ||
				null == networkConfig) {
			return null;
		}

		int threadNum = networkConfig.getThreadNum();
		if (0 == threadNum) {
			threadNum = 1;
		}

		NetworkAssignmenter networkAssignmenter = getTopoAssignmenter(threadNum, networkConfig);
		if (null == networkAssignmenter || networkAssignmenter.isEmpty()) {
			return null;
		}

		NetworkGenerator networkGenerator = NetworkGenerator.getTopoNetworkGenerator(threadNum);
		TopoDeviceFactory factory = new TopoDeviceFactory();
		
		ExecutorService executor = Executors.newFixedThreadPool(networkConfig.getThreadNum());
		CountDownLatch threadsSignal = new CountDownLatch(threadNum);
		System.out.println("开启线程数量：" + threadNum);
		for (int i = 0; i < threadNum; ++i) {
			NetworkRunnable runnable = new NetworkRunnable(
					i, threadsSignal, dbAdaptor,
                    networkAssignmenter, networkGenerator, factory,
                    networkConfig.getVoltageLevelList(),
					stations, bigFeeders);
			executor.execute(runnable);
		}
		try {
			threadsSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
		long t2 = System.currentTimeMillis(); // 
		
		Network network = (Network) networkGenerator.buildGraph();
		
		long t3 = System.currentTimeMillis(); // 
		
		System.out.println("加载数据用时：" + (t2 - t1) + "毫秒");
		System.out.println("构建网络用时：" + (t3 - t2) + "毫秒");
		System.out.println("共包含设备：" + network.getNodes().size());
		
		return network;
	}

	private NetworkAssignmenter getTopoAssignmenter(int threadNum, NetworkConfig networkConfig) {
		
		String sql = "select MODELID, MODELNAME, TERMINALDEF from dwzy.CONF_MODELMETA";
		List<Map<String, Object>> list = null;
		try {
			list = dbAdaptor.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		NetworkAssignmenter networkAssignmenter = NetworkAssignmenter.createAssignmenter(threadNum);
		int size = 0;
		if (null != list && !list.isEmpty()) {
			for (Map<String, Object> item : list) {
				if (null == item) {
					continue;
				}
				Object terminalDef = item.get("terminaldef");
				if (null == terminalDef || terminalDef.toString().isEmpty()) {
					// 不包含拓扑
					continue;
				}
				String modelName = item.get("modelname").toString();
				
				if (!networkConfig.isContainLowVoltage()) {
					// 只加载高压数据
					if (!modelName.contains("T_TX_ZWYC_") && !modelName.contains("T_TX_ZNYC_")) {
						continue;
					}
				}
				
				int modelId = Integer.valueOf(item.get("modelid").toString());
				networkAssignmenter.push(new NetworkAssignmenter.TopoTask(modelId, modelName));
				size++;
			}
		}
		networkAssignmenter.setMaxSize(size);
		return networkAssignmenter;
	}
}
