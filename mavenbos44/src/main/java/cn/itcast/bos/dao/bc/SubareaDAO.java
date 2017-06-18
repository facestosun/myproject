package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.bc.Subarea;

//分区dao
public interface SubareaDAO extends JpaRepository<Subarea, String>,JpaSpecificationExecutor<Subarea>{

	//查询定区为空的分区
	public List<Subarea> findByDecidedZoneIsNull();
}
