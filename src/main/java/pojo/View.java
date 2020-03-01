package pojo;

public class View {
    private Integer id;
    private String  date;
    private Integer pageView;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPageView() {
        return pageView;
    }

    public void setPageView(Integer pageView) {
        this.pageView = pageView;
    }

    @Override
    public String toString() {
        return "view{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", pageView=" + pageView +
                '}';
    }
}
