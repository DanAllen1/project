package serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.ServerResponse;
import dao.CategoryMapper;
import dao.ProductMapper;
import pojo.Category;
import pojo.Product;
import service.CategoryService;
import service.ProductService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private ProductMapper productMapper;

	//查找分类
	public ServerResponse findCategoryById(Integer id) {
		return ServerResponse.createBySuccess(categoryMapper.findCategoryById(id));
	}

	//返回全部分类
	public ServerResponse findAllCategory() {	
		return ServerResponse.createBySuccess(categoryMapper.findAllCategory());
	}

	//增加分类
	public ServerResponse insertCategory(Category category) {
		if(category!=null) {
			categoryMapper.insertCategory(category);
			return ServerResponse.createBySuccess();
		}		
		return ServerResponse.createByError();
	}

	//更新分类信息
	public ServerResponse updateCategoryById(Category category) {
		
		if(category!=null) {
			//先更新商品表里面的分类名
			Product product = new Product();
			product.setCategory(category.getSort());
			product.setCategoryId(category.getId());
			int j = productMapper.updateCategoryByCategoryId(product);
			//更新分类表的分类名
			int i = categoryMapper.updateCategoryById(category);
			return ServerResponse.createBySuccess();
		}		
		return ServerResponse.createByError();
	}

	//删除分类
	public ServerResponse deleteCategoryById(Integer id) {
		//删除该分类的商品
		productMapper.deleteProductsByCategoryId(id);
		//删除分类
		categoryMapper.deleteCategoryById(id);
		return ServerResponse.createBySuccess();
	}

	//检查分类名是否重复,用于添加
	public ServerResponse checkCategoryName(String name) {
		Integer status = categoryMapper.findIdBySort(name);
		if ((status == null)){
			return ServerResponse.createBySuccess();
		}
		else {
			return ServerResponse.createByError();
		}
	}

	//检查分类名是否重复,用于更新
	public ServerResponse checkCategoryName(String name, Integer id) {
		String originalName = categoryMapper.findCategoryById(id).getSort();
		if (name.equals(originalName)){
			return ServerResponse.createBySuccess();
		}
		Integer status = categoryMapper.findIdBySort(name);
		if ((status == null)){
			return ServerResponse.createBySuccess();
		}
		else {
			return ServerResponse.createByError();
		}
	}
}
