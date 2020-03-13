package controller.background;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.UploadPath;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import common.RTFresponse;
import common.ServerResponse;
import pojo.Product;
import service.ProductService;
import until.Check;

@RestController
@RequestMapping("/background")
public class ProductManageController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	ProductService productService = act.getBean(ProductService.class);
	private Check check = new Check();
	
	//添加商品
	@PostMapping("/product")
	public ServerResponse addProduct(Product product, HttpServletResponse response, HttpSession session,
			@RequestParam("imgs")List<MultipartFile> imgs) throws IOException {
		ServerResponse serverResponse;
		if (imgs.isEmpty() && imgs.size() <= 0){
			response.sendRedirect("/project/fault.html");
			return ServerResponse.createByErrorMessage("imgs为空");
		}
		//判断service层处理是否成功
		serverResponse = productService.InsertProduct(product, imgs);
		if (serverResponse.getStatus() == 1){
			//如果成功的话先把商品存进session，供发邮件使用
			session.setAttribute("latestProduct",serverResponse.getData());
			//成功则重定向到成功页面
			response.sendRedirect("/project/successful.html?operate=addProduct");
		}
		else {
			//否则重定向到失败页面
			response.sendRedirect("/project/fault.html");
		}
		return serverResponse;
	}

	//实时检查商品的名字是否重复
	@GetMapping("/product/name/{name}")
	public  ServerResponse checkProductName(@PathVariable String name){
		if (name==null){
			return ServerResponse.createByErrorMessage("用户名为空");
		}
		return productService.checkProductName(name);
	}

	//获取全部商品的数量
	@GetMapping("/products/quantity")
	public ServerResponse findProductQuantity(){
		return productService.findProductQuantity();
	}

	//更改商品(restful风格失败)
	@RequestMapping("/updateProduct")
	public ServerResponse updateProductById(Product product, HttpServletResponse response,
			@RequestParam("imgs")List<MultipartFile> imgs) throws IOException {
		ServerResponse serverResponse;
		if(imgs.isEmpty() || imgs.size() == 1){
			serverResponse = productService.updateProductWhileImgNull(product);
		}
		else{
			serverResponse = productService.updateProduct(product, imgs);
		}
		if (serverResponse.getStatus() == 1){
			response.sendRedirect("/project/successful.html?operate=updateProduct");
		}
		else {
			response.sendRedirect("/project/fault.html");
		}
		return serverResponse;
	}
	
	//删除
	@DeleteMapping("/product/{id}")
	public ServerResponse deleteProductById(@PathVariable Integer id) {
		return productService.deleteProduct(id);
	}
	
	//保存富文本编辑器的图片
	@RequestMapping("/saveRTFimg")
	public RTFresponse addProduct(
			@RequestParam("RTFimg") List<MultipartFile> RTFimg, HttpServletRequest request) {
		
		String originalFilename="";	
		List<String> list= new ArrayList<>();
		// 检查上传的东西是否为空
		if (!RTFimg.isEmpty() && RTFimg.size() > 0) {
			// 循环输出上传的所有文件
			for (MultipartFile file : RTFimg) {
				// 获取上传文件的原始名称
				originalFilename = file.getOriginalFilename();
				// 设置上传文件的保存的地址
				String dirPath = UploadPath.IMGRTF_PATH;
				System.out.println(dirPath);
				try {				
					// 把上传的文件以originalFilename为名字存入指定文件夹dirPath，若文件夹不存在则不行
					file.transferTo(new File(dirPath + originalFilename));
					//把路径加入要返回的列表
					list.add("/imgRTF/" + originalFilename);
				} catch (Exception e) {
					e.printStackTrace();
					return new RTFresponse(1,null);
				}
			}			
			// 跳转到成功页面
			System.out.println(list);
			return new RTFresponse(0,list);
		} else {
			return new RTFresponse(1,null);
		}
	}
}
