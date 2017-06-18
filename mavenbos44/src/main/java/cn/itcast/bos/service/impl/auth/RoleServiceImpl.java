package cn.itcast.bos.service.impl.auth;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FunctionDAO;
import cn.itcast.bos.dao.auth.RoleDAO;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.service.auth.RoleService;
import cn.itcast.bos.service.base.BaseService;

//角色业务层实现
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseService implements RoleService{
	
	//dao
	@Autowired
	private RoleDAO roleDAO;
	//功能dao
	@Autowired
	private FunctionDAO functionDAO;

	@Override
	public List<Role> findRoleList() {
		return roleDAO.findAll();
	}

	@Override
	public void saveRole(Role role, String functionIds) {
		// 1。保存角色
		roleDAO.save(role);//持久态
		//2.关联功能权限(多对多)
		//中间表维护：通过持久态的对象之间关系来维护
		if(StringUtils.isNotBlank(functionIds)){
			String[] functionIdArray = functionIds.split(",");
			for (String functionId : functionIdArray) {
				Function function = functionDAO.findOne(functionId);//持久态
				//建立关系，向中间表插入数据了
				role.getFunctions().add(function);
			}
		}
		
	}

}
