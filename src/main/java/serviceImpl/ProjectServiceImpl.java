package serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.Const;
import common.UploadPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import common.ServerResponse;
import dao.ProjectMapper;
import pojo.Project;
import service.ProjectService;
import until.DeleteFile;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	
	//返回所有文章
	public ServerResponse findAllProject(int currentPage, int pageSize) {
		PageHelper.startPage(currentPage,pageSize);
		List<Project> projectList = projectMapper.findAllProject();
		PageInfo<Project> pageInfo = new PageInfo<>(projectList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	//通过id查找文章
	public ServerResponse findProjectById(Integer id) {
		Project project = projectMapper.findProjectById(id);
		return ServerResponse.createBySuccess(project);
	}
	//搜索文章
	public ServerResponse findProjectBySearchContent(String searchContent,int currentPage, int pageSize) {
		PageHelper.startPage(currentPage,pageSize);
		List<Project> projectSearchResult = projectMapper.findProjectBySearchContent(searchContent);
		PageInfo<Project> pageInfo = new PageInfo<>(projectSearchResult);
		return ServerResponse.createBySuccess(pageInfo);
	}

	//获取全部文章的数量
	public ServerResponse findProjectQuantity() {
		return ServerResponse.createBySuccess(projectMapper.findProjectQuantity());
	}

	//按日期排序
	@Override
	public ServerResponse findProjectBySort(int sortType,int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		if (sortType == Const.ProjectSort.DATE_DESC){
			List<Project> projectList = projectMapper.findAllProjectOrderByDateDESC();
			PageInfo<Project> projectPageInfo = new PageInfo<>(projectList);
			return  ServerResponse.createBySuccess(projectPageInfo);
		}
		if (sortType == Const.ProjectSort.DATE_ASC){
			List<Project> projectList = projectMapper.findAllProjectOrderByDateASC();
			PageInfo<Project> projectPageInfo = new PageInfo<>(projectList);
			return  ServerResponse.createBySuccess(projectPageInfo);
		}
		return ServerResponse.createByErrorMessage("没有这种排序方式");
	}

	//添加文章
	public ServerResponse insertProject(Project project, MultipartFile img) {
		if(!img.isEmpty()&&img.getSize()>0) {
			String originalFilename = img.getOriginalFilename();
			String dirPath = UploadPath.IMG_PATH;
			try {
				img.transferTo(new File(dirPath+originalFilename));
				//把文章存进数据库
				project.setImg("/img/"+originalFilename);
				projectMapper.insertProject(project);
				//返回一个最新文章给订阅功能使用
				return ServerResponse.createBySuccess(projectMapper.findProjectByTitle(project.getTitle()));
			} catch (Exception e) {
				return ServerResponse.createByError();
			}
		}
		return ServerResponse.createByErrorMessage("照片为空");
	}

	//修改文章
	public ServerResponse updateProject(Project project, MultipartFile img) {
		//如果图片不为空
		if(!img.isEmpty()&&img.getSize()>0) {
			String originalFilename = img.getOriginalFilename();
			System.out.println(originalFilename);
			String dirPath = UploadPath.IMG_PATH;
			try {
				img.transferTo(new File(dirPath+originalFilename));
				//把文章存进数据库
				project.setImg("/img/"+originalFilename);
				int status = projectMapper.updateProjectById(project);
				if(status>0) {
					return ServerResponse.createBySuccess();
				}
				else {
					return ServerResponse.createByError();
				}
			} catch (Exception e) {
				return ServerResponse.createByError();
			}
		}
		//如果图片为空，则使用原来的图片
		String originalImg = projectMapper.findProjectById(project.getId()).getImg();
		project.setImg(originalImg);
		int status = projectMapper.updateProjectById(project);
		if(status>0) {
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}

	//删除文章
	public ServerResponse deleteProject(Integer id) {
		int status = projectMapper.deleteProjectById(id);
		DeleteFile delete = new DeleteFile();
		if(status>0) {
			//删除存在本地的图片
			/*if(delete.deleteImgByName(projectMapper.findProjectById(id).getImg())){
				return ServerResponse.createBySuccess();
			}
			else{
				return  ServerResponse.createByError();
			}*/
			return ServerResponse.createBySuccess();
		}
		else {
			return ServerResponse.createByError();
		}
	}

	//实时检测文章名是否重复
	public ServerResponse checkProjectName(String title) {
		Project project = projectMapper.findProjectByTitle(title);
		if (project == null){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}
}
