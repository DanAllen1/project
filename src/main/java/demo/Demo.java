package demo;


import common.Const;
import dao.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import common.ServerResponse;
import pojo.*;
import redis.clients.jedis.Jedis;
import service.*;
import serviceImpl.ProductServiceImpl;
import until.EmailUntil;
import until.ImgTransformToString;
import until.TimeUntil;
import until.TimerViewTask;

import javax.mail.MessagingException;
import java.util.*;


public class Demo {
	
	private int a = 1;
	private TimeUntil timeUntil = new TimeUntil();
	private TimerViewTask timerViewTask = new TimerViewTask();
	EmailUntil emailUntil = new EmailUntil();
	@Test
	public void demo() throws MessagingException {
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
		UserMapper userMapper = act.getBean(UserMapper.class);
		CustomerService customerService =act.getBean(CustomerService.class);

	}
	@Test
	public void demo2() {

	}

	@Test
	public void demo3(){
		String host = "localhost";
		int port = 6379;
		Jedis jedis = new Jedis(host,port);

		jedis.set("name","钟房桂");
		System.out.println(jedis.get("name"));
		jedis.hset("student","name","cn");
		jedis.hset("student","date","11.26");
		System.out.println(jedis.hgetAll("student"));
		System.out.println(jedis.exists("student"));
	}
}
