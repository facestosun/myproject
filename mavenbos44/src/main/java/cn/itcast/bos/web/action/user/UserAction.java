package cn.itcast.bos.web.action.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.user.UserService;
import cn.itcast.bos.utils.MD5Utils;
import cn.itcast.bos.web.action.base.BaseAction;

//用户操作的action
@Controller("userAction")
@Scope("prototype")//多例
//@ParentPackage("struts-default")
@ParentPackage("basic-bos")//注意：所有的action必须包继承自定义的包，才拥有自定义的一些东东，登录拦截器、全局结果集等等。。。
@Namespace("/")
public class UserAction extends BaseAction<User>{
	//注入Service
	@Autowired
	private UserService userService;

	//直接编写业务代码
	//登录业务
	@Action(value="user_login",
			results={@Result(name=LOGIN,location="/login.jsp"),
			@Result(name=SUCCESS,type="redirect",location="/index.jsp")})
	@InputConfig(resultName=LOGIN)//更改表单校验错误后自动跳转的input结果集视图的名字
	public String login(){
		//=================验证码校验
		//表单的输入的验证码
//		ServletActionContext.getRequest().getParameter("checkcode");
		String checkcode = getFromParameter("checkcode");
		//session中的验证码
		String checkcodeSession=(String) getFromSession("key");
		
		//判断：
		/*if(StringUtils.isBlank(checkcode)||!checkcode.equalsIgnoreCase(checkcodeSession)){
			//提示用户
			addFieldError("checkcode", getText("UserAction.checkcodeerror"));
			//跳转到登录页面
			return LOGIN;
		}*/
		
		
		//传统方式的登录==================调用业务层查询数据库
		/*User loginUser=userService.login(model);
		//判断
		if(null==loginUser){
			//登录失败
			//提示用户
			addActionError(getText("UserAction.loginerror"));
			//跳转到登录页面
			return LOGIN;
		}else{
			//登录成功
			//将用户放入session
//			ServletActionContext.getRequest().getSession().setAttribute("loginUser", loginUser);
			setToSession("loginUser", loginUser);
			//跳转到主页
			return SUCCESS;
		}*/
		
		//shiro方式的登录（认证）==================
		
		//获取subject对象（用户封装对象）
		Subject subject = SecurityUtils.getSubject();
		//获取token(令牌，腰牌，封装用户名密码)
		AuthenticationToken authenticationToken=
				new UsernamePasswordToken(model.getUsername(), MD5Utils.md5(model.getPassword()));
		
		//shiro认证动作
		//java开发时用来判断：
		//两种：1:布尔值判断：true或false
		//2：异常判断：是否有Exception
		try {
			subject.login(authenticationToken);
			//登录成功
			//跳转到主页
			return SUCCESS;
		} 
		catch (UnknownAccountException e) {
			e.printStackTrace();
			//登录失败,用户不存在
			//提示用户
			addActionError(getText("UserAction.usernamenoexist"));
			//跳转到登录页面
			return LOGIN;
		} 
		catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			//登录失败,密码不正确
			//提示用户
			addActionError(getText("UserAction.passwordIncorrect"));
			//跳转到登录页面
			return LOGIN;
		} 
		catch (AuthenticationException e) {
			//If unsuccessful, an AuthenticationException is thrown
			e.printStackTrace();
			//登录失败
			//提示用户
			addActionError(getText("UserAction.loginerror"));
			//跳转到登录页面
			return LOGIN;
		}
			
		
	}
	
	//退出登录-注销用户
	@Action(value="user_logout",
			results={@Result(name=LOGIN,type="redirect",location="/login.jsp")})
	public String logout(){
		
		//两步：
		//====================传统方式
		//清除session
//		ServletActionContext.getRequest().getSession().removeAttribute("loginUser");//不推荐
		//清除所有用户自定义相关session信息（httpSession）
//		ServletActionContext.getRequest().getSession().invalidate();
		
		//====================shiro方式：不但要释放session还要释放其他资源
		//提：“Session”：shiro的session：org.apache.shiro.session.Session
		//shiro可以用在任何的环境下：java和web
		//web：httpsession，shirosession封装httpsession
		//java：shiro自己的session
		SecurityUtils.getSubject().logout();
		//跳转到登录页面
		return LOGIN;		
	}
	
	//修改密码
//	@Action(value="user_editpassword",
//			results={@Result(name="json",type="json")}
//			results={@Result(name=JSON,type=JSON)}
//			)
	@Action("user_editpassword")
	public String editpassword(){
		//获取当前登录用户
//		User loginUser=(User) getFromSession("loginUser");
		//shiro方式获取认证后的用户对象
		User loginUser=(User) SecurityUtils.getSubject().getPrincipal();
		
		//将用户id传入model
		model.setId(loginUser.getId());
		//存放结果数据
		Map<String, Object> resultMap =new HashMap<>();
		
		try {
			//调用业务层修改
			userService.updatePasswordById(model);
			//修改成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			//修改成功
			resultMap.put("result", false);
		}
		
		//将map压如root栈顶
//		ActionContext.getContext().getValueStack().push(resultMap);
		pushToValuestackRoot(resultMap);
		
		//跳转到json类型的结果集上
//		return "json";
		return JSON;
		
	}
	
	//添加用户
	@Action(value="user_add",
			results={@Result(name=SUCCESS,location="/WEB-INF/pages/admin/userlist.jsp")})
	public String add(){
		userService.saveUser(model,roleIds);
		
		return SUCCESS;
	}
	
	//角色
	private String[] roleIds;
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	
	//用户列表
	@Action("user_list")
	public String list(){
		//业务层调用
		List<User> userList = userService.findUserList();
		//root栈顶
		pushToValuestackRoot(userList);
		return JSON;
	}
	
}
