package cn.itcast.bos.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.RoleDAO;
import cn.itcast.bos.dao.user.UserDAO;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.user.UserService;
import cn.itcast.bos.utils.MD5Utils;

//用户操作的实现
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements UserService{
	//注入dao
	@Autowired
	private UserDAO userDAO;
	//注入角色dao
	@Autowired
	private RoleDAO roleDAO;

	@Override
	public void saveUser(User user) {
		//密码加密
		user.setPassword(MD5Utils.md5(user.getPassword()));
		userDAO.save(user);
	}

	@Override
	public List<User> findUserList() {
		return userDAO.findAll();
	}

	@Override
	public User findByUsername(String username) {
		return userDAO.findByUsername(username);
	}

	@Override
	public List<User> findByUsernameLike(String username) {
		return userDAO.findByUsernameLike("%"+username+"%");
	}

	@Override
	public String findPasswordByUsername(String username) {
		return userDAO.findPasswordByUsername(username);
	}
	@Override
	public String findPasswordByUsername2(String username) {
		return userDAO.findPasswordByUsername2(username);
	}

	@Override
	public User login(User user) {
		//注意密码加密
		return userDAO.findByUsernameAndPassword(user.getUsername(), MD5Utils.md5(user.getPassword()));
	}

	@Override
	public void updatePasswordById(User user) {
		// 修改业务
		//hibernate：update方法修改（全属性）和快照修改
		//这里只能使用快照（课后自己写）
		//我们今天只想更新password字段，写语句了。
		userDAO.updatePasswordById(user.getId(), MD5Utils.md5(user.getPassword()));
		
	}

	@Override
	public void saveUser(User user, String[] roleIds) {
		//目标：
		//1.保存用户
		//加密
		user.setPassword(MD5Utils.md5(user.getPassword()));
		userDAO.save(user);
		
		//2.关联角色（多对多：持久态对象关联）
		if(roleIds!=null){
			for (String roleId : roleIds) {
				Role role = roleDAO.findOne(roleId);
				//关联：中间表插入数据
				role.getUsers().add(user);
			}
		}
		
	}

}
