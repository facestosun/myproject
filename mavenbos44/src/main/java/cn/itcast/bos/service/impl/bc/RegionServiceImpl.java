package cn.itcast.bos.service.impl.bc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.RegionDAO;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.bc.RegionService;

//区域业务层实现
@Service("regionService")
@Transactional
public class RegionServiceImpl extends BaseService implements RegionService{
	//注入dao
	@Autowired
	private RegionDAO regionDAO;

	@Override
	public void saveRegion(List<Region> regionList) {
		//批量保存
		regionDAO.save(regionList);
	}

	@Override
	public Page<Region> findRegionListPage(Pageable pageable) {
		return regionDAO.findAll(pageable);
	}

	@Override
	public List<Region> findRegionList() {
		return regionDAO.findAll();
	}

	@Override
	public List<Region> findRegionList(String param) {
		//属性表达式
		return regionDAO.findByProvinceLikeOrCityLikeOrDistrictLikeOrShortcodeOrCitycode
				("%"+param+"%", "%"+param+"%", "%"+param+"%", param, param);
		//语句
//		return regionDAO.findListByProvinceLikeOrCityLikeOrDistrictLikeOrShortcodeOrCitycode
//				("%"+param+"%",  param);
	}

}
