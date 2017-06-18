package cn.itcast.bos.web.action.bc;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.bc.StaffService;
import cn.itcast.bos.web.action.base.BaseAction;

//取派员操作action
@Controller("staffAction")
@Scope("prototype")
@ParentPackage("basic-bos")
@Namespace("/")
public class StaffAction extends BaseAction<Staff>{
	//注入service
	@Autowired
	private StaffService staffService;
	
	/*@Autowired//子类注入的时候，struts接管了，struts注入（按名字注入struts.objectFactory.spring.autoWire = name）
	//参数的名字作为bean的名字
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}*/

	//添加取派员业务
	@Action(value="staff_add"
			,results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/staff.jsp")})
	//方法级别的权限管理
	//授权
	@RequiresRoles("weihu")
	public String add(){
		//调用业务层保存
		staffService.saveStaff(model);
		
		//演示一下代码级别的shiro的权限控制
		//入口用户对象
		Subject subject = SecurityUtils.getSubject();
		//认证
		if(subject.isAuthenticated()){
			System.out.println("必须认证才能执行。。。");
		}
		
		//授权
		//==布尔值判断
		//角色权限
		if(subject.hasRole("weihu")){
			System.out.println("必须有某角色才能执行。。。");
		}
		
		//功能权限
		if(subject.isPermitted("staff")){
			System.out.println("必须有某功能权限才能执行。。。");
		}
		//==异常
		//角色权限
		try {
			subject.checkRole("weihu");
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		//功能权限
		try {
			subject.checkPermission("staff");
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
		
		
		//返回到jsp即可（jsp：datagrid：主动加载数据列表）
		return SUCCESS;
	}
	
	//分页列表查询
	@Action("staff_listPage")
	public String listPage(){
		//第一步：首先要获取到datagrid传过来两个参数
		//第二步：调用层查询
		//思考：持久层方案（不管什么方案，我们都要手动查询两次：总记录数和当前页的记录列表）
		//1)sql方案，拼接sql，where 1=1
		//2)creteria方案
		//今天：springdatajpa的分页方案，只需要调用一次api（底层仍然查询两次）
		//请求的分页bean对象：参数1：当前页码的索引（从0开始），参数2：每页最大记录数
		Pageable pageable=new PageRequest(page-1, rows);
		//参数：请求的分页bean的对象：用来封装请求数据
		//返回：响应的分页bean对象（存放的结果）
		Page<Staff> pageResponse = staffService.findAll(pageable);
		
		//重新组装数据，给页面想要的
		Map<String, Object> resultMap = new HashMap<>();
		//总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		//当前也的数据列表
		resultMap.put("rows", pageResponse.getContent());
		
		//压入root栈顶：交给json插件
		pushToValuestackRoot(resultMap);
		//跳转到json类型的结果集上，
		return JSON;
	}
	
	//属性驱动封装
	private int page;//页码
	private int rows;//每页最大记录数
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	//作废取派员
	@Action("staff_deleteBatch")
	public String deleteBatch(){
		//先获取选中的行的所有ids
		String ids = getFromParameter("ids");
		//结果map
		Map<String, Object> resultMap =new HashMap<>();
		
		try {
			//调用业务层
			staffService.deleteStaffBatch(ids);
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
		}
		//结果压入root栈顶
		pushToValuestackRoot(resultMap);
		
		return JSON;
	}
	
	//查询没有作废的取派员
	@Action("staff_listNoDel")
	public String listNoDel(){
		//调用业务层查询
		List<Staff> staffList= staffService.findStaffListNoDel();
		//压入栈顶
		pushToValuestackRoot(staffList);
		
		return JSON;
	}
	
}
