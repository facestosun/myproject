package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;

//功能权限的dao
public interface FunctionDAO extends JpaRepository<Function, String>{

	//根据父节点查询其子节点id最大值
	@Query("select max(f.id) from Function f where f.function =?")//hql
	public String findMaxIdByParentFunction(Function function);
	
	
	//根据用户查询菜单列表
	//hql
	@Query("from Function f inner join fetch f.roles r inner join fetch r.users u where u=? and f.generatemenu='1' order by f.zindex asc")
	public List<Function> findMenuByUser(User user);
	
	//sql
	@Query(value="SELECT * FROM t_auth_function t1 INNER JOIN t_auth_role_function t2 ON t1.id=t2.function_id INNER JOIN t_auth_user_role t3 ON t2.role_id=t3.role_id WHERE t1.generatemenu='1' AND t3.operator_id=? ORDER BY t1.zindex ASC",nativeQuery=true)
	public List<Function> findMenuByUserId(String userId);
	
	//查询所有的菜单
	//hql
	@Query("from Function f where f.generatemenu='1' order by f.zindex asc")
	public List<Function> findAllMenu();
	//sql
	@Query(value="select * from t_auth_function t1 where t1.generatemenu='1' order by t1.zindex asc",nativeQuery=true)
	public List<Function> findAllMenu2();
}
