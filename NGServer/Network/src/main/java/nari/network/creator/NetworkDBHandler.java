package nari.network.creator;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import nari.Dao.bundle.bean.ResultHandler;
import nari.Dao.interfaces.DbAdaptor;
import nari.network.device.*;

public class NetworkDBHandler implements ResultHandler<NetworkGenerator> {

	private TopoDeviceFactory factory = null;
	private NetworkGenerator graphGenerator = null;
	private DbAdaptor dbAdaptor;
	private Map<Integer, Station> stations = null;
	private Map<Integer, BigFeeder> bigFeeders = null;
	private boolean isBus;
	private boolean isInStation;
	private boolean isTransformer;
	private boolean isSwitch;
	private boolean isConductor;
	private boolean hasBigFeeder;
	private boolean hasUsualSwitchCase;

	private Station emptyStation = null;
    private BigFeeder emptyBigFeeder = null;
	
	public NetworkDBHandler(NetworkGenerator graphGenerator,
							TopoDeviceFactory factory,
							DbAdaptor dbAdaptor,
							Map<Integer, Station> stations,
							Map<Integer, BigFeeder> bigFeeders,
							boolean isBus,
							boolean isInStation,
							boolean isTransformer,
							boolean isSwitch,
							boolean isConductor,
							boolean hasBigFeeder,
							boolean hasUsualSwitchCase) {
		this.graphGenerator = graphGenerator;
		this.factory = factory;
		this.dbAdaptor = dbAdaptor;
		this.stations = stations;
		this.bigFeeders = bigFeeders;
		this.isBus = isBus;
		this.isInStation = isInStation;
		this.isTransformer = isTransformer;
		this.isSwitch = isSwitch;
		this.isConductor = isConductor;
		this.hasBigFeeder = hasBigFeeder;
		this.hasUsualSwitchCase = hasUsualSwitchCase;

        emptyStation = this.factory.createStation(0, 0, 0);
        emptyBigFeeder = this.factory.createBigFeeder(0, 0, 0,
                0, 0, emptyStation);
	}
	
	@Override
	public NetworkGenerator handle(ResultSet rs) throws SQLException {
		
		try {
			while (rs.next()) {
				
				// 处理Connection字段
				long[] connectionNodes = parseConnection(rs);
				if (null == connectionNodes) {
					continue;
				}
				
				// 处理OID字段
				int oid = rs.getInt("OID");
				
				// 处理SBZLX字段
				int modelId = rs.getInt("SBZLX");
				
				// 处理DYDJ字段
				int voltageLevel = rs.getInt("DYDJ");
				
				// 处理开关状态字段
				int switchCase = 0;
				if (isSwitch) {
					switchCase = rs.getInt("KGZT");
				}
				
				// 构造拓扑设备
				TopoDevice device = null;
				if (isInStation) {
					// 站内设备
					Station station = getStation(rs.getInt("SSDZ"));
					if (isBus) {
						// 母线
						device = factory.createBusDevice(modelId, oid, voltageLevel, connectionNodes, station);
					} else if (isTransformer) {
						// 站内变压器
						device = factory.createInStationTransformerDevice(modelId, oid, voltageLevel, connectionNodes, station);
					} else if (isSwitch) {
						// 站内开关
						device = factory.createInStationSwitchDevice(modelId, oid, voltageLevel, connectionNodes, station, switchCase);
						if (hasUsualSwitchCase) {
							int usualSwitchCase = rs.getInt("CKZT");
							((Switch) device).setUsualSwitchCase(usualSwitchCase);
						}
					} else {
						// 站内其他设备
						device = factory.createInStationDevice(modelId, oid, voltageLevel, connectionNodes, station);
					}
				} else {
					// 站外设备
					if (isTransformer) {
						// 站外变压器
						device = factory.createTransformerDevice(modelId, oid, voltageLevel, connectionNodes);
					} else if (isSwitch) {
						// 站外开关
						device = factory.createSwitchDevice(modelId, oid, voltageLevel, connectionNodes, switchCase);
						if (hasUsualSwitchCase) {
							int usualSwitchCase = rs.getInt("CKZT");
							((Switch) device).setUsualSwitchCase(usualSwitchCase);
						}
					} else if (isConductor) {
						// 导线段 or 电缆段
						device = factory.createConductorDevice(modelId, oid, voltageLevel, connectionNodes);
					} else {
						// 站外其他设备
						device = factory.createTopoDevice(modelId, oid, voltageLevel, connectionNodes);
					}
				}

                device.setInBigFeeder(hasBigFeeder);
                if (hasBigFeeder) {
                    int bigFeederOid = rs.getInt("SSDKX");
                    BigFeeder bigFeeder = getBigFeeder(bigFeederOid);
                    device.setBigFeeder(bigFeeder);
                } else {
                    device.setBigFeeder(emptyBigFeeder);
                }
				
				graphGenerator.add(device);
			}
		} finally {
			rs.close();
		}
		return graphGenerator;
	}
	
