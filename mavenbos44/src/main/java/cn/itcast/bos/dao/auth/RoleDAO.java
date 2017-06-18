package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;

//角色dao
public interface RoleDAO extends JpaRepository<Role, String>{
	
	//根据用户来查询角色列表
	//属性表达式
	public List<Role> findByUsers(User user);//参数应该是set，但如果集合只有一个元素的情况下，可以只传一个对象即可

}
