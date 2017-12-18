package nari.BaseService.handler;

import nari.BaseService.BaseServiceActivator;
import nari.model.bean.UserDef;
import nari.parameter.BaseService.GetConnection.GetConnectionRequest;
import nari.parameter.BaseService.GetConnection.GetConnectionResponse;
import nari.parameter.bean.LoginUser;
import nari.parameter.bean.MapView;
import nari.parameter.code.ReturnCode;

public class GetConnectionHandler {

	public GetConnectionResponse getConnection(GetConnectionRequest request){
		GetConnectionResponse resp = new GetConnectionResponse();
		
		UserDef user = UserDef.NONE;
		try {
			user = BaseServiceActivator.userService.findUserByName(request.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user == UserDef.NONE){
			resp.setCode(ReturnCode.getCode(ReturnCode.NOUSER_CODE, "用户名不存在"));
			return resp;
		}
		resp.setToken("123");
		resp.setCode(ReturnCode.SUCCESS);
		
		LoginUser loginUser = new LoginUser();
		loginUser.setDept(null);
		loginUser.setLoginName("zhangwenli");
		loginUser.setSex("1");
		loginUser.setUserId("111111111111");
		loginUser.setUserName("zwl");
		resp.setUser(loginUser);
		
		MapView view = new MapView();
		view.setXmax(123.111111);
		view.setXmin(122.111111);
		view.setYmax(33.111111);
		view.setYmin(30.111111);
		resp.setView(view);
		return resp;
	}
}
