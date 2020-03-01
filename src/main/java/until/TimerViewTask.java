package until;

import dao.ImageMapper;
import dao.ViewMapper;
import pojo.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import service.ViewService;
import serviceImpl.ViewServiceImpl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;


public class TimerViewTask extends TimerTask {
    //private Timer timer;
	private TimeUntil timeUntil = new TimeUntil();
	private ServletContext servletContext;
    ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");

    public static void main(String[] args)
    {
        //TimerUntil TimerUntil= new TimerUntil();
        //TimerUntil.timer = new Timer();
        //立刻开始执行TimerUntil任务，只执行一次
        //TimerUntil.timer.schedule(TimerUntil,new Date());
        //立刻开始执行TimerUntil任务，执行完本次任务后，隔2秒再执行一次
        //TimerUntil.timer.schedule(TimerUntil,new Date(),2000);
        //一秒钟后开始执行TimerUntil任务，只执行一次  
        //TimerUntil.timer.schedule(TimerUntil,1000);            
        //一秒钟后开始执行TimerUntil任务，执行完本次任务后，隔2秒再执行一次  
        //TimerUntil.timer.schedule(TimerUntil,1000,2000);            
        //立刻开始执行TimerUntil任务，每隔2秒执行一次  
        //TimerUntil.timer.scheduleAtFixedRate(TimerUntil,new Date(),2000);
        //一秒钟后开始执行TimerUntil任务，每隔2秒执行一次  
        //TimerUntil.timer.scheduleAtFixedRate(TimerUntil,1000,2000);  

        /*try
        {
            Thread.sleep(10000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }*/
        //结束任务执行，程序终止
        //TimerUntil.timer.cancel();
        //结束任务执行，程序并不终止,因为线程是JVM级别的  
        //TimerUntil.cancel();
    }

    //要执行的任务
    @Override
    public void run() {
    	
    	ViewService viewService = act.getBean(ViewService.class);
    	//如果view不为空先把昨日的信息更新进数据库
    	View view = (View) servletContext.getAttribute("view");
    	if(view != null) {
    		viewService.updateViewByDate(view); 
    	} 
        //创建新一天的记录
        viewService.newDayView();
        //把新一天的view存进servletContext
        ViewMapper viewMapper = act.getBean(ViewMapper.class);
        View view2 = viewMapper.findViewByDate(timeUntil.dateFormat(new Date()));
        servletContext.setAttribute("view", view2);
    }

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
    
}
