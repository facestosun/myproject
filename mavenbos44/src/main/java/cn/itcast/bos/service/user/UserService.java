package cn.itcast.bos.service.user;

import java.util.List;

import cn.itcast.bos.domain.user.User;

//用户操作的业务层接口
public interface UserService {
	/**
	 * 
	 * 说明：保存用户
	 * @param user
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 上午11:24:51
	 */
	public void saveUser(User user);
	/**
	 * 
	 * 说明：查询所有的用户列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 上午11:25:24
	 */
	public List<User> findUserList();
	
	/**
	 * 
	 * 说明：根据用户名查询用户
	 * @param username
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 上午11:48:02
	 */
	public User findByUsername(String username);
	/**
	 * 
	 * 说明：根据用户名模糊匹配用户列表
	 * @param username
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 上午11:48:09
	 */
	public List<User> findByUsernameLike(String username);
	
	/**
	 * 
	 * 说明：根据用户名查询密码
	 * @param username
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 上午11:53:57
	 */
	public String findPasswordByUsername(String username);
	
	/**
	 * 
	 * 说明：根据用户名称查询密码-query注解
	 * @param username
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 上午11:57:46
	 */
	public String findPasswordByUsername2(String username);
	
	
	/**
	 * 
	 * 说明：登录
	 * @param user
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 下午3:15:49
	 */
	public User login(User user);
	/**
	 * 
	 * 说明：修改密码
	 * @param user
	 * @author 传智.BoBo老师
	 * @time：2017年3月4日 下午5:19:37
	 */
	public void updatePasswordById(User user);
	
	/**
	 * 
	 * 说明：保存用户并关联角色
	 * @param user
	 * @param roleIds
	 * @author 传智.BoBo老师
	 * @time：2017年3月16日 下午3:11:24
	 */
	public void saveUser(User user, String[] roleIds);

}
