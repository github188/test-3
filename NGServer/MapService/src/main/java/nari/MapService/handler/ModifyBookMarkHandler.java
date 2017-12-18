package nari.MapService.handler;

import nari.parameter.MapService.BookMark.ModifyBookMarkRequest;
import nari.parameter.MapService.BookMark.ModifyBookMarkResponse;

public class ModifyBookMarkHandler {
	public ModifyBookMarkResponse modifyBookMark(ModifyBookMarkRequest request){
		ModifyBookMarkResponse resp = new ModifyBookMarkResponse();
		
//		DbAdaptor dbh = new DbAdaptorHandler();
//		String bookMarkName = request.getBookMarkName();
//		String bookMarkId = request.getBookMarkId();
//		MapView view = request.getView();
//		try{
//			String sql = "update CONF_BOOKMARK set BOOKMARKNAME=?,XMAX=?,XMIN=?,YMAX=?,YMIN=? where OID=?";
//			Object[] params = new Object[6];
//			params[0] = request.getBookMarkName();
//			params[1] = request.getView().getXmax();
//			params[2] = request.getView().getXmin();
//			params[3] = request.getView().getYmax();
//			params[4] = request.getView().getYmin();
//			params[5] = bookMarkId;
//			
//			dbh.update(sql, params);
//		}catch(Exception e){
//			e.printStackTrace();
//			resp.setCode(ReturnCode.FAILED);
//			return resp;
//		}
//		
//		//返回bookMark信息
//		BookMark bookMark = new BookMark();
//		bookMark.setBookMarkId(bookMarkId);
//		bookMark.setBookMarkName(bookMarkName);
//		bookMark.setView(view);
//		resp.setBookMark(bookMark);
//		
//		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}

