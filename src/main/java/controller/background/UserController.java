package controller.background;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import common.ServerResponse;
import common.UserDemo;
import dao.UserMapper;
import pojo.User;
import service.ProjectService;
import service.UserService;
import until.EmailUntil;

@RestController
@RequestMapping("/background")
public class UserController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	UserService userService = act.getBean(UserService.class);
	EmailUntil emailUntil = new EmailUntil();
	
	//登录验证
	@PostMapping("/user")
	public ServerResponse checkUser(@RequestBody User user,HttpSession session) {
		if(user == null) {
			return ServerResponse.createByErrorMessage("密码不能为空");
		}
		ServerResponse serverResponse = userService.checkUser(user);
		if(serverResponse.getStatus() == 1) {
			session.setAttribute("user", user);
		}
		return serverResponse;
	}
	
	//获取验证码
	@PostMapping("/user/checkNum")
	public ServerResponse getCheckNumByEmail(@RequestBody User user,HttpSession session) throws MessagingException {
		String email = user.getEmail();
		 
		 if(userService.checkEmail(user).getStatus()==1) {
			//生成一个1-1000随机数,并存进session中
			int checkNum = (int)(Math.random()*1000);
			session.setAttribute("checkNum", checkNum);
			//把验证码发给要找回密码的邮箱
			emailUntil.emailPost(email, checkNum);
			return ServerResponse.createBySuccessMessage("已发送到qq邮箱，请注意查收");
		}
		else {
			return ServerResponse.createByErrorMessage("该邮箱没有注册账号");
		}
	}
	
	//验证验证码是否正确
	@GetMapping("/user/checkNum/{checkNum}")
	public ServerResponse checkNum(@PathVariable Integer checkNum, HttpSession session) {
		if((int)checkNum == (int)session.getAttribute("checkNum")) {
			User user = new User();
			user.setName(UserDemo.name);
			user.setPassword(UserDemo.password);
			return ServerResponse.createBySuccess(user);
		}
		else {
			return ServerResponse.createByErrorMessage("验证码错误");
		}
	}
}
