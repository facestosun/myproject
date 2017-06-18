package cn.itcast.bos.web.action.bc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.service.bc.RegionService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.BaseAction;

@ParentPackage("basic-bos")
@Namespace("/")
@Controller("regionAction")
@Scope("prototype")
public class RegionAction extends BaseAction<Region>{
	
	//注入业务层
	@Autowired
	private RegionService regionService;
	
	
	//在值栈中放入属性，用来接收文件的参数
	private File upload;//文件对象，和参数同名的属性的名字
	private String uploadFileName;//文件名
	private String uploadContentType;//文件类型
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	//导入excel
	@Action("region_importData")
	public String importData(){
		//声明一个list，临时存放数据
		List<Region> regionList =new ArrayList<>();
		
		//结果map
		Map<String, Object> resultMap=new HashMap<String, Object>();
		
		
		//两大步：
		//第一步：拿到客户端上传的文件
		//使用struts2的拦截器，自动获取文件对象
		//第二步：解析文件excel，入库
		//如何解析excel：apache POI技术
		try {
			//通过poi api分析得知，api有两套，一套是HSSF:97格式，一套是XSSF：2007格式
			//需要通过判断选择。
			//下面使用97格式演示，（2007自己写，api完全）
			//读取excel：？？？和实际你读取excel的方式是一样。
			//先引入jar
			//1)打开工作簿
			//参数1：要读取的文件的流
			//97格式
			HSSFWorkbook workbook =new HSSFWorkbook(new FileInputStream(upload));
			//如果2007格式
//			XSSFWorkbook workbook2=new XSSFWorkbook(new FileInputStream(upload));
			
			//2)打开要读取的工作表
//			workbook.getSheet(name);//根据名字来获取
			HSSFSheet sheet = workbook.getSheetAt(0);//根据索引来获取
			//3.遍历表
			for (Row row : sheet) {
				//跳过第一行
				if(row.getRowNum()==0){
					continue;
				}
				//从第二行开始读取
				String id = row.getCell(0).getStringCellValue();
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				
				//new 瞬时态对象
				Region region=new Region();
				region.setId(id);
				region.setProvince(province);
				region.setCity(city);
				region.setDistrict(district);
				region.setPostcode(postcode);
				
				//简码：山东省荣成市石岛区—》SDRCSD
				//省
				String provinceStr=province.substring(0, province.length()-1);
				//市
				String cityStr=city.substring(0, city.length()-1);
				//区
				String districtStr=district.substring(0, district.length()-1);
				//拼接
				String shortcodeStr=provinceStr+cityStr+districtStr;
				//转换拼音
				String[] shortcodeArray = PinYin4jUtils.getHeadByString(shortcodeStr);
				//变成字符串
				String shortcode = StringUtils.join(shortcodeArray);
				region.setShortcode(shortcode);
				//城市编码:上海市 –》shanghai
				//转换
				String citycode = PinYin4jUtils.hanziToPinyin(cityStr,"");
				region.setCitycode(citycode);
				
				//添加到list
				regionList.add(region);
			}
			
			//调用业务层进行保存
			regionService.saveRegion(regionList);
			//成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			//处理
			//失败
			resultMap.put("result", false);
		}
		
		//压入栈顶json:{result:true}
		pushToValuestackRoot(resultMap);
		//跳转到json类型上
		return JSON;
	}
	
	//分页列表查询
	@Action("region_listPage")
	public String listPage(){
		//1·获取请求两个参数，封装到pageable中
		Pageable pageable=new PageRequest(page-1, rows);
		//2.调用业务层
		Page<Region> pageResponse= regionService.findRegionListPage(pageable);
		//3.重新组装数据
		Map<String, Object> resultMap =new HashMap<>();
		//总记录数
		resultMap.put("total", pageResponse.getTotalElements());
		//当前页列表
		resultMap.put("rows", pageResponse.getContent());
		//4.压入root栈顶
		pushToValuestackRoot(resultMap);
		//返回json类型结果集上
		return JSON;
	}
	
	//属性驱动封装-父类
//	private int page;//页码
//	private int rows;//每页最大记录数
//	public void setPage(int page) {
//		this.page = page;
//	}
//	public void setRows(int rows) {
//		this.rows = rows;
//	}
	
	//查询所有区域列表
	@Action("region_listAjax")
	public String listAjax(){
		//先获取q的参数（用户输入的）
		String param = getFromParameter("q");
		//列表
		List<Region> regionList=null;
		if(StringUtils.isBlank(param)){
			//调用业务层查询所有
			regionList=regionService.findRegionList();
		}else{
			//根据参数值动态查询
			regionList=regionService.findRegionList(param);
		}
		
		
		//压入root栈顶
		pushToValuestackRoot(regionList);
		
		return JSON;
	}

}
