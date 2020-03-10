package serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.ServerResponse;
import dao.UserMapper;
import pojo.User;
import service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	//登录检验
	public ServerResponse checkUser(User user) {

		User user2 = userMapper.findUserByName(user.getName());
		if(user.getName().equals(user2.getName()) && 
		   user.getPassword().equals(user2.getPassword())) {
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}
	
	//检测邮箱是否合法
	public ServerResponse checkEmail(User user) {
		List<User> userList = userMapper.findAllUsers();
		for(User user1:userList) {
			if(user.getEmail().equals(user1.getEmail())) {
				return ServerResponse.createBySuccess();
			}
		}
		return ServerResponse.createByError();
	}

	//找回用户账号密码
	public  ServerResponse retrieveUserByEmail(String email){
		User user = userMapper.findUserByEmail(email);
		if (user!=null){
			return ServerResponse.createBySuccess(user);
		}
		else
			return ServerResponse.createByErrorMessage("");
	}

}
