package controller.foreground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

import common.ServerResponse;
import service.CategoryService;
import service.ProductService;

@RestController
@RequestMapping("/foreground")
public class ProductController {
	
	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	ProductService productService = act.getBean(ProductService.class);

	//获取全部商品
	@GetMapping("/products")
	public ServerResponse findAllProduct(@RequestParam(value="currentPage",defaultValue = "1") int currentPage, 
			@RequestParam(value="pageSize",defaultValue = "8")int pageSize) {

		return productService.findAllProduct(currentPage,pageSize);
	}
	
	//通过种类获取商品
	@GetMapping("/products/{categoryName}")
	public ServerResponse findProductByCategory(@RequestParam(value="currentPage",defaultValue = "1") int currentPage, 
			@RequestParam(value="pageSize",defaultValue = "8")int pageSize,
			@PathVariable String categoryName) {
		return productService.findProductByCategory(currentPage,pageSize,categoryName);
	}
		
	//通过id查找商品	
	@GetMapping("/product/{id}")
		//整数类型尽量用Integer接受，不然用int接受到null数据的话会报错
	public ServerResponse findProductById(@PathVariable Integer id, HttpSession session) {
		if(id==null)
			return ServerResponse.createByErrorMessage("请传入需要查找的id");
		return productService.findProductByid(id);
	}
	//搜索
	@GetMapping("/product/resource/{searchContent}")
	public ServerResponse findProductBySearchContent(@PathVariable String searchContent,
		 @RequestParam(value = "currentPage" ,defaultValue = "1")int currentPage,
		 @RequestParam(value = "pageSize" ,defaultValue = "15")int pageSize){
		if (searchContent == null){
			return ServerResponse.createByError();
		}
		return  productService.findProductBySearchContent(searchContent,currentPage,pageSize);
	}
}
