package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.bc.Staff;

//取派员业务层接口
public interface StaffService {

	/**
	 * 
	 * 说明：保存取派员
	 * @param staff
	 * @author 传智.BoBo老师
	 * @time：2017年3月6日 下午2:51:55
	 */
	public void saveStaff(Staff staff);
	
	/**
	 * 
	 * 说明：分页列表查询所有数据
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月6日 下午3:21:15
	 */
	public Page<Staff> findAll(Pageable pageable);

	/**
	 * 
	 * 说明：批量作废
	 * @param ids
	 * @author 传智.BoBo老师
	 * @time：2017年3月6日 下午4:38:39
	 */
	public void deleteStaffBatch(String ids);

	/**
	 * 
	 * 说明：查询正常状态的取派员列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月9日 上午10:24:48
	 */
	public List<Staff> findStaffListNoDel();

}
