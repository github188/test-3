package nari.MapService.handler;




public class GetOtherMapHandler {
//	public GetOtherMapResponse getMap(GetOtherMapRequest request){
//		GetOtherMapResponse resp = new GetOtherMapResponse();
//		//调用 根据大小生成图片生成图片 接口？
//		
//		//调用网上某图
//		String path = request.getPath();
//		URL url = new URL(path);
//		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//		conn.setRequestMethod("GET");
//		conn.setConnectTimeout(5 * 1000); 
//		try{
//			InputStream input = conn.getInputStream(); 
//			int bigsize = input.available();
//			byte[] img = new byte[bigsize];
//			input.read(img);
//			
//			//返回切片图信息
//			
//			resp.setImg(img);
//			resp.setCode(ReturnCode.SUCCESS);
//			return resp;
//		}catch(Exception e){
//			e.printStackTrace();
//			resp.setCode(ReturnCode.FAILED);
//			return resp;
//		}
//		
//		
//	}
}
