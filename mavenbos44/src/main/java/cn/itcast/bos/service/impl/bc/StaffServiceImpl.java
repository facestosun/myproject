package cn.itcast.bos.service.impl.bc;
//取派员操作的业务层实现

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.StaffDAO;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.bc.StaffService;

@Service("staffService")
@Transactional
public class StaffServiceImpl extends BaseService implements StaffService{
	//注入dao
	@Autowired
	private StaffDAO staffDAO;

	@Override
	public void saveStaff(Staff staff) {
		staffDAO.save(staff);
	}

	@Override
	public Page<Staff> findAll(Pageable pageable) {
		return staffDAO.findAll(pageable);
	}

	@Override
	public void deleteStaffBatch(String ids) {
		//割
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			//调用dao
			//两种：
			//1）hibernate的快照：先查再改，等flush
			//2）自定义update语句
			staffDAO.updateDeltagById(id, '1');
		}
		
	}

	@Override
	public List<Staff> findStaffListNoDel() {
		return staffDAO.findByDeltag('0');
	}

}
