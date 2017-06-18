package cn.itcast.bos.auth.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.itcast.bos.dao.auth.FunctionDAO;
import cn.itcast.bos.dao.auth.RoleDAO;
import cn.itcast.bos.dao.user.UserDAO;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;

//bos的realm（提供安全数据：两种：认证数据，授权数据）
@Component("bosRealm")
public class BosRealm extends AuthorizingRealm{
	
	//注入缓存的名字给父类的方法
	@Value("BosShiroCache")
	public void setSuperAuthenticationCacheName(String authenticationCacheName){
		super.setAuthenticationCacheName(authenticationCacheName);
	}
	
	
	//注入用户操作的dao
	@Autowired
	private UserDAO userDAO;
	
	//注入角色权限的dao
	@Autowired
	private RoleDAO roleDAO;
	
	//注入功能权限的dao
	@Autowired
	private FunctionDAO functionDAO;

	@Override
	//提供授权的安全数据
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		
		
		System.out.println("------开始获取授权数据了。。。。");
		//目标1：先写死数据演示授权通过
		SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();//授权信息对象
		//提供当前登录用户所具备授权信息
		//授权信息：两种：功能权限和角色权限。
		//注意：在授权业务中功能权限和角色权限是个多对多的关系
		//但在shiro中，他俩没关系，就是两条判断权限的两条路。
		/*
		 * /page_base_region.action = perms["user"]
				/page_base_subarea.action = roles["operator"]
		 */
		//功能权限 /page_base_region.action = perms["user"]
//		authorizationInfo.addStringPermission("user");
		//角色权限：/page_base_subarea.action = roles["operator"]
//		authorizationInfo.addRole("operator");
		//底层：字符串的对比
		
		//目标2：根据当前登录用户来查询其拥有哪些角色权限和功能权限
		//获取当前登录的用户名
		//两种方式
		//1.工具类获取subject
//		User user=(User)SecurityUtils.getSubject().getPrincipal();
		//2.通过参数PrincipalCollection来获取
		User user=(User)principals.getPrimaryPrincipal();
		
		
		if("admin".equals(user.getUsername())){
			//超管
			//要查询所有的角色
			List<Role> roleList = roleDAO.findAll();
			for (Role role : roleList) {
				authorizationInfo.addRole(role.getCode());
			}
			//所有的功能权限
			List<Function> functionList = functionDAO.findAll();
			for (Function function : functionList) {
				authorizationInfo.addStringPermission(function.getCode());
			}
		}else{
			//普通用户
			//根据多表关联来查询角色
			List<Role> roleList = roleDAO.findByUsers(user);
			for (Role role : roleList) {
				//角色的编码添加到授权信息对象中
				authorizationInfo.addRole(role.getCode());
				//导航查询
				Set<Function> functionSet = role.getFunctions();
				for (Function function : functionSet) {
					//将功能权限编码添加到授权信息中
					authorizationInfo.addStringPermission(function.getCode());
				}
			}
		}
		
		
		
		
		return authorizationInfo;
	}

	@Override
	//提供认证的安全数据
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("------开始获取认证数据了。。。。");
		//获取用户输入的用户名
		String username = ((UsernamePasswordToken)token).getUsername();
		
		//根据  用户输入的用户名    向数据库查询   用户对象
		User user = userDAO.findByUsername(username);
		if(user==null){
			//意味着，用户名不存在
			return null;//没有安全数据
		}else{
			//用户名存在。问题：密码对不对呢？谁来验证？安全管理器！只需要返回安全数据即可
			AuthenticationInfo authenticationInfo =
			//参数1：principal首长，主用户对象User，将来安全管理器会将该对象放入"session"
			//参数2：credentials：凭证,这里就是真实密码，将来安全管理器会拿它和用户输入的做对比！
			//参数3：realm的名字，父类的方法调用
					new SimpleAuthenticationInfo(user, user.getPassword(), getName());
			//将安全数据对象交给安全管理器
			return authenticationInfo;
		}
		
		//没有安全数据
//		return null;
	}

}
