package cn.itcast.bos.service.impl.bc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.DecidedZoneDAO;
import cn.itcast.bos.dao.bc.SubareaDAO;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.bc.DecidedZoneService;

//定区管理的业务层实现
@Service("decidedZoneService")
@Transactional
public class DecidedZoneServiceImpl extends BaseService implements DecidedZoneService {
	//注入定区dao
	@Autowired
	private DecidedZoneDAO decidedZoneDAO;
	//注入分区dao
	@Autowired
	private SubareaDAO subareaDAO;

	@Override
	public void saveDecidedZone(DecidedZone decidedZone, String[] subareaId) {
		// 两个
		//保存定区
//		decidedZoneDAO.save(decidedZone);//?瞬时态
		//如果需要抢占主键，那么springdatajpa就会立刻通知hibernate的api，该对象就会变成持久态
		//如果不需要抢占主键，springdatajpa不需要先通知hibernate，等事务提交之前统一通知即可。
		//解决：
		//强行放springdatajpa通知hibernate
		decidedZoneDAO.saveAndFlush(decidedZone);
		//关联分区
		if(subareaId!=null){
			for (String sId : subareaId) {
				//更新
				//hibernate快照
				Subarea subarea = subareaDAO.findOne(sId);//持久态
				subarea.setDecidedZone(decidedZone);
				//等待flush，事务提交之前自动
				//query注解语句
			}
		}
		
	}

	@Override
	public Page<DecidedZone> findDecidedZoneListPage(Specification<DecidedZone> spec, Pageable pageable) {
		return decidedZoneDAO.findAll(spec, pageable);
	}

}
