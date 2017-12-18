package nari.MapService.handler;

import nari.parameter.MapService.BookMark.AddBookMarkRequest;
import nari.parameter.MapService.BookMark.AddBookMarkResponse;

public class AddBookMarkHandler {
	public AddBookMarkResponse addBookMark(AddBookMarkRequest request){
		AddBookMarkResponse resp = new AddBookMarkResponse();
//		DbAdaptor dbh = new DbAdaptorHandler();
//		
//		String bookMarkName = request.getBookMarkName();
//		MapView view = request.getView();
//		//主键
//		int OId = 0;
//		try {
//			OId = dbh.getSequence("CONF_BOOKMARK");
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		Object[] params = new Object[8];
//		params[0] = OId;
//		params[1] = request.getBookMarkName();
//		params[2] = request.getUserName();
//		params[3] = request.getIdentify();
//		params[4] = request.getDocumentId();
//		params[5] = request.getView().getXmax();
//		params[6] = request.getView().getXmin();
//		params[7] = request.getView().getYmax();
//		params[8] = request.getView().getYmin();
//		
//		try{
//			String sql = "insert into CONF_BOOKMARK values(?,?,?,?,?,?,?,?,?,null,null)";
//			dbh.save(sql, params);
//		}catch(Exception e){
//			e.printStackTrace();
//			resp.setCode(ReturnCode.FAILED);
//			return resp;
//		}
//		
//		//返回bookMark信息
//		BookMark addBookMark = new BookMark();
//		addBookMark.setBookMarkId(String.valueOf(OId));	
//		addBookMark.setView(view);
//		addBookMark.setBookMarkName(bookMarkName);
//		resp.setBookMark(addBookMark);
//		
//		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
