 package nari.distribution.TopoAnalysis.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nari.distribution.TopoAnalysis.bean.DeviceInfo;
import nari.distribution.TopoAnalysis.bean.MockOutageRangeAnalyze.MockOutageRangeAnalyzeRequest;
import nari.distribution.TopoAnalysis.bean.MockOutageRangeAnalyze.MockOutageRangeAnalyzeResponse;
import nari.model.device.DeviceKey;
import nari.model.device.DeviceModel;
import nari.network.device.Bus;
import nari.network.device.Switch;
import nari.network.device.TopoDevice;
import nari.network.interfaces.NetworkAdaptor;
import nari.network.structure.NetworkWalker;
import nari.parameter.code.PsrTypeSystem;
import nari.parameter.code.ReturnCode;

public class MockOutageRangeAnalyzeHandler 
	extends BaseTopoAnalyzeHandler {
	
	public MockOutageRangeAnalyzeResponse test(MockOutageRangeAnalyzeRequest req) {
		
		MockOutageRangeAnalyzeResponse resp = new MockOutageRangeAnalyzeResponse();
		if (null == req || !req.validate()) {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "传入参数有误"));
			return resp;
		}
		
		// 构造起始查询条件
		DeviceKey sourceDeviceKey = null; // 电源类型（出线开关）
		List<DeviceKey> openSwitchKeys = new ArrayList<DeviceKey>(); // 需要断开的开关
		if (req.getPsrTypeSys().equals(PsrTypeSystem.CLASS_ID)) {
			sourceDeviceKey = getDeviceKeyByClassId(req.getSourceType(), req.getSourceId());
			if (null != req.getOpenSwitch()) {
				for (DeviceInfo deviceInfo : req.getOpenSwitch()) {
					openSwitchKeys.add(getDeviceKeyByClassId(deviceInfo.getPsrType(), 
							deviceInfo.getPsrId()));
				}
			}
		} else if (req.getPsrTypeSys().equals(PsrTypeSystem.EQUIPMENT_ID)) {
			sourceDeviceKey = getDeviceKeyByEqumentId(req.getSourceType(), req.getSourceId());
			if (null != req.getOpenSwitch()) {
				for (DeviceInfo deviceInfo : req.getOpenSwitch()) {
					openSwitchKeys.add(getDeviceKeyByEqumentId(deviceInfo.getPsrType(), 
							deviceInfo.getPsrId()));
				}
			}
		} else if (req.getPsrTypeSys().equals(PsrTypeSystem.MODEL_ID)) {
			sourceDeviceKey = getDeviceKeyByModelId(req.getSourceType(), req.getSourceId());
			if (null != req.getOpenSwitch()) {
				for (DeviceInfo deviceInfo : req.getOpenSwitch()) {
					openSwitchKeys.add(getDeviceKeyByModelId(deviceInfo.getPsrType(), 
							deviceInfo.getPsrId()));
				}
			}
		} else {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "传入参数中的psrTypeSys有误"));
			return resp;
		}
		
		if (null == sourceDeviceKey) {
			// 未查找到起点设备
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "未能查找到起点设备"));
			return resp;
		}
		
		// 分析网络中的设备
		if (null == networkAdaptor || networkAdaptor == NetworkAdaptor.NONE) {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "未能查找到拓扑网络"));
			return resp;
		}
		
		// 电源类型
		TopoDevice sourceDevice = networkAdaptor.getDevice(sourceDeviceKey.getModelId(), sourceDeviceKey.getOid());
		if (null == sourceDevice) {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "未能在拓扑网络中查找到起点设备"));
			return resp;
		}
		
		// 第一遍追踪，单源追踪，不考虑开关状态，全部追踪
		// 收集追踪过程中出线的所有配电变压器
		final Set<TopoDevice> transformers = new HashSet<TopoDevice>(); 
		
		// 记录第二次搜索需要的电源
		final List<TopoDevice> sourceDevices = new ArrayList<TopoDevice>(); 

		long t1 = System.currentTimeMillis(); 
		networkAdaptor.search(
				sourceDevice, 
				new NetworkWalker() {

					@Override
					public int walk(TopoDevice last, TopoDevice current,
							List<TopoDevice> nexts) {
						
						// 判空操作
						if (null == current) {
							return NetworkWalker.PROCESS_EACH;
						}
						
						if (current.isBus()) {
							// 判断当前设备是否是母线
							Bus bus = current.asBus();
							if (bus.getVoltageLevel() !=
								bus.getStation().getVoltageLevel()) {
								// 母线的电压等级与母线所属电站的电压等级不同，说明母线作为当前轨迹的电源
								// 这里并不将母线作为电源，而是将母线的前一个设备作为电源
								// 因为母线会连接到别的配电网中，第二次追踪时会造成追踪路径的扩散
								sourceDevices.add(last);
								// 母线作为电源，需要杀掉当前的分支
								return NetworkWalker.KILL_BRANCH;
							}
							
						} else if (current.isTransformer()) {
							// 判断当前设备是否是变压器
							int modelId = current.getModelId();
							if (isDistributionTransformer(modelId)) {
								// 当前设备是一个配电变压器，将其加入列表
								transformers.add(current);
								// 搜索到了配电变压器，需要杀掉当前的分支
								return NetworkWalker.KILL_BRANCH;
							}
						}
						
						// 其他情况，无方向地继续前进
						return NetworkWalker.PROCESS_EACH;
					}
					
				}, 
				NetworkAdaptor.DEPTH_FIRST);
		long t2 = System.currentTimeMillis(); 
		System.out.println("第一次追踪，用时：" + (t2 - t1) + "毫秒。");
		System.out.println("共分析出电源：" + sourceDevices.size() + "个，配电：" + transformers.size() + "个。");

		// 两次追踪完成后，剩下的配电变压器，即为不带电的设备
		List<DeviceInfo> transformerDevices = topoDevicesToDeviceInfo(transformers.iterator(), req.getPsrTypeSys());
		resp.setCode(ReturnCode.SUCCESS);
		resp.setOutageTransformer(transformerDevices.toArray(new DeviceInfo[transformerDevices.size()]));
		return resp;
	}
 	
	public MockOutageRangeAnalyzeResponse mockOutageRangeAnalyze(MockOutageRangeAnalyzeRequest req) {
		
		MockOutageRangeAnalyzeResponse resp = new MockOutageRangeAnalyzeResponse();
		if (null == req || !req.validate()) {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "传入参数有误"));
			return resp;
		}
		
		// 构造起始查询条件
		DeviceKey sourceDeviceKey = null; // 电源类型（出线开关）
		List<DeviceKey> openSwitchKeys = new ArrayList<DeviceKey>(); // 需要断开的开关
		if (req.getPsrTypeSys().equals(PsrTypeSystem.CLASS_ID)) {
			sourceDeviceKey = getDeviceKeyByClassId(req.getSourceType(), req.getSourceId());
			if (null != req.getOpenSwitch()) {
				for (DeviceInfo deviceInfo : req.getOpenSwitch()) {
					openSwitchKeys.add(getDeviceKeyByClassId(deviceInfo.getPsrType(), 
							deviceInfo.getPsrId()));
				}
			}
		} else if (req.getPsrTypeSys().equals(PsrTypeSystem.EQUIPMENT_ID)) {
			sourceDeviceKey = getDeviceKeyByEqumentId(req.getSourceType(), req.getSourceId());
			if (null != req.getOpenSwitch()) {
				for (DeviceInfo deviceInfo : req.getOpenSwitch()) {
					openSwitchKeys.add(getDeviceKeyByEqumentId(deviceInfo.getPsrType(), 
							deviceInfo.getPsrId()));
				}
			}
		} else if (req.getPsrTypeSys().equals(PsrTypeSystem.MODEL_ID)) {
			sourceDeviceKey = getDeviceKeyByModelId(req.getSourceType(), req.getSourceId());
			if (null != req.getOpenSwitch()) {
				for (DeviceInfo deviceInfo : req.getOpenSwitch()) {
					openSwitchKeys.add(getDeviceKeyByModelId(deviceInfo.getPsrType(), 
							deviceInfo.getPsrId()));
				}
			}
		} else {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "传入参数中的psrTypeSys有误"));
			return resp;
		}
		
		if (null == sourceDeviceKey) {
			// 未查找到起点设备
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "未能查找到起点设备"));
			return resp;
		}
		
		// 分析网络中的设备
		if (null == networkAdaptor || networkAdaptor == NetworkAdaptor.NONE) {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "未能查找到拓扑网络"));
			return resp;
		}
		
		// 电源类型
		TopoDevice sourceDevice = networkAdaptor.getDevice(sourceDeviceKey.getModelId(), sourceDeviceKey.getOid());
		if (null == sourceDevice) {
			// 参数错误，抛出异常
			resp.setCode(new ReturnCode(ReturnCode.VALUEWRONG_CODE, "未能在拓扑网络中查找到起点设备"));
			return resp;
		}
		
		// 第一遍追踪，单源追踪，不考虑开关状态，全部追踪
		// 收集追踪过程中出线的所有配电变压器
		final Set<TopoDevice> transformers = new HashSet<TopoDevice>(); 
		
		// 记录第二次搜索需要的电源
		final List<TopoDevice> sourceDevices = new ArrayList<TopoDevice>(); 

		long t1 = System.currentTimeMillis(); 
		networkAdaptor.search(
				sourceDevice, 
				new NetworkWalker() {

					@Override
					public int walk(TopoDevice last, TopoDevice current,
							List<TopoDevice> nexts) {
						
						// 判空操作
						if (null == current) {
							return NetworkWalker.PROCESS_EACH;
						}
						
						if (current.isBus()) {
							// 判断当前设备是否是母线
							Bus bus = current.asBus();
							if (bus.getVoltageLevel() !=
								bus.getStation().getVoltageLevel()) {
								// 母线的电压等级与母线所属电站的电压等级不同，说明母线作为当前轨迹的电源
								// 这里并不将母线作为电源，而是将母线的前一个设备作为电源
								// 因为母线会连接到别的配电网中，第二次追踪时会造成追踪路径的扩散
								sourceDevices.add(last);
								// 母线作为电源，需要杀掉当前的分支
								return NetworkWalker.KILL_BRANCH;
							}
							
						} else if (current.isTransformer()) {
							// 判断当前设备是否是变压器
							int modelId = current.getModelId();
							if (isDistributionTransformer(modelId)) {
								// 当前设备是一个配电变压器，将其加入列表
								transformers.add(current);
								// 搜索到了配电变压器，需要杀掉当前的分支
								return NetworkWalker.KILL_BRANCH;
							}
						}
						
						// 其他情况，无方向地继续前进
						return NetworkWalker.PROCESS_EACH;
					}
					
				}, 
				NetworkAdaptor.DEPTH_FIRST);
		long t2 = System.currentTimeMillis(); 
		System.out.println("第一次追踪，用时：" + (t2 - t1) + "毫秒。");
		System.out.println("共分析出电源：" + sourceDevices.size() + "个，配电：" + transformers.size() + "个。");
		
		// 第二遍追踪，多源追踪，考虑开关状态进行追踪

		// 第二次追踪时断开的开关
		final Set<Switch> openSwitchs = new HashSet<Switch>();
		for (DeviceKey switchKey : openSwitchKeys) {
			TopoDevice _device = networkAdaptor.getDevice(switchKey.getModelId(), switchKey.getOid());
			if (null == _device) {
				continue;
			}
			if (!_device.isSwitch()) {
				continue;
			}
			openSwitchs.add(_device.asSwitch());
		}
		
		t1 = System.currentTimeMillis();
		networkAdaptor.search(
				sourceDevices.toArray(new TopoDevice[sourceDevices.size()]), 
				new NetworkWalker() {

					@Override
					public int walk(TopoDevice last, TopoDevice current,
							List<TopoDevice> nexts) {
						
						// 判空操作
						if (null == current) {
							return NetworkWalker.PROCESS_EACH;
						}
						
						if (current.isBus()) {
							// 判断当前设备是否是母线
							Bus bus = current.asBus();
							if (bus.getVoltageLevel() !=
									bus.getStation().getVoltageLevel()) {
								// 母线的电压等级与母线所属电站的电压等级不同，说明母线作为当前轨迹的电源
								// 母线作为电源，需要杀掉当前的分支
								return NetworkWalker.KILL_BRANCH;
							}
							
						} else if (current.isTransformer()) {
							// 判断当前设备是否是变压器
							int modelId = current.getModelId();
							if (isDistributionTransformer(modelId)) {
								// 当前设备是一个配电变压器，说明其带电
								// 将其从列表中排除
								transformers.remove(current);
								// 搜索到了配电变压器，需要杀掉当前的分支
								return NetworkWalker.KILL_BRANCH;
							}
						} else if (current.isSwitch()) {
							// 判断当前设备是否是开关
							if (openSwitchs.contains(current)) {
								// 当前开关位于断开的列表中，杀掉当前分支
								return NetworkWalker.KILL_BRANCH;
							}
						}
						
						// 其他情况，无方向地继续前进
						return NetworkWalker.PROCESS_EACH;
					}
					
				},
				NetworkAdaptor.DEPTH_FIRST);
		t2 = System.currentTimeMillis(); 
		System.out.println("第二次追踪，用时：" + (t2 - t1) + "毫秒。");
		
		// 两次追踪完成后，剩下的配电变压器，即为不带电的设备
		List<DeviceInfo> transformerDevices = topoDevicesToDeviceInfo(transformers.iterator(), req.getPsrTypeSys());
		resp.setCode(ReturnCode.SUCCESS);
		resp.setOutageTransformer(transformerDevices.toArray(new DeviceInfo[transformerDevices.size()]));
		return resp;
	}
	
	/**
	 * 是否是配电变压器
	 * 
	 * @param modelId
	 * @return
	 */
	private boolean isDistributionTransformer(int modelId) {
		return 30200000 == modelId || 30200001 == modelId ||
				30200002 == modelId || 30200003 == modelId ||
				11000000 == modelId || 11000001 == modelId;
	}
	
	private List<DeviceInfo> topoDevicesToDeviceInfo(Iterator<TopoDevice> topoDevices, String psrTypeSys) {
		
		List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
		
		int psrTypeSysEnum = 0;
		if (psrTypeSys.equals(PsrTypeSystem.CLASS_ID)) {
			psrTypeSysEnum = 1;
		} else if (psrTypeSys.equals(PsrTypeSystem.EQUIPMENT_ID)) {
			psrTypeSysEnum = 2;
		}
		
		// 将topoDevices按表分组
		Map<String, List<String>> topoDeviceMap = new HashMap<String, List<String>>();
		while (topoDevices.hasNext()) {
			TopoDevice topoDevice = topoDevices.next();
			String sModelId = String.valueOf(topoDevice.getModelId());
			List<String> topoDeviceOids = topoDeviceMap.get(sModelId);
			if (null == topoDeviceOids) {
				topoDeviceOids = new ArrayList<String>();
				topoDeviceMap.put(sModelId, topoDeviceOids);
			}
			topoDeviceOids.add(String.valueOf(topoDevice.getOid()));
		}
		
		for (Map.Entry<String, List<String>> entry : topoDeviceMap.entrySet()) {
			String modelId = entry.getKey();
			
			String psrType = modelId;
			if (1 == psrTypeSysEnum) {
				psrType = getClassIdByModelId(modelId);
			} else if (2 == psrTypeSysEnum) {
				psrType = getEqumentIdByModelId(modelId);
			}
			
			DeviceModel model = null;
			try {
				model = modelService.fromSubClass(modelId, false);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			List<String> oids = entry.getValue();
			getDeviceInfosFromOids(model, psrType,
					oids.toArray(new String[oids.size()]),
					deviceInfos);
		}
		return deviceInfos;
	}
	
	
}
