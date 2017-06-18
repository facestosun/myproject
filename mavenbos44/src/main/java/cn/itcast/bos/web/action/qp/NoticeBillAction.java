package cn.itcast.bos.web.action.qp;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.qp.NoticeBillService;
import cn.itcast.bos.web.action.base.BaseAction;

//通知单操作
@Controller("noticeBillAction")
@Scope("prototype")
@Namespace("/")
@ParentPackage("basic-bos")
public class NoticeBillAction extends BaseAction<NoticeBill>{
	
	//注入service
	@Autowired
	private NoticeBillService noticeBillService;

	//新单
	@Action(value="noticeBill_add",
			results={@Result(name=SUCCESS,location="/WEB-INF/pages/qupai/noticebill_add.jsp")})
	public String add(){
		//获取当前登录人
		User loginUser= (User) getFromSession("loginUser");
		//放入model中
		model.setUser(loginUser);
		
		//业务层调用
		noticeBillService.saveNoticeBill(model);
		
		return SUCCESS;
	}
}
