package nari.MapService.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import nari.Logger.LoggerManager;
import nari.parameter.MapService.GetMap.GetMapRequest;
import nari.parameter.MapService.GetMap.GetMapResponse;
import nari.parameter.code.ReturnCode;

public class GetMapHandler {
	
	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	public GetMapResponse getMap(GetMapRequest request){
		GetMapResponse resp = new GetMapResponse();

		int level = request.getLevel()+1;
		String picRow = "";
		String picColumn = "";
		String[] ss = {"0000","000","00","0"};
		//所在层级行的数目
		String URL = "D:\\Program Files (x86)\\内网通\\程伟\\SectionFolder\\SectionFolder\\PngLarer\\L"+level;
		File file = new File(URL);
		int rowNum = file.list().length;
		//行的后四位
		String StrRowNum = (Integer.toHexString(rowNum)).toLowerCase();
		StrRowNum = ss[StrRowNum.length()] + StrRowNum;
		//行的字符串
		int row = request.getRow();
		//若超出数目返回空
		if(row>rowNum){
			return null;
		}
		picRow = (Integer.toHexString(row)).toLowerCase();
		picRow = ss[picRow.length()]+picRow+StrRowNum;
		
		//所在层级列的数目
		URL = URL + "\\R" + picRow;
		file = new File(URL);
		int columnNum = file.list().length;
		//列的后四位
		String StrColumnNum = (Integer.toHexString(columnNum)).toLowerCase();
		StrColumnNum = ss[StrColumnNum.length()] + StrColumnNum;
		//列的字符串
		int column = request.getColumn();
		//若超出数目返回空
		if(column>columnNum){
			return null;
		}
		picColumn = (Integer.toHexString(column)).toLowerCase();
		picColumn = ss[picColumn.length()]+picColumn+StrColumnNum;
		
		
//		switch(level){
//		case 1:{
//			int row = request.getRow();
//			if(row<0){
//				break;
//			}
//			int column = request.getColumn();
//			
//			//十进制改成四位16进制
//			String[] ss = {"0000","000","00","0"};
//			picRow = (Integer.toHexString(row)).toLowerCase();
//			picRow = ss[picRow.length()]+picRow+"0003";//(总数为3)
//			
//			picColumn = (Integer.toHexString(column)).toLowerCase();
//			picColumn = ss[picColumn.length()]+picColumn+"0005";
//			break;
//		}
//		}
		
		
		try{
		//从本地读取图片数据
//			long s = System.currentTimeMillis();
//			if(picRow.eq)
			URL = URL + "\\C" + picColumn + ".png";
			InputStream input = new FileInputStream(URL);
			int bigsize = input.available();
			byte[] img=new byte[bigsize];
			input.read(img);
//			long e = System.currentTimeMillis();
//			logger.info("used "+(e-s)+"ms");
			//返回切片图信息
//			resp.setRow(row);
//			resp.setColumn(column);
//			resp.setLevel(level);
			resp.setImg(img);
			
			resp.setCode(ReturnCode.SUCCESS);
			return resp;
		}catch(Exception e){
			e.printStackTrace();
			resp.setCode(ReturnCode.FAILED);
			return resp;
		}
	}
}
