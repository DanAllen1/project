package dao;

import java.util.List;

import pojo.Category;

public interface CategoryMapper {
	
	//查
	public Category findCategoryById(Integer id);
	
	public Integer findIdBySort(String sort);
	
	public List<Category> findAllCategory();
	
	//增
	public Integer insertCategory(Category category);
	
	//改
	public Integer updateCategoryById(Category category);
	
	//删
	public Integer deleteCategoryById(Integer id);

}
