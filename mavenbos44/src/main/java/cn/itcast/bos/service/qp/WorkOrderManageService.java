package cn.itcast.bos.service.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.qp.WorkOrderManage;

//工作订单的业务层接口
public interface WorkOrderManageService {

	/**
	 * 
	 * 说明：保存工作订单
	 * @param workOrderManage
	 * @author 传智.BoBo老师
	 * @time：2017年3月13日 下午3:54:49
	 */
	public void saveWorkOrderManage(WorkOrderManage workOrderManage);

	/**
	 * 
	 * 说明：分页列表查询
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月13日 下午4:13:19
	 */
	public Page<WorkOrderManage> findWorkOrderManageListPage(Pageable pageable);

	/**
	 * 
	 * 说明：全文检索工作单
	 * @param pageable
	 * @param conditionName
	 * @param conditionValue
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月13日 下午5:20:54
	 */
	public Page<WorkOrderManage> findWorkOrderManageListPage(Pageable pageable, String conditionName, String conditionValue);

}
