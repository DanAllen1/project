package service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;

import common.ServerResponse;
import pojo.Product;

public interface ProductService {

	public ServerResponse findProductByid(Integer id);
	
	public ServerResponse findProductByCategory(int currentPage, int pageSize, String categoryName);
	
	public ServerResponse findAllProduct(int currentPage, int pageSize);

	public ServerResponse findProductBySearchContent(String searchContent,int currentPage, int pageSize);

	public ServerResponse findProductQuantity();

	public ServerResponse InsertProduct(Product product,List<MultipartFile> imgs);
	
	public ServerResponse updateProduct(Product product,List<MultipartFile> imgs);

	public  ServerResponse checkProductName(String name);
	
	public ServerResponse updateProductWhileImgNull(Product product);
	
	public ServerResponse deleteProduct(Integer id);
}
