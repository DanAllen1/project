package controller.foreground;

import common.ServerResponse;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Insert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Customer;
import service.CustomerService;

import java.io.IOException;

@RestController
@RequestMapping("/foreground")
public class CustomerController {

    ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
    CustomerService customerService = act.getBean(CustomerService.class);

    //存储顾客信息，并把顾客信息发到qq邮箱
    @PostMapping("/customer")
    public ServerResponse addCustomer(Customer customer, HttpServletResponse response) throws MessagingException, IOException {
        ServerResponse serverResponse;
        serverResponse = customerService.insertCustomer(customer);
        if (serverResponse.getStatus() == 1){
            //成功则重定向到成功页面
            response.sendRedirect("/project/successful.html?operate=postMail");
        }
        else {
            //失败则重定向到失败页面
            response.sendRedirect("/project/fault.html");
        }
        return serverResponse;
    }
}
