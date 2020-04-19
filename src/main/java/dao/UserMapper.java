package dao;

import java.util.List;

import pojo.User;

public interface UserMapper {

	public User findUserById(Integer id);
	public User findUserByName(String name);
	public User findUserByEmail(String email);
	public List<User> findUserByRole(Integer role);
	public List<User> findAllUsers();
	public List<User> findAllUsersExcludeRole(Integer role);
	public Integer findUserQuantity();

	public Integer insertUser(User user);

	public Integer updateUserRoleById(User user);
	public Integer updateUserPasswordById(User user);

	public Integer deleteUserById(Integer id);



}