	private Station getStation(int oid) {

	    if (oid == 0) {
	        return emptyStation;
        }

		Station station = stations.get(oid);
		if (null != station) {
			return station;
		}
		// 从数据库中查询
		Map<String,Object> record = null;
		try {
			record = dbAdaptor.findMap(
			        "select SBZLX, DYDJ from dwzy.T_TX_ZNYC_DZ where OID = "
					+ oid);
		} catch (SQLException e) {
			e.printStackTrace();
			return emptyStation;
		}
		
		if (null == record) {
			return emptyStation;
		}
		
		int modelId = 0, voltageLevel = 0;
		Object oModelId = record.get("sbzlx");
		Object oVoltageLevel = record.get("dydj");
		if (null != oModelId) {
			modelId = Integer.valueOf(String.valueOf(oModelId));
		}
		if (null != oVoltageLevel) {
			voltageLevel = Integer.valueOf(String.valueOf(oVoltageLevel));
		}
		station = factory.createStation(modelId, oid, voltageLevel);
		stations.put(oid, station);
		return station;
	}

	private BigFeeder getBigFeeder(int oid) {

        if (oid == 0) {
            return emptyBigFeeder;
        }

		BigFeeder bigFeeder = bigFeeders.get(oid);
		if (null != bigFeeder) {
			return bigFeeder;
		}
		// 从数据库中查询
		Map<String,Object> record = null;
		try {
			record = dbAdaptor.findMap(
					"select SBZLX, DYDJ, CXKG, CXKGLX, QSDZ from dwzy.T_TX_ZWYC_DKX where OID = "
					+ oid);
		} catch (SQLException e) {
			e.printStackTrace();
			return emptyBigFeeder;
		}

		if (null == record) {
			return emptyBigFeeder;
		}

		int modelId = 0, voltageLevel = 0, outletSwitchType = 0,
                outletSwitchId = 0, startStationId = 0;
		Object oModelId = record.get("sbzlx");
        Object oVoltageLevel = record.get("dydj");
        Object oOutletSwitchType = record.get("cxkglx");
        Object oOutletSwitchId = record.get("cxkg");
        Object oStartStationId = record.get("qsdz");
		if (null != oModelId) {
			modelId = Integer.valueOf(String.valueOf(oModelId));
		}
		if (null != oVoltageLevel) {
			voltageLevel = Integer.valueOf(String.valueOf(oVoltageLevel));
		}
        if (null != oOutletSwitchType) {
            outletSwitchType = Integer.valueOf(String.valueOf(oOutletSwitchType));
        }
        if (null != oOutletSwitchId) {
            outletSwitchId = Integer.valueOf(String.valueOf(oOutletSwitchId));
        }
        if (null != oStartStationId) {
            startStationId = Integer.valueOf(String.valueOf(oStartStationId));
        }
        Station startStation = getStation(startStationId);
		bigFeeder = factory.createBigFeeder(
		        modelId, oid, voltageLevel, outletSwitchType, outletSwitchId, startStation);
		bigFeeders.put(oid, bigFeeder);
		return bigFeeder;
	}

	private long[] parseConnection(ResultSet rs) throws SQLException {
		
		Blob blob = rs.getBlob("CONNECTION");
		if (null == blob || blob.length() <= 0) {
			return null;
		}
		InputStream is = blob.getBinaryStream();
		int terminalCount = 0;
		try {
			terminalCount = is.read();	//第一个byte代表拓扑点个数
		} catch (IOException e) {
			return null;
		}
		long blength = blob.length();
		if (terminalCount == 0 || blength != (8 * terminalCount + 1) ) {	//拓扑点个数*8 + 1即为blob的长度（8byte表示一个拓扑点id）
			return null;
		}
		
		byte[] bytes = new byte[8 * terminalCount];
		try {
			if (8 * terminalCount != is.read(bytes, 0, 8 * terminalCount)) {
				return null;
			}
		} catch (IOException ex) {
			return null;
		}
		
		long[] connectionNodes = new long[terminalCount];
		int offset = 0;
		for (int i = 0; i < terminalCount; i++) {
			connectionNodes[i] = bytesToLong(bytes, offset);
			offset += 8;
		}
		return connectionNodes;
	}
	
	private long bytesToLong(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    int b2 = bytes[off + 2] & 0xFF;  
	    int b3 = bytes[off + 3] & 0xFF; 
	    int b4 = bytes[off + 4] & 0xFF;  
	    int b5 = bytes[off + 5] & 0xFF; 
	    int b6 = bytes[off + 6] & 0xFF; 
	    int b7 = bytes[off + 7] & 0xFF;
	    return (b7 << 56) | (b6 << 48) | (b5 << 40) | (b4 << 32) |
	    		(b3 << 24) | (b2 << 16) | (b1 << 8) | b0;  
	}

}
