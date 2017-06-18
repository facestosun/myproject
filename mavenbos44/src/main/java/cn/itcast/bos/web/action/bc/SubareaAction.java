package cn.itcast.bos.web.action.bc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
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

import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.bc.SubareaService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.base.BaseAction;

//分区action
@Controller("subareaAction")
@Scope("prototype")
@ParentPackage("basic-bos")
@Namespace("/")
public class SubareaAction extends BaseAction<Subarea>{
	//注入业务层
	@Autowired
	private SubareaService subareaService;
	//保存添加分区
	@Action(value="subarea_add",
			results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/subarea.jsp")})
	public String add(){
		//调用业务层保存
		subareaService.saveSubarea(model);
		
		return SUCCESS;
	}
	
	//组合条件分页列表查询
	@Action("subarea_listPage")
	public String listPage(){
		//第一步：获取页面的参数（免代码）:分页参数（父类代码）+业务参数（模型驱动）
		//第二步：选择一种分页查询方案调用业务层
		//sql:手动拼接条件：业务和分页
		//criteria：业务条件需要手动拼接
		//今天：springdatajpa Speciafication方案：封装了jpa的criteria
		
		//分页条件
		Pageable pageable=new PageRequest(page-1, rows);
		//业务规范条件
		Specification<Subarea> spec = getSubareaSpecification();
		//调用业务层查询
		//参数1：业务规范条件
		//参数2：分页条件
		Page<Subarea> pageResponse = subareaService.findSubareaListPage(spec, pageable);
		//组装数据
		Map<String, Object> resultMap =new HashMap<>();
		//总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		//当前页列表
		resultMap.put("rows", pageResponse.getContent());
		//4.压入root栈顶
		pushToValuestackRoot(resultMap);
		
		
		return JSON;
	}

	/**
	 * 
	 * 说明：//业务规范条件
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月9日 上午9:38:23
	 */
	private Specification<Subarea> getSubareaSpecification() {
		Specification<Subarea> spec=new Specification<Subarea>() {
			
			@Override
			//参数1：查询的主对象root对象（主表），相当于Detacxxx.forClass(Subarea.class)
			//参数2：简单查询条件对象,功能不强 jpa
			//参数3：条件构建对象，jpa where后面的条件 name =?...and.... or....
			//返回值：where后面的条件，是由cb构建出来的。
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//定义list
				List<Predicate> andPredicateList=new ArrayList<>();//and
				List<Predicate> orPredicateList=new ArrayList<>();//or
				
				//------------单对象，单表
				//关键字
				if(StringUtils.isNotBlank(model.getAddresskey())){
					
					//类似：Restrictions.eq(propertyName, value)
					//参数1：属性包装对象，
					//参数2：属性值
					//p1:addresskey=？
//					Predicate p1 = cb.equal(root.get("addresskey"), model.getAddresskey());
					Predicate p1 = cb.equal(root.get("addresskey").as(String.class), model.getAddresskey());
					andPredicateList.add(p1);
				}
				//定区编号
				if(model.getDecidedZone()!=null&&StringUtils.isNotBlank(model.getDecidedZone().getId())){
					
					Predicate p2 = cb.equal(root.get("decidedZone"), model.getDecidedZone());
//					cb.equal(root.get("decidedZone.id"), model.getDecidedZone().getId());
					andPredicateList.add(p2);
				}
				
				
				//-----------关联对象，多表
				if(model.getRegion()!=null){
					//对象关联
					//参数1：连接的关联属性的包装类型
					//参数2：关联的类型,该属性可以省略，默认内联
					Join<Subarea, Region> regionJoin = root.join(root.getModel().getSingularAttribute("region", Region.class), JoinType.INNER);
					//省
					if(StringUtils.isNotBlank(model.getRegion().getProvince())){
						
						Predicate p3 = cb.like(regionJoin.get("province").as(String.class), "%"+model.getRegion().getProvince()+"%");
						andPredicateList.add(p3);
					}
					//市
					if(StringUtils.isNotBlank(model.getRegion().getCity())){
						
						Predicate p4 = cb.like(regionJoin.get("city").as(String.class), "%"+model.getRegion().getCity()+"%");
						andPredicateList.add(p4);
					}
					//区
					if(StringUtils.isNotBlank(model.getRegion().getDistrict())){
						
						Predicate p5 = cb.like(regionJoin.get("district").as(String.class), "%"+model.getRegion().getDistrict()+"%");
						andPredicateList.add(p5);
					}
				}
				
				//将所有的条件合并成一个大条件
//				cb.and(p1,p2,p3...);//全是and关系
//				cb.or(p5,p6,p7..);//全是or关系
//				cb.and(andPredicateList.toArray());
				Predicate andP = cb.and(andPredicateList.toArray(new Predicate[0]));
				//玩法：堆积木
				//比如：(a and b ) or (c or d)
//				p1 =cb.and(a,b);
//				p2=cb.or(c,d);
//				cb.or(p1,p2)
				
				return andP;
			}
		};
		return spec;
	}
	
