package pojo;

public class Image {
	
	private Integer id;
	private Integer productId;
	private String mainImg;
	private String otherImg;
	
	public Image() {
		super();
	}
	public Image(Integer productId, String mainImg, String otherImg) {
		super();
		this.productId = productId;
		this.mainImg = mainImg;
		this.otherImg = otherImg;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
	public String getOtherImg() {
		return otherImg;
	}
	public void setOtherImg(String otherImg) {
		this.otherImg = otherImg;
	}
	@Override
	public String toString() {
		return "Image [id=" + id + ", productId=" + productId + ", mainImg=" + mainImg + ", otherImg=" + otherImg + "]";
	}
	
	

}
