package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Staff;

//取派员的dao接口
public interface StaffDAO extends JpaRepository<Staff, String>{
	
	//根据id更新删除标志
	@Query("update Staff set deltag=?2 where id =?1")
	@Modifying
	public void updateDeltagById(String id,Character deltag);
	
	//查询没有删除标记的取派员列表
	//属性表达式
	public List<Staff> findByDeltag(Character deltag);

}
