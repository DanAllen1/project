package dao;

import pojo.Customer;

import java.util.List;

public interface CustomerMapper {

    public Customer findCustomerById(Integer id);
    public List<Customer> findAllCustomer();
    public Integer findCustomersQuantity();
    public List<String> findEmailByMark(Integer mark);
    public List<Customer> findCustomersByEmail(String email);
    public Integer findMarkByEmail(String email);

    public Integer insertCustomer(Customer customer);

    public Integer updateMarkByEmail(Customer customer);

    public Integer deleteCustomerById(Integer Id);
    public Integer deleteCustomerByEmail(String email);
}
