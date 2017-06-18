package cn.itcast.bos.service.impl.qp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import cn.itcast.bos.dao.bc.DecidedZoneDAO;
import cn.itcast.bos.dao.bc.RegionDAO;
import cn.itcast.bos.dao.qp.NoticeBillDAO;
import cn.itcast.bos.dao.qp.WorkBillDAO;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.qp.WorkBill;
import cn.itcast.bos.dto.bc.Customer;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.qp.NoticeBillService;

//通知单操作
@Service("noticeBillService")
@Transactional
public class NoticeBillServiceImpl extends BaseService implements NoticeBillService{
	//注入dao
	@Autowired
	private NoticeBillDAO NoticeBillDAO;
	//注入定区dao
	@Autowired
	private DecidedZoneDAO decidedZoneDAO;
	
	//注入工单dao
	@Autowired
	private WorkBillDAO workBillDAO;
	
	//注入区域dao
	@Autowired
	private RegionDAO regionDAO;

	@Override
	public void saveNoticeBill(NoticeBill noticeBill) {
		
		//保存通知单
		NoticeBillDAO.save(noticeBill);//持久态，jpa抢占主键（找hibernate抢）
		//hibernate的抢占主键策略
		//如果主键是数据库层面生成的，那么在调用save方法的时候，会立即发出sql，如mysql
		//但如果主键不是数据库层面生成的，那么在调用save方法的时候，不会立即发出sql，等commit之前flush发出了。
		
		//数据库层面生成主键的策略是怎么订？
		//mysql:主键自增长，只需要发送insert语句即可拥有id
		//oracle:使用序列（自动生成一个序列）来提供主键的值。发送select HIBERNATE_SEQUENCE.nextxx() from dual;
		
		noticeBill.setOrdertype("人工");
		
		//拿出来：地址
		String address = noticeBill.getPickaddress();
		
		//===========================1.	客户地址的完全匹配（CRM系统调用）--优先
		//远程调用ws接口
		Customer customer = WebClient.create("http://localhost:8888/mavencrm44/services/CustomerWS/customerService/customers")
		.path("/"+address)//地址
		.path("/decidedZone/")
		.type(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.get(Customer.class);
		//判断
		if(customer!=null && StringUtils.isNotBlank(customer.getDecidedZoneId())){
			//找到定区了，查询定区
			DecidedZone decidedZone = decidedZoneDAO.findOne(customer.getDecidedZoneId());
			if(decidedZone!=null){
				//找到取派员了(导航查询)
				Staff staff = decidedZone.getStaff();
				//2件事情
				//1)通知单的信息修改
				noticeBill.setOrdertype("自动");
				noticeBill.setStaff(staff);
				//2)给取派员下一个工单(工单表插入一条数据)
				saveWorkBill(noticeBill, staff);
				//规则1走完：返回结束
				return;
			}
		}
		
		//===========================2.	客户地址中的关键字匹配（最终目标还是找定区-有取派员)
		//先寻找区域(通过省市区锁定一个区域)
		//省市区一定是联动选择的（实际）-课后
		//这里截取字符串（不安全的有bug）
		//上海市上海市浦东新区
		//山东省济南市长河区
		//先找索引
		//省
		int provinceIndex = address.indexOf("省");
		//找不到，是直辖市
		if(provinceIndex==-1){
			provinceIndex = address.indexOf("市");
		}
		//市
		int cityIndex=address.indexOf("市", provinceIndex+1);
		//区
		int districtIndex=address.indexOf("区", cityIndex+1);
		//截取字符串
		//省
		String province = address.substring(0, provinceIndex+1);
		//市
		String city = address.substring(provinceIndex+1, cityIndex+1);
		//区
		String district = address.substring( cityIndex+1, districtIndex+1);
		
		//调用dao查询,获取到区域
		Region region = regionDAO.findByProvinceAndCityAndDistrict(province,city,district);
		//找到地址所属区域了
		if(region!=null){
			//先获取区域下面的所有分区(导航查询
			Set<Subarea> subareaSet = region.getSubareas();
			
			if(!subareaSet.isEmpty()){
				for (Subarea subarea : subareaSet) {
					//根据关键字找分区）（区域下面有很多分区）
					if(address.contains(subarea.getAddresskey())){
						//找到分区了，就找到了定区
						DecidedZone decidedZone = subarea.getDecidedZone();
						if(decidedZone!=null){
							//定区不为空，又找到取派员了
							Staff staff = decidedZone.getStaff();
							//2件事情
							//1)通知单的信息修改
							noticeBill.setOrdertype("自动");
							noticeBill.setStaff(staff);
							//2)给取派员下一个工单(工单表插入一条数据)
							saveWorkBill(noticeBill, staff);
							//规则1走完：返回结束
							return;
						}
						//如果找到，不找了
						break;
					}
				}
			}
			
			
		}
		
		
	}

	//保存工单，下工单给取派员
	private void saveWorkBill(NoticeBill noticeBill, Staff staff) {
		WorkBill workBill = new WorkBill();
//				workBill.setId(id);//oid
		workBill.setNoticeBill(noticeBill);//通知单的外键，持久态
		workBill.setType("新");//工单类型：新,追,销--主要客服在操作工单类型，但取派员也能间接操作
		workBill.setPickstate("新单");//取件状态:主要给取派员用的
		workBill.setBuildtime(new Date());//工单生成时间
		workBill.setAttachbilltimes(new BigDecimal(0));//追单次数：主要客服在操作,每次催单+1
		workBill.setStaff(staff);//取派员编号，外键
		workBill.setRemark(noticeBill.getRemark());//用户的备注(冗余设计)
		
		//保存操作
		workBillDAO.save(workBill);
		
		//发送短信的代码在下面
	}
	
	//发短信
	public static void main(String[] args) {
		//发送短信
		try {
			//参数1：短信接口url
			//参数2：应用的key
			//参数3：安全码
			TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", "23695203", "52c48e18476415d10e3273e8313c3250");
			AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
			req.setExtend( "" );
			req.setSmsType( "normal" );
			req.setSmsFreeSignName( "波波老师" );//签名
			req.setSmsParamString( "{'name':'Rose','time':'2017-01-01'}" );//参数
			req.setRecNum( "18516566511" );
			req.setSmsTemplateCode( "SMS_50750022" );
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			System.out.println(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
