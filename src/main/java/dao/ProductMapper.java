package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import pojo.Product;

public interface ProductMapper {
	
	public Product findProductById(Integer id);
	
	public Integer findProductIdByName(String name);
	
	public List<Product> findProductByCategory(String categoryName);
	
	public List<Product> findAllProduct();

	public List<Product> findProductBySearchContent(@Param(value = "searchContent") String searchContent);

	public Integer findProductQuantity();

	public Integer insertProduct(Product product);

	public Integer updateProductById(Product product);
	
	public Integer updateCategoryByCategoryId(Product product);
			
	public Integer deleteProductById(Integer id);

	public Integer deleteProductsByCategoryId(Integer id);

}
