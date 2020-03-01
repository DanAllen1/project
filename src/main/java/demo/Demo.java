package demo;


import dao.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import common.ServerResponse;
import pojo.*;
import service.CategoryService;
import service.ProductService;
import service.ProjectService;
import service.ViewService;
import serviceImpl.ProductServiceImpl;
import until.ImgTransformToString;
import until.TimeUntil;
import until.TimerViewTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;


public class Demo {
	
	private int a = 1;
	private TimeUntil timeUntil = new TimeUntil();
	private TimerViewTask timerViewTask = new TimerViewTask();
	@Test
	public void demo() {
		ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
		ImageMapper imgMapper =  act.getBean(ImageMapper.class);
		ProductMapper productMapper = act.getBean(ProductMapper.class);
		ProductService productService = act.getBean(ProductService.class);
		ProjectMapper projectMapper = act.getBean(ProjectMapper.class);
		ProjectService projectService = act.getBean(ProjectService.class);
		CustomerMapper customerMapper = act.getBean(CustomerMapper.class);
		CategoryMapper categoryMapper= act.getBean(CategoryMapper.class);
		ViewMapper viewMapper = act.getBean(ViewMapper.class);
		ViewService viewService =act.getBean(ViewService.class);
		System.out.println(projectMapper.updateProjectById(new Project()));
		System.out.println(productMapper.findProductQuantity());
	}
	@Test
	public void demo2() {
		Timer timer = new Timer();
		TimerViewTask timerViewTask =new TimerViewTask();
		timer.scheduleAtFixedRate(timerViewTask,new Date(),1000);
		System.out.println("测试");
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		/*timerViewTask.cancel();*/
	}

}
