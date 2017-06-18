package cn.itcast.bos.service.auth;

import java.util.List;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;

//功能权限业务层接口
public interface FunctionService {

	/**
	 * 查询所有功能权限
	 * 说明：
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月16日 上午10:26:19
	 */
	public List<Function> findFunctionList();

	/**
	 * 
	 * 说明：保存功能权限
	 * @param model
	 * @author 传智.BoBo老师
	 * @time：2017年3月16日 上午10:58:36
	 */
	public void saveFunction(Function function);

	/**
	 * 
	 * 说明：根据不同用户查询不同菜单列表
	 * @param user
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2017年3月16日 下午4:10:42
	 */
	public List<Function> findMenu(User user);

}
