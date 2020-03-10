package service;

import javax.mail.MessagingException;

import common.ServerResponse;
import pojo.Customer;

public interface CustomerService {

    public ServerResponse findAllCustomer();
    public ServerResponse findCustomerById(Integer id);
    public ServerResponse findAllCustomersQuantity();
    public ServerResponse postMailToMarkingCustomer() throws MessagingException;

    public ServerResponse insertCustomer(Customer customer) throws MessagingException;

    public ServerResponse updateMarkByEmail(Customer customer) throws MessagingException;

    public ServerResponse deleteCustomerById(Integer id);

}
