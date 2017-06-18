package cn.itcast.bos.web.action.auth;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.auth.FunctionService;
import cn.itcast.bos.web.action.base.BaseAction;

//功能权限操作
@ParentPackage("basic-bos")
@Namespace("/")
@Controller("functionAction")
@Scope("prototype")
public class FunctionAction extends BaseAction<Function>{

	//注入service
	@Autowired
	private FunctionService functionService;
	//列表查询
	@Action("function_list")
	public String list(){
		List<Function> functionList=functionService.findFunctionList();
		//root栈顶
		pushToValuestackRoot(functionList);
		return JSON;
	}
	
	//添加功能权限
	@Action(value="function_add",
			results={@Result(name=SUCCESS,location="/WEB-INF/pages/admin/function.jsp")})
	public String add(){
		//SERVICE
		functionService.saveFunction(model);
		
		return SUCCESS;
	}
	
	
	//动态菜单显示
	@Action("function_showmenu")
	public String showmenu(){
		//什么是动态菜单？generatemenu=1 ，根据 用户 来查询
		//获取当前登录用户
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		
		//调用业务层
		List<Function> menuList=functionService.findMenu(user);
		//压入root栈顶
		pushToValuestackRoot(menuList);
		return JSON;
	}
}
