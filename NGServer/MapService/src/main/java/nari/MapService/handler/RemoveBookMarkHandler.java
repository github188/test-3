package nari.MapService.handler;

import nari.parameter.MapService.BookMark.RemoveBookMarkRequest;
import nari.parameter.MapService.BookMark.RemoveBookMarkResponse;

public class RemoveBookMarkHandler {
	public RemoveBookMarkResponse removeBookMark(RemoveBookMarkRequest request){
		RemoveBookMarkResponse resp = new RemoveBookMarkResponse();
		
		//调用sql语句所在的接口？实现类
//		DbAdaptor dbh = new DbAdaptorHandler();
//		String[] bookMarkId = request.getBookMarkId();
//		int length = bookMarkId.length;
//		Object[] params = new Object[length];
//		StringBuffer a=new StringBuffer("delete from CONF_BOOKMARK where id in (?");
//		for(int i=0;i<length;i++){
//			params[i] = Integer.getInteger(bookMarkId[i]);
//			if(i==0){
//				a.append("?");
//			}
//			if(i!=0){
//				a.append(",?");
//			}
//		}
//		a.append(")");
//		try{	
//			String sql = a.toString();
//			dbh.delete(sql, params);
//		}catch(Exception e){
//			//操作失败，回滚
//			try{
//				dbh.getConnection().rollback();
//			}catch(Exception e1){
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//			resp.setCode(ReturnCode.FAILED);
//			return resp;
//		}
//		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
