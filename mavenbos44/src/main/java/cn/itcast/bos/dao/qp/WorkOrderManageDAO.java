package cn.itcast.bos.dao.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.qp.WorkOrderManage;

//工作订单dao
public interface WorkOrderManageDAO extends JpaRepository<WorkOrderManage, String>{
	
	
	/**
	 * 
	 * 说明：//全文检索工作单
	 * @param pageable
	 * @param conditionName
	 * @param conditionValue
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月13日 下午5:23:01
	 */
	public Page<WorkOrderManage> searchByLucene(Pageable pageable, String conditionName, String conditionValue);
}
