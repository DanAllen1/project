package listener;

import dao.ViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import pojo.View;
import service.ViewService;
import until.TimeUntil;
import until.TimerViewTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import static java.util.Calendar.HOUR_OF_DAY;

@WebListener
public class ViewsListener implements ServletContextListener{

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
    private ViewMapper viewMapper = act.getBean(ViewMapper.class);
    /*private ViewService viewService;*/
    private Timer timer = new Timer();
    private TimeUntil timeUntil = new TimeUntil();
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    private static final long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;


    @Override
    public void contextInitialized(ServletContextEvent sce) {

    	//把数据库的view查询出来并存在servletContext中
    	View view = viewMapper.findViewByDate(timeUntil.dateFormat(new Date()));
    	sce.getServletContext().setAttribute("view", view);
    	//创建一个定时器要做的任务类的对象
        TimerViewTask timerViewTask = new TimerViewTask();
        //给对象的servletContext赋值
        timerViewTask.setServletContext(sce.getServletContext());
        //先运行一次
        timerViewTask.run();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7); //凌晨0点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        //安排指定的任务每天0点运行一次。
        timer.schedule( timerViewTask,date,PERIOD_DAY);
    }
    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
    //程序关闭时把访问量存进数据库
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
    	//程序关闭前更新页面的访问量
        viewMapper.updateViewByDate((View) sce.getServletContext().getAttribute("view"));
    }

}
