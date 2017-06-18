package cn.itcast.bos.service.impl.qp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.qp.WorkOrderManageDAO;
import cn.itcast.bos.domain.qp.WorkOrderManage;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.qp.WorkOrderManageService;

//工作订单业务层实现
@Service("workOrderManageService")
@Transactional
public class WorkOrderManageServiceImpl extends BaseService implements WorkOrderManageService{
	//注入dao
	@Autowired
	private WorkOrderManageDAO workOrderManageDAO;

	@Override
	public void saveWorkOrderManage(WorkOrderManage workOrderManage) {
		workOrderManageDAO.save(workOrderManage);
	}

	@Override
	public Page<WorkOrderManage> findWorkOrderManageListPage(Pageable pageable) {
		//调用dao
		return workOrderManageDAO.findAll(pageable);
	}

	@Override
	public Page<WorkOrderManage> findWorkOrderManageListPage(Pageable pageable, String conditionName, String conditionValue) {
		//调用dao
		return workOrderManageDAO.searchByLucene(pageable, conditionName, conditionValue);
	}

}
