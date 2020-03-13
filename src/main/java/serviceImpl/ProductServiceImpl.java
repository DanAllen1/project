package serviceImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.UploadPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import common.RTFresponse;
import common.ServerResponse;
import dao.CategoryMapper;
import dao.ImageMapper;
import dao.ProductMapper;
import pojo.Image;
import pojo.Product;
import service.ProductService;
import until.DeleteFile;
import until.ImgTransformToString;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ImageMapper imgMapper;
	@Autowired
	private CategoryMapper categoryMapper;
		 
	//通过id查找商品
	public ServerResponse findProductByid(Integer id) {		
		
		return ServerResponse.createBySuccess(productMapper.findProductById(id));
	}
	
	//通过分类查找商品
	public ServerResponse findProductByCategory(int currentPage, int pageSize, String categoryName) {
		PageHelper.startPage(currentPage, pageSize);
		List<Product> productList = productMapper.findProductByCategory(categoryName);
		PageInfo<Product> pageInfo = new PageInfo<>(productList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	//返回全部商品
	public ServerResponse findAllProduct(int currentPage, int pageSize){
		PageHelper.startPage(currentPage, pageSize);
		List<Product> productList = productMapper.findAllProduct();
		PageInfo<Product> pageInfo = new PageInfo<>(productList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	//搜索
	public ServerResponse findProductBySearchContent(String searchContent, int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		List<Product> productList = productMapper.findProductBySearchContent(searchContent);
		PageInfo<Product> pageInfo = new PageInfo<>(productList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	//获取全部商品的数量
	public ServerResponse findProductQuantity() {
		return ServerResponse.createBySuccess(productMapper.findProductQuantity());
	}

	//把商品信息存进数据库
	public ServerResponse InsertProduct(Product product,List<MultipartFile> imgs) {			
		
		Image img = new Image();
		String otherImg = "";
		//存取图片的路径
		List<String> imgPathList =new ArrayList<>();
		//通过分类名获取分类id
		product.setCategoryId(categoryMapper.findIdBySort(product.getCategory()));
		//把商品存进商品表
		int status = productMapper.insertProduct(product);
		//如果插入失败直接返回错误
		if(!(status>0))
			return ServerResponse.createByErrorMessage("商品存取失败");
		//通过商品名字查询出刚才存取的商品的id,然后设置图片的商品id
		int productId = productMapper.findProductIdByName(product.getName());
		img.setProductId(productId);
		//从前端传过来的文件提取图片信息
		// 检查上传的东西是否为空
		if (!imgs.isEmpty() && imgs.size() > 0) {	
			// 循环输出上传的所有文件
			for (MultipartFile file : imgs) {
				// 获取上传文件的原始名称
				String originalFilename = file.getOriginalFilename();
				// 设置上传文件的保存的物理地址
				String dirPath = UploadPath.IMG_PATH;
				try {
					// 把上传的文件以originalFilename为名字存入指定文件夹dirPath，若文件夹不存在则不行
					file.transferTo(new File(dirPath + originalFilename));
					//存取图片的项目路径
					imgPathList.add("/img/"+originalFilename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}				
		} else {
			return ServerResponse.createByErrorMessage("图片为空");
		}
		//提取出图片路径并存进pojo类中
		int i=1;
		for(String imgPath:imgPathList) {
			//设置第一张图片为商品主图片
			if(i==1) {
				img.setMainImg(imgPath);
			}
			else {
				otherImg+=imgPath+",";	
			}	
			i++;
		}	
		//去掉最后一个多余的","
		otherImg = otherImg.substring(0, otherImg.length() -1);
		img.setOtherImg(otherImg);
		//最后把图片存进图片表
		int status2 = imgMapper.insertImg(img);
		//若插入成功，服务器返回成功状态码
		if(status2>0) {
			//返回一个最新商品数据给订阅功能使用
			return ServerResponse.createBySuccess(productMapper.findProductById(productId));
		}
		//否则返回失败
		return ServerResponse.createByError();
	}

	//更新商品信息
	public ServerResponse updateProduct(Product product,List<MultipartFile> imgs) {			
		
		Image img = new Image();
		String otherImg = "";
		//存取图片的路径
		List<String> imgPathList =new ArrayList<>();
		//通过id更改商品信息
		int status = productMapper.updateProductById(product);
		//如果更改直接返回错误
		if(!(status>0))
			return ServerResponse.createByError();
		//处理上传的图片
		// 检查上传的东西是否为空
		if (!imgs.isEmpty() && imgs.size() > 0) {	
			// 循环输出上传的所有文件
			for (MultipartFile file : imgs) {
				// 获取上传文件的原始名称
				String originalFilename = file.getOriginalFilename();
				// 设置上传文件的保存的物理地址
				String dirPath = UploadPath.IMG_PATH;
				try {
					// 把上传的文件以originalFilename为名字存入指定文件夹dirPath，若文件夹不存在则不行
					file.transferTo(new File(dirPath + originalFilename));
					//存取图片的项目路径
					imgPathList.add("/img/"+originalFilename);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}				
		} else {
			return ServerResponse.createByErrorMessage("图片为空");
		}
		//提取出图片路径并存进image类中
		int i=1;
		for(String imgPath:imgPathList) {
			//设置第一张图片为商品主图片
			if(i==1) {
				img.setMainImg(imgPath);
			}
			else {
				otherImg+=imgPath+",";	
			}	
			i++;
		}
		//去掉最后一个多余的","
		otherImg = otherImg.substring(0, otherImg.length() -1);
		img.setOtherImg(otherImg);
		//设置要更改的图片记录的商品id
		img.setProductId(product.getId());
		//最后调动修改图片的mapper
		int status2 = imgMapper.updateImgByProductId(img);
		//若修改成功，服务器返回成功状态码
		if(status2>0) {
			return ServerResponse.createBySuccess();
		}
		//否则返回失败
		return ServerResponse.createByErrorMessage("s数据库修改失败");
	}

	//实时检查商品是否重复,用于添加商品时
	public ServerResponse checkProductName(String name) {
		Integer id = productMapper.findProductIdByName(name);
		if (id != null){
			return ServerResponse.createByErrorMessage("用户名已存在");
		}
		else {
			return ServerResponse.createBySuccess();
		}
	}

	//删除商品
	public ServerResponse deleteProduct(Integer id) {

		//删除商品
		int status = productMapper.deleteProductById(id);
		//如果删除商品失败返回错误码
		if(!(status>0))
			return ServerResponse.createByError();
        /*//获取图片的路径
        Image img = imgMapper.findImgByProductId(id);
        //把路径变成应该字符串集合
        ImgTransformToString imgTransformToString = new ImgTransformToString();
        List<String> imgList = imgTransformToString.imgTransform(img);
        //删除存在硬盘上的商品照片
        DeleteFile deleteFile = new DeleteFile();
        //删除失败直接返回错误
        if(!deleteFile.deleteImg(imgList)){
            return ServerResponse.createByError();
        };*/
		//删除数据库里的商品图片
		int status2 = imgMapper.deleteImgByProductId(id);
		//如果删除图片失败返回错误码
		if(!(status2>0))
			return ServerResponse.createByError();
		//成功则返回成功码
		return ServerResponse.createBySuccess();
	}
	
	//图片为空时修改
	public ServerResponse updateProductWhileImgNull(Product product) {
		
		Product originalProduct = productMapper.findProductById(product.getId());
		int status = productMapper.updateProductById(product);
		//如果更改直接返回错误
		if(!(status>0))
			return ServerResponse.createByError();
		return ServerResponse.createBySuccess();
	}
	
	
	
}
