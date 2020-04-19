package controller.foreground;

import common.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import common.ServerResponse;
import pojo.Project;
import service.ProductService;
import service.ProjectService;

@RestController
@RequestMapping("/foreground")
public class ProjectController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	ProjectService projectService = act.getBean(ProjectService.class);
	
	//返回全部文章
	@GetMapping("/projects")
	public ServerResponse findAllProject(@RequestParam(value="currentPage",defaultValue = "1") int currentPage, 
			@RequestParam(value="pageSize",defaultValue = "8")int pageSize) {
		return projectService.findAllProject(currentPage, pageSize);
	}
    //根据排序方式返回文章
    @GetMapping("/projects/sort/{keyword}")
    public ServerResponse findAllProjectBySort(@RequestParam(value="currentPage",defaultValue = "1") int currentPage,
                                               @RequestParam(value="pageSize",defaultValue = "8")int pageSize,
                                               @PathVariable Integer keyword){
	    if (keyword == Const.ProjectSort.DEFAULT){
            return projectService.findAllProject(currentPage, pageSize);
        }
        if (keyword == Const.ProjectSort.DATE_DESC || keyword == Const.ProjectSort.DATE_ASC){
            return projectService.findProjectBySort(keyword, currentPage, pageSize);
        }
        return ServerResponse.createByErrorMessage("没有这种排序方式");
	}
	//通过id查找文章
	@GetMapping("/project/{id}")
	public ServerResponse findProjectById(@PathVariable Integer id) {
		return projectService.findProjectById(id);
	}
	//通过输入内容搜索文章
	@GetMapping("/project/searchContent/{searchContent}")
	public  ServerResponse searchProject(@PathVariable String searchContent, @RequestParam(value="currentPage",defaultValue = "1") int currentPage,
										 @RequestParam(value="pageSize",defaultValue = "8")int pageSize){
		if (searchContent == null)
			return ServerResponse.createByError();
		return projectService.findProjectBySearchContent(searchContent,currentPage,pageSize);
	}
}
