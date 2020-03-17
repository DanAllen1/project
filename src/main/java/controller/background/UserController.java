package controller.background;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import common.ServerResponse;
import pojo.User;
import service.UserService;
import until.EmailUntil;

import java.io.IOException;

@RestController
@RequestMapping("/background")
public class UserController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	UserService userService = act.getBean(UserService.class);
	EmailUntil emailUntil = new EmailUntil();

	//获取全部用户（role>0才算是正常用户）
	@GetMapping("/user")
	public ServerResponse findAllUsersExcludeRole0(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize",defaultValue = "8") int pageSize, HttpSession session){
		User currentUser = (User) session.getAttribute("user");
		//管理员才有权限获取所以用户的信息
		if (currentUser.getRole() == Const.UserRole.ROLE_ADMIN){
			return userService.findAllUsersExcludeRole0(pageNum,pageSize);
		}
		//否则提示你没有权限这么做
		return ServerResponse.createByErrorMessage("你没有权限这么做");
	}
	//登录验证
	@PostMapping("/user/session")
	public ServerResponse checkUser(@RequestBody User user,HttpSession session) {
		if(user == null) {
			return ServerResponse.createByErrorMessage("密码不能为空");
		}
		ServerResponse serverResponse = userService.checkUser(user);
		//如果service层校验正确
		if (serverResponse.getStatus() == 1) {
            //把用户信息存进session
            session.setAttribute("user", serverResponse.getData());
        }
        //如果service层校验错误
        return serverResponse;
	}
	
	//获取验证码 找回密码
	@PostMapping("/user/retrieveCheckNum")
	public ServerResponse getRetrieveCheckNumByEmail(@RequestBody User user,HttpSession session) throws MessagingException {
		String email = user.getEmail();
		//先通过邮箱和用户名检查该账号是否已经存在
		 if(userService.checkUserIsExist(user.getName(), user.getEmail()).getStatus() == 1) {
			//生成一个1-1000随机数,并存进session中
			int checkNum = (int)(Math.random()*1000);
			session.setAttribute("checkNum", checkNum);
			session.setAttribute("name",user.getName());
			//把验证码发给要找回密码的邮箱
			emailUntil.emailPost(email, checkNum);
			return ServerResponse.createBySuccessMessage("已发送到qq邮箱，请注意查收");
		}
		else {
			return ServerResponse.createByErrorMessage("该账号不存在");
		}
	}

    //获取验证码 注册账号
    @PostMapping("/user/registerCheckNum")
    public ServerResponse getRegisterCheckNum(@RequestBody User user,HttpSession session) throws MessagingException {
        String email = user.getEmail();
        if(user.getEmail() != null) {
            //生成一个1-1000随机数,并存进session中
            int checkNum = (int) (Math.random() * 1000);
            session.setAttribute("checkNum", checkNum);
            session.setAttribute("email", email);
            //把验证码发给要找回密码的邮箱
            try {
                emailUntil.emailPost(email, checkNum);
                return ServerResponse.createBySuccessMessage("已发送到qq邮箱，请注意查收");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ServerResponse.createByErrorMessage("请不要输入无效地址");
        } else {
            return ServerResponse.createByErrorMessage("邮件不能为空");
        }
    }

	//验证验证码是否正确，若正确则找回账号密码
	@GetMapping("/user/name/{name}checkNum/{checkNum}")
	public ServerResponse checkNum(@PathVariable Integer checkNum, @PathVariable String name, HttpSession session) {
		System.out.println(name);
		if (checkNum ==null ||name ==null){
		    return ServerResponse.createByErrorMessage("有未填写的内容");
        }
		//如果验证码正确，且和邮箱匹配,则执行找回密码操作
	    if((int)checkNum == (int)session.getAttribute("checkNum") && name.equals(session.getAttribute("name"))) {
			return userService.retrieveUserByUsername(name);
		}
		else {
			return ServerResponse.createByErrorMessage("验证码错误");
		}
	}

	//注册账号
	@PostMapping("/user")
	public ServerResponse signUp(@RequestBody User user, Integer checkNum, HttpSession session){
	    //检验user或checkNum是否为空
        if (user == null || checkNum ==null){
            return ServerResponse.createByErrorMessage("有未填写内容");
        }
	    //检验验证码是否正确
	    if (user.getEmail().equals(session.getAttribute("email")) &&
                checkNum.equals(session.getAttribute("checkNum"))){
	        //若正确则调用service增加用户
	        return userService.addUser(user);
        }
        return ServerResponse.createByError();
	}

	//检验用户名是否已存在
	@GetMapping("/user/name/{name}")
	public ServerResponse checkUsernameIsValid(@PathVariable String name){
		if (name != null){
			//如果用户名存在
			if(userService.checkUsernameIsExist(name).getStatus() == 1){
				return ServerResponse.createByErrorMessage("用户名已存在");
			} else { //如果用户名不存在
				return ServerResponse.createBySuccess();
			}
		} else {
			return ServerResponse.createByErrorMessage("用户名不能为空");
		}
	}

	//通过权限获取用户
	@GetMapping("/user/role/{role}")
	public ServerResponse findUsersByRole(@PathVariable Integer role){
		if (role == null){
			return ServerResponse.createByError();
		}
		//如果该权限不存在,返回错误
		if (role != Const.UserRole.ROLE_NORMAL && role != Const.UserRole.ROLE_RETAILER &&
				role != Const.UserRole.ROLE_ADMIN && role != Const.UserRole.ROLE_NONE){
			ServerResponse.createByErrorMessage("没有这种权限");
		}
		return userService.findUsersByRole(role);
	}

	//修改用户的权限
	@PutMapping("/user/role")
	public ServerResponse updateUserRole(@RequestBody User user,HttpSession session){
		if (user.getRole()==null || user.getId() == null){
			return ServerResponse.createByErrorMessage("要修改的用户id和权限不能为空");
		}
		//获取选择正在登录的用户
		User currentUser = (User) session.getAttribute("user");
		//如果现在登录的账号是管理员或retailer才有权限修改别人的权限
		if (currentUser.getRole() == Const.UserRole.ROLE_ADMIN || currentUser.getRole() ==Const.UserRole.ROLE_RETAILER){
			//如果要把用户权限修改为0，提示无法操作
			if (user.getRole() == Const.UserRole.ROLE_NONE){
				return ServerResponse.createByErrorMessage("无法把用户权限修改为0");
			}
			//如果要把用户权限修改为管理员，提示无法操作
			else if (user.getRole() == Const.UserRole.ROLE_ADMIN){
				return ServerResponse.createByErrorMessage("无法把用户权限修改为管理员权限");
			}
			else {
				//如果现在登陆的用户权限>要修改的权限,允许操作
				if (currentUser.getRole()>user.getRole()){
					return userService.updateUserRoleById(user);
				}
				else {
					return ServerResponse.createByErrorMessage("你没有权限这么做");
				}
			}
		}
		else {
			return ServerResponse.createByErrorMessage("你没有权限这么做");
		}
	}

	//拒绝用户的账号创建请求
	@DeleteMapping("/user/createApplication/{id}")
	public ServerResponse refuseUserCreated(@PathVariable Integer id, HttpSession session){
		User currentUser = (User) session.getAttribute("user");
		//检查现在登录的用户是否有权限这么做
		if (currentUser.getRole() == Const.UserRole.ROLE_RETAILER || currentUser.getRole() == Const.UserRole.ROLE_ADMIN){
			return userService.deleteUserById(id);
		} else {
			return ServerResponse.createByErrorMessage("你没有权限这么做");
		}
	}

	//修改密码
	@PutMapping("/user")
	public ServerResponse updatePassword(String originalPassword, String newPassword,
										 HttpSession session, HttpServletResponse response) throws IOException {
		if (originalPassword == null || newPassword == null){
			return ServerResponse.createByErrorMessage("不能为空");
		}
		User currentUser = (User) session.getAttribute("user");
		//如果旧密码正确，则允许修改密码
		if (originalPassword.equals(currentUser.getPassword())){
			User newUser = currentUser;
			newUser.setPassword(newPassword);
			ServerResponse serverResponse = userService.updatePasswordById(newUser);
			//如果修改成功
			if (serverResponse.getStatus() == 1){
				//消除session中的user
				session.removeAttribute("user");
				return serverResponse;
			}
			else {
				return serverResponse;
			}
		} else {
			return ServerResponse.createByErrorMessage("请输入正确的旧密码");
		}
	}

	//删除账号
	@DeleteMapping("/user/{id}")
	public ServerResponse deleteUserById(@PathVariable Integer id, HttpSession session){
		if (id == null){
			return ServerResponse.createByErrorMessage("要删除的账号id不能为空");
		}
		User currentUser = (User) session.getAttribute("user");
		//只有管理员有权限删除用户账号
		if (currentUser.getRole() == Const.UserRole.ROLE_ADMIN){
			return userService.deleteUserById(id);
		}
		else {
			return ServerResponse.createByErrorMessage("你没有这个权限");
		}
	}

	//获取当前session登录的账号信息
	@GetMapping("/user/session")
	public ServerResponse getSessionUser(HttpSession session){
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null){
			ServerResponse.createByErrorMessage("当前没有登录的账号");
		}
		return ServerResponse.createBySuccess(currentUser);
	}

	//登出当前账号
	@DeleteMapping("/user/session")
	public ServerResponse logout(HttpSession session){
		System.out.println("okok");
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null){
			ServerResponse.createByErrorMessage("当前没有登录的账号");
		}
		session.invalidate();
		return ServerResponse.createBySuccess();
	}
}
