package cn.itcast.bos.web.action.auth;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.service.auth.RoleService;
import cn.itcast.bos.web.action.base.BaseAction;

//角色action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller("roleAction")
@Scope("prototype")
public class RoleAction extends BaseAction<Role>{
	//service
	@Autowired
	private RoleService roleService;
	
	//列表
	@Action("role_list")
	public String list(){
		List<Role> roleList= roleService.findRoleList();
		pushToValuestackRoot(roleList);
		return JSON;
	}
	
	//保存角色
	@Action(value="role_add",
			results={@Result(name=SUCCESS,location="/WEB-INF/pages/admin/role.jsp")})
	public String add(){
		
		roleService.saveRole(model,functionIds);
		
		return SUCCESS;
	}
	
	private String functionIds;
	public void setFunctionIds(String functionIds) {
		this.functionIds = functionIds;
	}
	

}
