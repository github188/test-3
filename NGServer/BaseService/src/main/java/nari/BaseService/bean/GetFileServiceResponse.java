package nari.BaseService.bean;

import nari.parameter.code.ReturnCode;

public class GetFileServiceResponse {

	private byte[] bytes = null;
	
	private ReturnCode code = null;
	
	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
