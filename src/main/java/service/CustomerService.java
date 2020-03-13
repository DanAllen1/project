package service;

import javax.mail.MessagingException;

import common.ServerResponse;
import pojo.Customer;
import pojo.Product;
import pojo.Project;

public interface CustomerService {

    public ServerResponse findAllCustomer();
    public ServerResponse findCustomerById(Integer id);
    public ServerResponse findAllCustomersQuantity();
    public ServerResponse postMailToMarkingCustomer(Product product) throws MessagingException;
    public ServerResponse postMailToMarkingCustomer(Project project) throws MessagingException;

    public ServerResponse insertCustomer(Customer customer) throws MessagingException;

    public ServerResponse updateMarkByEmail(Customer customer) throws MessagingException;

    public ServerResponse deleteCustomerById(Integer id);

}