	//导出excel
	@Action("subarea_exportData")
	public String exportData() throws Exception{
		//目标：下载excel
		//操作：三步
		//第一步：根据业务条件获取分区列表，不带分页
		Specification<Subarea> spec = getSubareaSpecification();
		List<Subarea> subareaList= subareaService.findSubareaList(spec);
		
		//第二步：创建一个excel，写入excel‘
		//POI技术
		//1)创建一个新(空)的工作簿
		//97格式
		HSSFWorkbook workbook=new HSSFWorkbook();
		//2)创建一个工作表
//		workbook.createSheet();//默认名字的表
		HSSFSheet sheet = workbook.createSheet("分区数据");//带名字的表
		
		//3)一行一行写数据
		//写表头
		HSSFRow headerRow = sheet.createRow(0);
		//分区编号	区域编码	关键字	起始号	结束号	单双号	位置信息
		//一格一格写
		headerRow.createCell(0).setCellValue("分区编号");
		headerRow.createCell(1).setCellValue("区域编码");
		headerRow.createCell(2).setCellValue("关键字");
		headerRow.createCell(3).setCellValue("起始号");
		headerRow.createCell(4).setCellValue("结束号");
		headerRow.createCell(5).setCellValue("单双号");
		headerRow.createCell(6).setCellValue("位置信息");
		
		//写表数据
		for (Subarea subarea : subareaList) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			//一格一格写
			dataRow.createCell(0).setCellValue(subarea.getId());
			dataRow.createCell(1).setCellValue(subarea.getRegion().getId());
			dataRow.createCell(2).setCellValue(subarea.getAddresskey());
			dataRow.createCell(3).setCellValue(subarea.getStartnum());
			dataRow.createCell(4).setCellValue(subarea.getEndnum());
			dataRow.createCell(5).setCellValue(subarea.getSingle().toString());//类型会导致api报错
			dataRow.createCell(6).setCellValue(subarea.getPosition());
		}
		
		//第三步：将excel的流写入响应，交给浏览器
		//获取响应
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//设置两个头信息
		//设置客户端浏览器用于识别附件的两个参数Content-Type和Content-Disposition
		//文件名
		String downFilename="分区数据.xls";
		//获取文件的MIME类型：
		String contentType=ServletActionContext.getServletContext().getMimeType(downFilename);
		//将MIME类型放入响应
		ServletActionContext.getResponse().setContentType(contentType);
		//浏览器类型
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		//附件名编码，解决中文乱码问题
		downFilename = FileUtils.encodeDownloadFilename(downFilename, agent);
		//获取附件的名字和下载方式
		String contentDisposition="attachment;filename="+downFilename;
		//将附件名字和下载方式放入响应头信息中
		ServletActionContext.getResponse().setHeader("Content-Disposition", contentDisposition);
		
		
		//写入浏览器
		workbook.write(response.getOutputStream());
		
		return NONE;
	}
	
	//查询没有关联定区的分区的列表
	@Action("subarea_listNoDecidedZoneId")
	public String listNoDecidedZoneId(){
		List<Subarea> subareaList= subareaService.findSubareaListNoDecidedZoneId();
		//压入栈顶
		pushToValuestackRoot(subareaList);
		return JSON;
	}
}
