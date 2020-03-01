package service;

import javax.mail.MessagingException;

import common.ServerResponse;
import pojo.Customer;

public interface CustomerService {

    public ServerResponse findAllCustomer();
    public ServerResponse findCustomerById(Integer id);
    public ServerResponse findAllCustomersQuantity();

    public ServerResponse insertCustomer(Customer customer) throws MessagingException;

    public ServerResponse deleteCustomerById(Integer id);

}
