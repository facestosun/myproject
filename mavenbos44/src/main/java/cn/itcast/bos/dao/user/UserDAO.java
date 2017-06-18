package cn.itcast.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.itcast.bos.domain.user.User;
import java.lang.String;
import java.util.List;

//用户的dao
public interface UserDAO extends JpaRepository<User, String>{

	//=============属性表达式
	//--根据用户名来查询用户对象
	//参数：必须对应字段属性：个数和类型
	//返回值：推荐：实体类型（保证最多返回一条数据），list类型(不能保证返回最多一条，只能用list)，结果自动封装到实体类中。
	public User findByUsername(String username);
	//----根据用户名模糊匹配用户--列表
	public List<User> findByUsernameLike(String username);
	
	
	//============jpa命名查询的方式(缺点，不推荐)
	public String findPasswordByUsername(String username);
	
	//===========Query注解--推荐
	@Query(value="select password from User where username =?")//hql
//	@Query(value="select password from t_user where username =?",nativeQuery=true)//sql
	public String findPasswordByUsername2(String username);
	
	//----参数占位符的使用
	//1。匿名参数占位符:个数、数据类型、顺序都必须和参数一致
	@Query("from User where username =? and password =?")
	public User findUserByUsernameAndPassword1(String username ,String password);
	//2。命名参数占位符：参数名字一致即可，名字随意
	@Query("from User where username =:username and password =:pwd")
	public User findUserByUsernameAndPassword2(@Param("pwd")String password,@Param("username")String username );
	//2。JPA参数占位符：根据参数的索引来对应参数的值
	@Query("from User where username =?2 and password =?1")
	public User findUserByUsernameAndPassword3(String password,String username );
	
	
	//------登录业务：根据用户名和密码查询
	public User findByUsernameAndPassword(String username ,String password);
	
	//根据id修改密码
	@Query("update User set password =?2 where id =?1")//hql
	//增删改，必须加上下面的注解
	@Modifying
	public void updatePasswordById(String id,String password);
	
	
}
