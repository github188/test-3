package nari.QueryService.handler;

import nari.parameter.QueryService.VirtualSpatialLocate.VirtualLocateRequest;
import nari.parameter.QueryService.VirtualSpatialLocate.VirtualLocateResponse;
import nari.parameter.code.ReturnCode;

public class VirtualLocateHandler {
	public VirtualLocateResponse locateVirtual(VirtualLocateRequest request){
		VirtualLocateResponse resp = new VirtualLocateResponse();
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
