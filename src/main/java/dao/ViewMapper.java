package dao;

import pojo.View;

import java.util.List;

public interface ViewMapper {

    public View findViewById(Integer id);
    public View findViewByDate(String date);
    public List<View> findAllViews();

    public Integer insertViewByDate(View view);

    public Integer updateViewByDate(View view);
}
