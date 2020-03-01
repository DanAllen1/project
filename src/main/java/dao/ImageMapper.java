package dao;

import pojo.Image;

public interface ImageMapper {
	
	//通过商品id查找照片
	public Image findImgByProductId(Integer productId);
	
	//增
	public Integer insertImg(Image img);
	
	//更改图片信息
	public Integer updateImgByProductId(Image img);
	
	//通过商品id删除图片
	public Integer deleteImgByProductId(Integer productId);
	
}
