package controller.background;

import common.Const;
import common.ServerResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import pojo.Customer;
import service.CustomerService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/background")
public class CustomerManageController {

    ApplicationContext act=new ClassPathXmlApplicationContext("applicationContext.xml");
    CustomerService customerService = act.getBean(CustomerService.class);

    //通过id获取单个顾客留言
    @GetMapping("/customer/{id}")
    public ServerResponse findCustomerById(@PathVariable Integer id){
        return customerService.findCustomerById(id);
    }
    //获取所有顾客的留言
    @GetMapping("/customer")
    public ServerResponse findAllCustomer(){
        return  customerService.findAllCustomer();
    }
    //删除顾客的留言
    @DeleteMapping("/customer/{id}")
    public ServerResponse deleteCustomerById(@PathVariable Integer id){
        return customerService.deleteCustomerById(id);
    }
    //给订阅的顾客发邮件
    @PostMapping("/customer/email")
    public ServerResponse postMailTOMarkingCustomer() throws MessagingException {
        return customerService.postMailToMarkingCustomer();
    }
    //获取顾客数量
    @GetMapping("/customers/quantity")
    public  ServerResponse findAllCustomerQuantity(){
        return customerService.findAllCustomersQuantity();
    }
}
