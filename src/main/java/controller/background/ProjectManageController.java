package controller.background;

import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import common.ServerResponse;
import pojo.Project;
import pojo.User;
import service.ProjectService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/background")
public class ProjectManageController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	ProjectService projectService = act.getBean(ProjectService.class);
	
	//添加文章
	@PostMapping("/project")
	public ServerResponse addProject(Project project, HttpServletResponse response,
			@RequestParam("imgs")MultipartFile imgs, HttpSession session) throws IOException {
		ServerResponse serverResponse;
		//获取登录中的用户名
		User user = (User)session.getAttribute("user");
		//设置作者名
		project.setWriter(user.getName());
		serverResponse = projectService.insertProject(project, imgs);
		if (serverResponse.getStatus() == 1){
			//文章发表成功的话把文章存进session，以供订阅功能使用
			session.setAttribute("latestProject",serverResponse.getData());
			//成功则重定向到成功页面
			response.sendRedirect("/project/successful.html?operate=addProject");
		}
		else {
			//失败则重定向到失败页面
			response.sendRedirect("/project/fault.html");
		}
		return serverResponse;
	}

	//实时检测文章名是重复
	@GetMapping("/project/title/{title}")
	public  ServerResponse checkName(@PathVariable String title){
		if (title == null){
			return ServerResponse.createByError();
		}
		return projectService.checkProjectName(title);
	}
	//返回全部商品的数量
	@GetMapping("/projects/quantity")
	public  ServerResponse findProductQuantity(){
		return projectService.findProjectQuantity();
	}

	//修改文章
	@RequestMapping("/updateProject")
	public ServerResponse updateProject(Project project, HttpServletResponse response,
			@RequestParam("imgs")MultipartFile imgs, HttpSession session) throws IOException {
		//获取作者名字
		User user = (User) session.getAttribute("user");
		project.setWriter(user.getName());
		ServerResponse serverResponse;
		serverResponse = projectService.updateProject(project, imgs);
		if (serverResponse.getStatus() == 1){
			//成功则重定向到成功页面
			response.sendRedirect("/project/successful.html?operate=updateProject");
		}
		else {
			//失败则重定向到失败页面
			response.sendRedirect("/project/fault.html");
		}
		return serverResponse;
	}
	
	//删除文章
	@DeleteMapping("/project/{id}")
	public ServerResponse deleteProjectById(@PathVariable Integer id) {
		return projectService.deleteProject(id);
	}
}