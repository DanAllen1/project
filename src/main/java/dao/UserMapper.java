package dao;

import java.util.List;

import pojo.User;

public interface UserMapper {

	public User findUserById(Integer id);
	public User findUserByName(String name);
	public List<User> findAllUsers();
	
	
}
