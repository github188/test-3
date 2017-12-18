package nari.MainGridService.BaseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nari.parameter.MainGridService.GetFileName.GetFileNameRequest;
import nari.parameter.MainGridService.GetFileName.GetFileNameResponse;
import nari.parameter.bean.SearchFile;

public class GetFileNameHandler {

	public GetFileNameResponse getFileName(GetFileNameRequest request){
		String path = request.getPath();
		String type = request.getType();
		String time = request.getTime();
		File file = new File(path);
		String[] searchFileName = file.list();
		int fileNum = searchFileName.length;
		List<SearchFile> searchFileList = new ArrayList<SearchFile>();
		for(int i = 0;i<fileNum;i++){
			//由_分割字符串
			String[] s = searchFileName[i].split("_");
			if(s[1].contains(time)){
				SearchFile searchFile = new SearchFile();
				//得到单个满足的文件预测类型
				String fileForecastType = s[0];
				//得到当前时间
				String fileTime = time;
				//得到预测时间
				String fileForecastTime = s[2].substring(0,s[2].indexOf("."));
				//得到图片格式
				String fileType = s[2].substring(s[2].indexOf("."));
				searchFile.setFileForecastTime(fileForecastTime);
				searchFile.setFileForecastType(fileForecastType);
				searchFile.setFileName(searchFileName[i]);
				searchFile.setFileTime(fileTime);
				searchFile.setFileType(fileType);
				searchFileList.add(searchFile);
			}else{
				System.out.println("未找到指定时间发布的图片");
			}
		}
		//返回所需值
		SearchFile[] searchFile = new SearchFile[searchFileList.size()];
		for(int i = 0;i<searchFileList.size();i++){
			searchFile[i] = searchFileList.get(i);
		}
		GetFileNameResponse resp = new GetFileNameResponse();
		resp.setSearchfile(searchFile);
		return resp;
	}
}
