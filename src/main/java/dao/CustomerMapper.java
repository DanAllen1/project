package dao;

import pojo.Customer;

import java.util.List;

public interface CustomerMapper {

    public Customer findCustomerById(Integer id);
    public List<Customer> findAllCustomer();
    public Integer findCustomersQuantity();

    public Integer insertCustomer(Customer customer);

    public Integer deleteCustomerById(Integer Id);
}
