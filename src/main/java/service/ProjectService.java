package service;

import org.springframework.web.multipart.MultipartFile;

import common.ServerResponse;
import pojo.Project;

public interface ProjectService {

	public ServerResponse findAllProject(int currentPage, int pageSize);
	public ServerResponse findProjectById(Integer id);
	public ServerResponse findProjectBySearchContent(String searchContent,int currentPage, int pageSize);
	public ServerResponse findProjectQuantity();

	public ServerResponse insertProject(Project project,MultipartFile img);
	public ServerResponse updateProject(Project project,MultipartFile img);
	public ServerResponse deleteProject(Integer id);

	public ServerResponse checkProjectName(String title);
}
