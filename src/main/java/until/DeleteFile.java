package until;

import pojo.Image;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

//通过传入的照片的路径删除图片
public class DeleteFile {

	String dirPath = "C:/Tomcat/apache-tomcat-9.0.20/webapps";
	  //多张图片的删除

	public boolean deleteImg(List<String> imgList) {
		//获取该目录下的所有文件名
		for (String img : imgList){
			System.out.println(img);
			//创建File对象，一个File类对象对应系统的一个磁盘文件或文件夹，所以创建File类对象要给出文件夹名
			Boolean a=new File(dirPath+img).delete();
			//如果删除失败,返回错误
			if (a){
				return false;
			}
		}
		return  true;
	  }
	  //只删除一张图片
	public boolean deleteImgByName(String img){
		//获取该目录下的所有文件名

		//创建File对象，一个File类对象对应系统的一个磁盘文件或文件夹，所以创建File类对象要给出文件夹名
		Boolean a=new File(dirPath+img).delete();
		//如果删除失败,返回错误
		if (a){
			return false;
		}
		return  true;
	}
}
