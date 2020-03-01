package controller.background;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import common.ServerResponse;
import pojo.View;
import service.UserService;
import service.ViewService;
import until.EmailUntil;


@RequestMapping("/background")
@RestController
public class ViewController {

	ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
	ViewService viewService =act.getBean(ViewService.class);

	//返回所有天数的浏览量
	@GetMapping("/views")
	public ServerResponse findAllPageView(HttpServletRequest request) {

        Integer pageViewExcludeToday = viewService.findAllViews();
	    View view = (View) request.getServletContext().getAttribute("view");
	    Integer pageViewToday = view.getPageView();
	    Integer pageViewTotal = pageViewExcludeToday + pageViewToday;
		return ServerResponse.createBySuccess(pageViewTotal);
	}

	//今天的浏览量
    @GetMapping("/views/today")
    public ServerResponse findTodayPageView(HttpServletRequest request){
        View view = (View) request.getServletContext().getAttribute("view");
	    return  ServerResponse.createBySuccess(view.getPageView());
    }

	//浏览量增长
	@GetMapping("/views/increased")
    public ServerResponse findYesterdayPageView(){
        return  viewService.findIncreasedView();
    }

	
}
