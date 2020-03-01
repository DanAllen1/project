package pojo;

public class Category {
	
	private Integer id;
	private String sort;
	
	public Category() {
		super();
	}
	public Category(String sort) {
		super();
		this.sort = sort;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "Category [id=" + id + ", sort=" + sort + "]";
	}
}
