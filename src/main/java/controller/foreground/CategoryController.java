package controller.foreground;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import common.ServerResponse;
import pojo.Category;
import service.CategoryService;
import service.CustomerService;

@RequestMapping("/foreground")
@RestController
public class CategoryController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
    CategoryService categoryService = act.getBean(CategoryService.class);

	//返回所有类别
	@GetMapping("/categorys")
	public ServerResponse findAllCategory() {
		return categoryService.findAllCategory();
	}
	
	//测试restful的put
	@PutMapping("/update/demo")
	public ServerResponse demoUpdate(String name) {
		return ServerResponse.createBySuccessMessage("put成功");
	}
}
