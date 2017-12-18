package nari.BaseService.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nari.BaseService.bean.GetFileServiceRequest;
import nari.BaseService.bean.GetFileServiceResponse;
import nari.parameter.code.ReturnCode;

public class GetFileServiceHandler {

	public GetFileServiceResponse GetFile(GetFileServiceRequest req){
		GetFileServiceResponse resp = new GetFileServiceResponse();
		String pathName = req.getPathName();
		pathName = pathName.replace("\\", "\\"+"\\");
		File file = new File(pathName);
		List<Byte> byteList = new ArrayList<Byte>();
		InputStream in = null;
		boolean wrongFlag = false;
		try {
			in = new FileInputStream(file);
			byte[] readbyte = new byte[1];
			while(in.read(readbyte) != -1){
				byteList.add(readbyte[0]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			resp.setCode(ReturnCode.FAILED);
			wrongFlag = true;
		}catch (IOException e) {
			e.printStackTrace();
			resp.setCode(ReturnCode.FAILED);
			wrongFlag = true;
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(wrongFlag){
				return resp;
			}
		}
		
		int count = byteList.size();
		byte[] bytes = new byte[count];
		for(int i= 0;i<count;i++){
			bytes[i] = byteList.get(i);
		}
		resp.setBytes(bytes);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
