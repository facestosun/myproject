package cn.itcast.bos.service.bc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.DecidedZone;

//定区管理业务层接口
public interface DecidedZoneService {

	/**
	 * 
	 * 说明：保存定区并关联分区
	 * @param decidedZone
	 * @param subareaId
	 * @author 传智.BoBo老师
	 * @time：2017年3月9日 上午11:18:17
	 */
	public void saveDecidedZone(DecidedZone decidedZone, String[] subareaId);

	/**
	 * 
	 * 说明：组合条件分页查询定区
	 * @param spec
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月9日 下午2:48:26
	 */
	public Page<DecidedZone> findDecidedZoneListPage(Specification<DecidedZone> spec, Pageable pageable);

}
