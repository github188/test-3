package nari.QueryService.handler;

import nari.parameter.QueryService.VirtualConditionQuery.VirtualConditionQueryRequest;
import nari.parameter.QueryService.VirtualConditionQuery.VirtualConditionQueryResponse;
import nari.parameter.code.ReturnCode;

public class VirtualConditionQueryHandler {

	public VirtualConditionQueryResponse queryVirtual(VirtualConditionQueryRequest request){
		VirtualConditionQueryResponse resp = new VirtualConditionQueryResponse();
//		String virtualType = request.getVirtualType();
//		ModelService ms = QueryServiceActivator.modelService;
//		DeviceModel model = null;
//		try {
//			 model = ms.fromClass(virtualType, false);
//		} catch (ModelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		CriteriaBuilder builder = model.getEntryManager().getCriteriaBuilder();
//		SubPSRDef[] subTypes = request.getSubTypes();
//		int subLength = subTypes.length;
//		String[] subPSRType = new String[subLength];
//		String[] subPSRName = new String[subLength];
//		//虚拟设备下的每个设备的查询
//		for(int i=0;i<subLength;i++){
//			subPSRType[i] = subTypes[i].getSubPSRType();
//			subPSRName[i] = subTypes[i].getSubPSRName();
//			Expression exp1 = builder.equal(builder.getRoot().get("subPSRType", String.class),subPSRType[i]);
//			Expression exp2 = builder.equal(builder.getRoot().get("subPSRName", String.class),subPSRName[i]);
//			Expression e = builder.and(exp1, exp2);
//			ResultSet set = model.search(returnType, e);
//			Iterator<Device> it = set.resultList();
//			int count = 0;
//			//得到虚拟设备下的每个设备查询结果
//			List<VirtualDevice> VirtualDevList = new ArrayList<VirtualDevice>();
//			while(it.hasNext()){
//				VirtualDevList.add(count, it.next().asVirtualDevice());
//				if(VirtualDevList.get(count) == null){
//					break;
//				}
//				count = count+1;
//			}
//			//虚拟设备下的每个设备查询每个结果
//			String[] Oid = new String[count];
//			for(int j=0;j<count;j++){
//				Oid[j] = VirtualDevList.get(j).getOid();
//			}
//			
//			
//		}
		
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
