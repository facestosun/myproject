package cn.itcast.bos.web.action.bc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.dto.bc.Customer;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.bos.web.action.base.BaseAction;

//定区管理action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller("decidedZoneAction")
@Scope("prototype")
public class DecidedZoneAction extends BaseAction<DecidedZone>{

	//注入service
	@Autowired
	private DecidedZoneService decidedZoneService;
	//添加定区
	@Action(value="decidedZone_add",
			results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String add(){
		//调用业务层
		decidedZoneService.saveDecidedZone(model,subareaId);
		
		return SUCCESS;
	}
	
	//属性驱动封装分区编号
	private String[] subareaId;
	public void setSubareaId(String[] subareaId) {
		this.subareaId = subareaId;
	}
	
	//组合条件分页列表查询
	@Action("decidedZone_listPage")
	public String listPage(){
		//今天还是使用spec方案
		//第一步：封装请求bean：
		//分页条件
		Pageable pageable =new PageRequest(page-1, rows);
		//业务条件
		Specification<DecidedZone> spec = new Specification<DecidedZone>() {
			
			@Override
			public Predicate toPredicate(Root<DecidedZone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//spec方案提供了两个集合的封装对象
				Predicate conjunctionPredicate = cb.conjunction();//存放and关系的p
				Predicate disjunctionPredicate = cb.disjunction();//存放的or关系的p
				
				
				//------单表
				//定区编码
				if(StringUtils.isNotBlank(model.getId())){
					conjunctionPredicate.getExpressions().add(
					cb.equal(root.get("id").as(String.class), model.getId())
					);
				}
				
				//是否关联分区
				if(StringUtils.isNotBlank(hasSubarea)){
					if(hasSubarea.equals("0")){
						//定区没有关联分区
//						cb.isNull(x);//判断某对象是否为空
//						cb.isEmpty(collection);//判断某集合的对象的元素是否为空
						//这里用第二个
						conjunctionPredicate.getExpressions().add(
						cb.isEmpty(root.get("subareas").as(Set.class))
						);
					}else{
						//定区关联分区
						conjunctionPredicate.getExpressions().add(
						cb.isNotEmpty(root.get("subareas").as(Set.class))
						);
					}
				}
				
				
				//-----多表数据
				//先关联
				if(model.getStaff()!=null){
					//内联
					Join<DecidedZone, Staff> staffJoin = root.join(root.getModel().getSingularAttribute("staff", Staff.class), JoinType.INNER);
					//位置
					if(StringUtils.isNotBlank(model.getStaff().getStation())){
						conjunctionPredicate.getExpressions().add(
						cb.like(staffJoin.get("station").as(String.class), "%"+model.getStaff().getStation()+"%")
						);
					}
				}
				
				
				return conjunctionPredicate;
			}
		};
		//第二步：请求service查询数据
		Page<DecidedZone> pageResponse= decidedZoneService.findDecidedZoneListPage(spec,pageable);
		//第三步：重新组装shuju
		Map<String, Object> resultMap =new HashMap<String, Object>();
		//总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		//当前页的数据列表
		resultMap.put("rows", pageResponse.getContent());
		//第四步：压入root栈顶
		pushToValuestackRoot(resultMap);
		return JSON;
	}
	
	//属性驱动
	private String hasSubarea;
	public void setHasSubarea(String hasSubarea) {
		this.hasSubarea = hasSubarea;
	}
	
	
	//查询没有关联定区的客户列表
	@Action("decidedZone_listCustomerListNoDecidedZoneId")
	public String listCustomerListNoDecidedZoneId(){
		//调用接口
		Collection<? extends Customer> customerList = WebClient.create("http://localhost:8888/mavencrm44/services/CustomerWS/customerService/customers")
		.type(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.getCollection(Customer.class);
		
		//压入root栈顶
		pushToValuestackRoot(customerList);
		//跳转到json类型结果集上
		return JSON;
	}
	
	//查询已经关联定区的客户列表
	@Action("decidedZone_listCustomerListHasDecidedZoneId")
	public String listCustomerListHasDecidedZoneId(){
		//调用接口
		Collection<? extends Customer> customerList = WebClient.create("http://localhost:8888/mavencrm44/services/CustomerWS/customerService/customers")
			.path("/"+model.getId())
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.getCollection(Customer.class);
		
		//压入root栈顶
		pushToValuestackRoot(customerList);
		//跳转到json类型结果集上
		return JSON;
	}
	
	@Action(value="decidedzone_assignCustomersToDecidedzone"
			,results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String assignCustomersToDecidedzone(){
		//获取和拼接客户编号(,格式)
		String cIds = StringUtils.join(customerIds, ",");
		//接口调用
		WebClient.create("http://localhost:8888/mavencrm44/services/CustomerWS/customerService/customers")
		.path("/"+model.getId())
		.path("/"+cIds)
		.type(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.put(null);//没有对象传递
		
		return SUCCESS;
	}
	
	//属性驱动
//	private String customerIds;//同名参数自动封装为", "字符串
	private String[] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	
	
	
	
	
}
