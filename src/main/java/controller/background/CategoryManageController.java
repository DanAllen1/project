package controller.background;
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
import pojo.Product;
import service.CategoryService;
import service.ProductService;

@RequestMapping("/background")
@RestController
public class CategoryManageController {
	
	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	CategoryService categoryService = act.getBean(CategoryService.class);

	//增加分类
	@PostMapping("/category")
	public ServerResponse addCategory(Category category) {
		return categoryService.insertCategory(category);
	}

	//实时检测分类名是否重复
	@GetMapping("/category/name/{name}")
	public ServerResponse checkName(@PathVariable String name){
		System.out.println("name:  "+name);
		if (name == null){
			return ServerResponse.createByErrorMessage("分类名不能为空");
		}
		else {
			return categoryService.checkCategoryName(name);
		}
	}

	//实时检测分类名是否重复
	@GetMapping("/category/name/{name}/id/{id}")
	public ServerResponse checkName(@PathVariable String name, @PathVariable Integer id){
		System.out.println("name:  "+name+"id: "+id);
		if (name == null && id == null){
			return ServerResponse.createByErrorMessage("分类名或id不能为空");
		}
		else {
			return categoryService.checkCategoryName(name, id);
		}
	}

	//更新分类信息
	@RequestMapping("/updateCategory")
	public ServerResponse updateCategory(Category category) {
		
		if(category==null) {
			return ServerResponse.createByError();
		}
		return categoryService.updateCategoryById(category);
	}
	
	//删除分类
	@DeleteMapping("/category/{id}")
	public ServerResponse deleteCategory(@PathVariable Integer id) {
		return categoryService.deleteCategoryById(id);
	}
}
