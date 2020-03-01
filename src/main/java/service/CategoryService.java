package service;

import common.ServerResponse;
import net.sf.jsqlparser.schema.Server;
import pojo.Category;

public interface CategoryService {
	
	public ServerResponse findCategoryById(Integer id);
	
	public ServerResponse findAllCategory();
	
	public ServerResponse insertCategory(Category category);
	
	public ServerResponse updateCategoryById(Category category);
	
	public ServerResponse deleteCategoryById(Integer id);

	public ServerResponse checkCategoryName(String name);
	public ServerResponse checkCategoryName(String name, Integer id);
}
