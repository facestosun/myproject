package cn.itcast.bos.web.action.qp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.qp.WorkOrderManage;
import cn.itcast.bos.service.qp.WorkOrderManageService;
import cn.itcast.bos.web.action.base.BaseAction;

//工作订单的管理
@Controller("workOrderManage")
@Scope("prototype")
@Namespace("/")
@ParentPackage("basic-bos")
public class WorkOrderManageAction extends BaseAction<WorkOrderManage>{
	//注入service
	@Autowired
	private WorkOrderManageService workOrderManageService;

	//添加工作订单
	@Action("workOrderManage_add")
	public String add(){
		
		Map<String, Object> resultMap =new HashMap<>();
		try {
			//直接调用业务层
			workOrderManageService.saveWorkOrderManage(model);
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
		}
		//压入root栈顶
		pushToValuestackRoot(resultMap);
		return JSON;
	}
	
	//分页列表查询
	@Action("workOrderManage_listPage")
	public String listPage(){
		//封装分页条件
		Pageable pageable = new PageRequest(page-1, rows);
		
		Page<WorkOrderManage> pageReponse=null;
		if(StringUtils.isBlank(conditionValue)){
			//用户没有输入任何的查询词语，那么查询所有
			//调用业务层查询
			pageReponse=workOrderManageService.findWorkOrderManageListPage(pageable);
		}else{
			//使用全文检索查询方式
			pageReponse=workOrderManageService.findWorkOrderManageListPage(pageable,conditionName,conditionValue);
		}
		
		//组装数据
		Map<String, Object> resultMap=new HashMap<>();
		//总记录数
		resultMap.put("total", pageReponse.getTotalElements());
		//当前页的数据列表
		resultMap.put("rows", pageReponse.getContent());
		
		//压入栈顶
		pushToValuestackRoot(resultMap);
		//了解Map也可以自动生成(栈顶map不纯洁)
//		ActionContext.getContext().getValueStack().set("total", pageReponse.getTotalElements());
//		ActionContext.getContext().getValueStack().set("rows", pageReponse.getContent());
		
		return JSON;
	}
	
	//通过属性驱动
	private String conditionName;
	private String conditionValue;

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	
}
