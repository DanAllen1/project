package serviceImpl;

import common.Const;
import common.ServerResponse;
import dao.CustomerMapper;
import dao.ProductMapper;
import dao.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Customer;
import pojo.Email;
import pojo.Product;
import pojo.Project;
import service.CustomerService;
import until.EmailUntil;

import java.util.List;

import javax.mail.MessagingException;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProjectMapper projectMapper;
    private EmailUntil emailUntil = new EmailUntil();

    public ServerResponse findAllCustomer() {
        List<Customer> customerList= customerMapper.findAllCustomer();
        return ServerResponse.createBySuccess(customerList);
    }


    public ServerResponse findCustomerById(Integer id) {
        return ServerResponse.createBySuccess(customerMapper.findCustomerById(id));
    }

    //获取客户总数量
    public ServerResponse findAllCustomersQuantity() {
        
        return ServerResponse.createBySuccess(customerMapper.findCustomersQuantity());
    }

   //给订阅的用户发邮件 发布商品时
    public ServerResponse postMailToMarkingCustomer(Product product) throws MessagingException {
        Email email = new Email();
        email.setSubject("News from GNSolar");
        email.setContent("Product title:"+product.getName()+"<br>"+
                         "Product description:"+product.getDescription()+"<br>"+
                         "<img src='http://123.57.242.246:8080"+product.getImg().getMainImg()+"'>"+"<br>"+
                         "you can <a href='http://123.57.242.246:8080/project/product.html?id="+product.getId()+"'>detail</a>"+"<br>"+
                         "<a href='http://123.57.242.246:8080/project/unsubscribe.html'>退订</a>");
        //搜索出已经订阅过的用户
        List<String> emailList= customerMapper.findEmailByMark(Const.CustomerMark.MARK);
        email.setRecipients(emailList);
        //发送邮件
        ServerResponse serverResponse = emailUntil.emailPost(email);
        if (serverResponse.getStatus() == 1){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("fail to post email");
    }

    //给订阅的用户发邮件 发布文章时
    @Override
    public ServerResponse postMailToMarkingCustomer(Project project) throws MessagingException {
        Email email = new Email();
        email.setSubject("News from GNSolar");
        email.setContent("Project title:"+project.getTitle()+"<br>"+
                        "Project description:"+project.getDescription()+"<br>"+
                        "<img src='http://123.57.242.246:8080"+project.getImg()+"'>"+"<br>"+
                        "You can<a href='http://123.57.242.246:8080/project/project.html?id="+project.getId()+"'>detail</a>"+"<br>"+
                        "<a href='http://123.57.242.246:8080/project/unsubscribe.html'>unsubscribe</a>");
        //搜索出已经订阅过的用户
        List<String> emailList= customerMapper.findEmailByMark(Const.CustomerMark.MARK);
        email.setRecipients(emailList);
        //发送邮件
        ServerResponse serverResponse = emailUntil.emailPost(email);
        if (serverResponse.getStatus() == 1){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("fault to send email");
    }

    //存储客户信息
    public ServerResponse insertCustomer(Customer customer) throws MessagingException {
        final Integer originalMark;
        //先查询该邮箱是否已经存在数据库
        if (customerMapper.findCustomersByEmail(customer.getEmail()).size() != 0){
            //获取以前的订阅状态
            originalMark = customerMapper.findMarkByEmail(customer.getEmail());
            //如果存在，把数据库内该邮箱的记录的订阅状态更新一下
            customerMapper.updateMarkByEmail(customer);
        } else {
            originalMark = null;
        }
        //把客户信息存进数据库
    	int status = customerMapper.insertCustomer(customer);
        //如果数据库操作成功
        if(status > 0){
            //给第一次访问网站的顾客发一封邮箱
            if (originalMark == null){
                {
                    Email email = new Email();
                    //设置标题
                    email.setSubject("Welcome to GNSolar the first time");
                    //设置内容
                    email.setContent("Thanks for your view!<br>"+
                            "hope you can focus on GNSolar，there will be lots of fantastic product<br>"+
                            "<a href='http://123.57.242.246:8080/project/index.html>主页</a>");
                    //设置收件人
                    email.setRecipient(customer.getEmail());
                    ServerResponse serverResponse = emailUntil.emailPost(email);
                    if (serverResponse.getStatus() == 0){
                        customerMapper.deleteCustomerByEmail(customer.getEmail());
                        System.out.println("该无效邮箱已经删除");
                        return ServerResponse.createByError();
                    }
                }
            }

            //异步把客户信息通过邮件发送给管理员
            Runnable runnable =new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        //把顾客信息通过邮件发给管理员
                        emailUntil.sendCustomerInfo(customer);
                        //如果用户刚订阅了本网站,给用户发一封邮件
                        Email email = new Email();
                        if (customer.getMark() == Const.CustomerMark.MARK && originalMark != Const.CustomerMark.MARK){
                            //设置标题
                            email.setSubject("subscribe successful");
                            //设置内容
                            email.setContent("you have subscribed GNSolar successful<br>" +
                                    "Thanks for your subscription!<br>"+
                                    "<a href='http://123.57.242.246:8080/project/unsubscribe.html>退订</a>");
                            //设置收件人
                            email.setRecipient(customer.getEmail());
                            //发送并且获取返回值
                            ServerResponse serverResponse = emailUntil.emailPost(email);
                            if (serverResponse.getStatus() == 0){
                                customerMapper.deleteCustomerByEmail(customer.getEmail());
                                System.out.println("该无效邮箱已经删除");
                            }
                        }
                    } catch (MessagingException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //启动上面的线程
            Thread thread=new Thread(runnable);
            thread.start();
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("数据库出现问题");
        }
    }

    //订阅和取订功能
    public ServerResponse updateMarkByEmail(Customer customer) throws MessagingException {
        int status = 0;
        //通过邮箱查找订阅状态
        Integer mark = customerMapper.findMarkByEmail(customer.getEmail());
        //如果用户的请求是订阅的话
        if(customer.getMark() == Const.CustomerMark.MARK){
            //如果mark不为空，说明数据库存在该用户的信息，直接修改订阅状态就可以
            if(mark != null){
                //如果该用户已经订阅过了
                if (mark == Const.CustomerMark.MARK){
                    return ServerResponse.createByErrorMessage("your email account have already subscribed");
                }
                //否则把用户的mark状态改为订阅
                else {
                    status = customerMapper.updateMarkByEmail(customer);
                }
            }
            //如果数据库中没有该用户信息，则增加一个新用户
            else {
                status= customerMapper.insertCustomer(customer);
            }
            if (status > 0){
                //给用户发一封订阅成功的邮件
                Email email = new Email();
                //设置标题
                email.setSubject("subscribed successful");
                //设置内容
                email.setContent("you have subscribed GNSolar successful<br>" +
                                "Thanks for your subscription!");
                //设置收件人
                email.setRecipient(customer.getEmail());
                //发送并且获取返回值
                ServerResponse serverResponse = emailUntil.emailPost(email);
                System.out.println(serverResponse.getStatus());
                //如果发送失败 说明邮箱无效，删除该邮箱的所有记录
                if (serverResponse.getStatus() == 0){
                    customerMapper.deleteCustomerByEmail(customer.getEmail());
                    return ServerResponse.createByErrorMessage("please enter valid email");
                }
                return  ServerResponse.createBySuccessMessage("subscribe successful");
            }
        }
        //如果用户的操作是不是订阅的话
        else {
            //检测用户的请求是否为取订请求
            if (customer.getMark() == Const.CustomerMark.NO_MARK){
                //如果数据库中该用户的mark不是已订阅状态的话，返回错误msg该邮箱没有订阅
                if(mark ==null || mark == Const.CustomerMark.NO_MARK){
                   return ServerResponse.createByErrorMessage("your email account have not subscribed to GNSolar");
                }
                //如果数据库中该用户的mark是已订阅状态，则执行取订操作
                else {
                    status = customerMapper.updateMarkByEmail(customer);
                    System.out.println(status);
                    if (status>0){
                      return   ServerResponse.createBySuccess("subscribe successful");
                    }
                }
            }
        }
        return ServerResponse.createByErrorMessage("unknown error");
    }

    //删除顾客信息
    public ServerResponse deleteCustomerById(Integer id) {
        int status = customerMapper.deleteCustomerById(id);
        if(status != 0){
            return ServerResponse.createBySuccess();
        }
        else{
            return ServerResponse.createByError();
        }
    }

}
