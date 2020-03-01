package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import pojo.Project;

public interface ProjectMapper {
	
	public Project findProjectById(Integer id);
	public List<Project> findProjectBySearchContent(@Param(value="searchContent")String searchContent);
	public Project findProjectByTitle(String title);
	public List<Project> findAllProject();
	public Integer findProjectQuantity();

	public Integer insertProject(Project project);
	
	public Integer updateProjectById(Project project);
	
	public Integer deleteProjectById(Integer id);

}
