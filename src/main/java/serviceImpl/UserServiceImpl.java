package serviceImpl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.ServerResponse;
import dao.UserMapper;
import pojo.Email;
import pojo.User;
import service.UserService;
import until.EmailUntil;

import javax.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	private EmailUntil emailUntil = new EmailUntil();
	
	//登录检验
	public ServerResponse checkUser(User user) {

		User user2 = userMapper.findUserByName(user.getName());
		//如果账号密码存在
		if(user2 != null && user.getName().equals(user2.getName()) &&
		   user.getPassword().equals(user2.getPassword())) {
			//检测用户权限,如果用户权限符合普通 商家 管理员中任意一个,登录成功
			if (user2.getRole() == Const.UserRole.ROLE_NORMAL ||
					user2.getRole() == Const.UserRole.ROLE_RETAILER ||
					user2.getRole() == Const.UserRole.ROLE_ADMIN) {
				return ServerResponse.createBySuccess(user2);
			}
			//如果权限足够，返回成功
			else {
				return ServerResponse.createByErrorMessage("你的账号还在等待管理员允许创建");
			}
		//账号密码信息不存在
		}else {
			return ServerResponse.createByErrorMessage("账号不存在或密码错误");
		}
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

	//增加一个新用户（该账号需要管理员或者商家同意才能使用）
	public ServerResponse addUser(User user) {
		//设置权限为待管理员同意状态
		user.setRole(Const.UserRole.ROLE_NONE);
		int status = userMapper.insertUser(user);
		if (status>0){
			return ServerResponse.createBySuccess();
		}
		return  ServerResponse.createByError();
	}

	//更新用户权限
	@Override
	public ServerResponse updateUserRoleById(User user) {
		//先把修改前的信息提取出来了，以便发邮件等使用
		User originalUser = userMapper.findUserById(user.getId());
		//更新权限
		int status = userMapper.updateUserRoleById(user);
		//如果更新成功
		if (status == 1){
			//如果更新前权限是0，更新后是1，说明是管理员允许该账号创建，发一封邮件提醒该用户
			if (originalUser.getRole() == Const.UserRole.ROLE_NONE && user.getRole() == Const.UserRole.ROLE_NORMAL) {
				//异步发送邮件
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						Email email = new Email();
						email.setRecipient(originalUser.getEmail());
						email.setSubject("你注册的账号" + originalUser.getName() + "已被管理员允许创建");
						email.setContent("你的账号已被允许创建，现在可以正常使用了");
						try {
							emailUntil.emailPost(email);
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					}
				};
				Thread thread = new Thread(runnable);
				thread.start();
			}
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByErrorMessage("数据库发生错误");
	}

	//删除用户
	@Override
	public ServerResponse deleteUserById(Integer id) {
		userMapper.deleteUserById(id);
		return ServerResponse.createBySuccess();
	}

	//检测用户名是否合法
	@Override
	public ServerResponse checkUsernameIsValid(String name) {
		//如果查询数据库中没有改名字，则该名字可以使用
		if (userMapper.findUserByName(name) == null){
			return  ServerResponse.createBySuccess();
		} else {  //否则返回该用户名已经存在
			return ServerResponse.createByErrorMessage("用户名已存在");
		}
	}

	//通过权限查找用户
	@Override
	public ServerResponse findUsersByRole(Integer role) {
		List<User> userList = userMapper.findUserByRole(role);
		if (userList.size() != 0){
			return ServerResponse.createBySuccess(userList);
		}
		return ServerResponse.createByErrorMessage("没有这个权限的用户");
	}

	//修改密码
	@Override
	public ServerResponse updatePasswordById(User newUser) {
		int status = userMapper.updateUserPasswordById(newUser);
		if (status>0){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByErrorMessage("数据库发生错误");
	}

	//返回全部用户，除了权限为0的（0为已注册但是未被批准创建的账号）
	@Override
	public ServerResponse findAllUsersExcludeRole0(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		List<User> userList = userMapper.findAllUsersExcludeRole(Const.UserRole.ROLE_NONE);
		if (userList.size()>0){
			PageInfo<User> pageInfo= new PageInfo<>(userList);
			return ServerResponse.createBySuccess(pageInfo);
		}
		return ServerResponse.createByErrorMessage("没有用户");
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
