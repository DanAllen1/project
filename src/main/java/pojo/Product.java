package pojo;

public class Product {
	
	private Integer id;
	private String name;
	private String date;
	private String leadtime;
	private String specification;
	private String warranty;
	private String price;
	private String MOQ;
	private String category;
	private Integer categoryId;
	private String description;
	private String detail;
	private String seoTitle;
	private String seoKey;
	private String seoDescription;	
	private Image img;
	
	
	public Product() {
		super();
	}
	public Product(Integer id, String name, String date, String leadtime, String specification, String warranty,
			String price, String mOQ, String category, Integer categoryId, String description, String detail,
			String seoTitle, String seoKey, String seoDescription, Image img) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.leadtime = leadtime;
		this.specification = specification;
		this.warranty = warranty;
		this.price = price;
		MOQ = mOQ;
		this.category = category;
		this.categoryId = categoryId;
		this.description = description;
		this.detail = detail;
		this.seoTitle = seoTitle;
		this.seoKey = seoKey;
		this.seoDescription = seoDescription;
		this.img = img;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLeadtime() {
		return leadtime;
	}
	public void setLeadtime(String leadtime) {
		this.leadtime = leadtime;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMOQ() {
		return MOQ;
	}
	public void setMOQ(String mOQ) {
		MOQ = mOQ;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getSeoTitle() {
		return seoTitle;
	}
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}
	public String getSeoKey() {
		return seoKey;
	}
	public void setSeoKey(String seoKey) {
		this.seoKey = seoKey;
	}
	public String getSeoDescription() {
		return seoDescription;
	}
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}
	public Image getImg() {
		return img;
	}
	public void setImg(Image img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", date=" + date + ", leadtime=" + leadtime + ", specification="
				+ specification + ", warranty=" + warranty + ", price=" + price + ", MOQ=" + MOQ + ", category="
				+ category + ", categoryId=" + categoryId + ", description=" + description + ", detail=" + detail
				+ ", seoTitle=" + seoTitle + ", seoKey=" + seoKey + ", seoDescription=" + seoDescription + ", img="
				+ img + "]\r\n";
	}
	
	
	
		

}
