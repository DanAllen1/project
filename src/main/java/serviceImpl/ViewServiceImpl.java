package serviceImpl;

import dao.ViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.ServerResponse;
import pojo.View;
import service.ViewService;
import until.TimeUntil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ViewServiceImpl implements ViewService {

    @Autowired
    private ViewMapper viewMapper;
    private TimeUntil timeUntil = new TimeUntil();

    //创建新的一天的浏览量记录
    public void newDayView() {
        String date = timeUntil.dateFormat(new Date());
        //先判断是不是新的一天
        if (viewMapper.findViewByDate(date) == null){
            View view = new View();
            view.setDate(date);
            //创建一个新的浏览量记录
            viewMapper.insertViewByDate(view);
        }
        //不是新的一天则创建一个新的记录
        else {
            System.out.println("判断不是新的一天");
            return;
        }
    }

    /*//返回今天的点击量
	public ServerResponse findTodayView() {
		View view = viewMapper.findViewByDate(timeUntil.dateFormat(new Date()));
		return ServerResponse.createBySuccess(view.getPageView());
	}*/

    //返回点击量增长
    public ServerResponse findIncreasedView() {
        Calendar calendar =Calendar.getInstance();
        //今天的天数-1就是昨天
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-1);
        String yesterday = timeUntil.dateFormat(calendar.getTime());
        String today = timeUntil.dateFormat(new Date());
        Integer pageViewIncrease = viewMapper.findViewByDate(today).getPageView() - viewMapper.findViewByDate(yesterday).getPageView();
        return ServerResponse.createBySuccess(pageViewIncrease);
    }

    //返回全部天数的点击量
	public Integer findAllViews() {
		List<View> viewList = viewMapper.findAllViews();
		Integer viewTotal = 0;
		for (View view : viewList){
		    if (!view.getDate().equals(timeUntil.dateFormat(new Date())))
		    viewTotal+=view.getPageView();
        }
		return viewTotal;
	}

	//更新今天的点击量
	public ServerResponse updateViewByDate(View view) {
		viewMapper.updateViewByDate(view);
		return ServerResponse.createBySuccess();
	}

	
	
}
