package cn.itcast.bos.web.interceptor;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

//登录拦截器
@Component("loginInterceptor")
public class LoginInterceptor extends MethodFilterInterceptor{

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		//判断
		if(null==ServletActionContext.getRequest().getSession().getAttribute("loginUser")){
			//没有登录
			//返回到登录页面
//			return "login";
			return Action.LOGIN;
		}
		//放行，继续调用下一个拦截器
		return invocation.invoke();
	}

}
