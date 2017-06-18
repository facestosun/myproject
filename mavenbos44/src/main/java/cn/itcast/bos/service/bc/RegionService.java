package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.bc.Region;

//区域的业务层接口
public interface RegionService {

	/**
	 * 
	 * 说明：批量保存区域
	 * @param regionList
	 * @author 传智.BoBo老师
	 * @time：2017年3月8日 上午10:49:47
	 */
	public void saveRegion(List<Region> regionList);

	/**
	 * 
	 * 说明：分页列表查询所有
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月8日 下午2:39:42
	 */
	public Page<Region> findRegionListPage(Pageable pageable);

	/**
	 * 
	 * 说明：查询所有的区域
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月8日 下午2:57:04
	 */
	public List<Region> findRegionList();

	/**
	 * 
	 * 说明：根据参数条件查询区域列表
	 * @param param
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月8日 下午3:11:11
	 */
	public List<Region> findRegionList(String param);

}
