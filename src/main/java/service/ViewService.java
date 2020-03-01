package service;

import common.ServerResponse;
import pojo.View;

public interface ViewService {

    public void newDayView();
    
    /*public ServerResponse findTodayView();*/
    public ServerResponse findIncreasedView();
    public Integer findAllViews();
    
    public ServerResponse updateViewByDate(View view);
}
