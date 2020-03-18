package controller.background;

import common.Const;
import common.ServerResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import pojo.Customer;
import pojo.Product;
import pojo.Project;
import service.CustomerService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

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
    public ServerResponse postMailTOMarkingCustomer(HttpSession session) throws MessagingException {
       Project project = (Project) session.getAttribute("latestProject");
        //如果发表的是商品
        if (project != null){
            //移去session中的这个属性
            session.removeAttribute("latestProject");
            return customerService.postMailToMarkingCustomer(project);
        }
        //如果发布的是文章
        Product product = (Product) session.getAttribute("latestProduct");
        if (product != null){
            //移去session中的这个属性
            session.removeAttribute("latestProduct");
            return customerService.postMailToMarkingCustomer(product);
        }
        return ServerResponse.createByErrorMessage("there is no new product or project published");

    }
    //获取顾客数量
    @GetMapping("/customers/quantity")
    public  ServerResponse findAllCustomerQuantity(){
        return customerService.findAllCustomersQuantity();
    }
}
