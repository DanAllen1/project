package service;

import common.ServerResponse;
import pojo.User;

public interface UserService {

	public ServerResponse checkUser(User user);

	public ServerResponse retrieveUserByEmail(String email);

	public ServerResponse checkEmail(User user);
	
}
