package cn.itcast.bos.service.auth;

import java.util.List;

import cn.itcast.bos.domain.auth.Role;

//角色service
public interface RoleService {

	/**
	 * 
	 * 说明：列表查询
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月16日 上午11:28:51
	 */
	public List<Role> findRoleList();

	/**
	 * 
	 * 说明：保存角色，并关联功能权限
	 * @param role
	 * @param functionIds
	 * @author 传智.BoBo老师
	 * @time：2017年3月16日 上午11:54:39
	 */
	public void saveRole(Role role, String functionIds);

}
