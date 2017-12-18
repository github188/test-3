package nari.model.user;

import nari.model.bean.UserDef;

public interface UserService {

	public static final UserService NONE = new UserService(){

		@Override
		public UserDef findUser(String userId) throws Exception {
			return null;
		}

		@Override
		public UserDef findUserByName(String userName) throws Exception {
			return null;
		}
		
	};
	
	public UserDef findUser(String userId) throws Exception;
	
	public UserDef findUserByName(String userName) throws Exception;
}
