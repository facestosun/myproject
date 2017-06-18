package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Region;

//区域daoR
public interface RegionDAO extends JpaRepository<Region, String>{

	
	//根据省市区以及简码等进行模糊匹配查询区域列表
	//属性表达式
	public List<Region> findByProvinceLikeOrCityLikeOrDistrictLikeOrShortcodeOrCitycode
	(String province,String city,String district,String shortcode,String citycode);
	//Query注解
	@Query("from Region where provice like ?1 or city like ?1 or district like ?1 or shortcode =?2 or citycode =?2")
	public List<Region> findListByProvinceLikeOrCityLikeOrDistrictLikeOrShortcodeOrCitycode(String param1,String param2);
	
	//根据省市区完全匹配查询
	//属性表达式
	public Region findByProvinceAndCityAndDistrict(String province,String city,String district);
}
