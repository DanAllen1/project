package serviceImpl;

import common.ServerResponse;
import dao.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Customer;
import service.CustomerService;
import until.EmailUntil;

import java.util.List;

import javax.mail.MessagingException;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

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

    //存储客户信息
    public ServerResponse insertCustomer(Customer customer) throws MessagingException {
        //把客户信息存进数据库
    	int status = customerMapper.insertCustomer(customer);
        if(status!=0){
        	//把客户信息通过邮件发送给管理员
        	EmailUntil emailUntil = new EmailUntil();
        	emailUntil.sendCustomerInfo(customer);
            return ServerResponse.createBySuccess();
        }
        else {
            return ServerResponse.createByError();
        }

    }


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
