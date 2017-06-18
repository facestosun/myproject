package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.Subarea;

//分区业务层接口
public interface SubareaService {

	/**
	 * 
	 * 说明：保存一个分区
	 * @param model
	 * @author 传智.BoBo老师
	 * @time：2017年3月8日 下午3:47:46
	 */
	public void saveSubarea(Subarea subarea);
	
	/**
	 * 
	 * 说明：组合条件分页查询
	 * @param spec
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月8日 下午4:51:04
	 */
	public Page<Subarea> findSubareaListPage(Specification<Subarea> spec, Pageable pageable);

	/**
	 * 
	 * 说明：根据条件查询分区列表
	 * @param spec
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月9日 上午9:39:46
	 */
	public List<Subarea> findSubareaList(Specification<Subarea> spec);

	/**
	 * 
	 * 说明：查询没有关联定区的分区
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月9日 上午10:35:28
	 */
	public List<Subarea> findSubareaListNoDecidedZoneId();

}
