package cn.itcast.bos.service.impl.auth;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FunctionDAO;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.auth.FunctionService;
import cn.itcast.bos.service.base.BaseService;

//功能权限业务层实现
@Service("functionService")
@Transactional
public class FunctionServiceImpl extends BaseService implements FunctionService{
	//注入dao
	@Autowired
	private FunctionDAO functionDAO;

	@Override
	public List<Function> findFunctionList() {
		return functionDAO.findAll();
	}

	@Override
	//清除缓存区域的所有元素
	@CacheEvict(value="MenuSpringCache",allEntries=true)
	public void saveFunction(Function function) {
		
		//生成主键（自定义规则）
		//当前节点的父节点如果有子节点，子节点数字+1
		//当前节点的父节点如果没有子节点，第一个子节点：父节点id+001，有一个特例，一级节点：固定写死101
		//查询选中的父节点的子节点id的最大值
		Function parentFunction = function.getFunction();
		
		String id=null;
		//调用dao查询
		String maxId = functionDAO.findMaxIdByParentFunction(parentFunction);
		if(StringUtils.isNotBlank(maxId)){
			//当前节点的父节点如果有子节点，子节点数字+1
			id=String.valueOf(Integer.parseInt(maxId)+1);
		}else{
			//当前节点的父节点如果没有子节点，第一个子节点：父节点id+001，有一个特例，一级节点：固定写死101
			if(parentFunction.getId().equals("0")){
				id="101";
			}else{
				id=parentFunction.getId()+"001";
			}
		}
		
		function.setId(id);
		//线程bug
		
		//保存
		functionDAO.save(function);
		
	}

	@Override
	//将当前查询的结果放入指定的缓存区域,value:缓存区域的名字
//	@Cacheable(value="MenuSpringCache")
	//缓存是map结构，如何存放的？key:value
	//value:结果
	//key:有三个生成策略
	//1)如果方法没有参数，则key为0
	//2)如果方法有一个参数，参数的对象本身作为key：地址
	//3)如果有多个参数，多个参数对象计算出的hash值做为key
	//key：user对象的地址
	//解决：将key指定为用户的id即可。
	@Cacheable(value="MenuSpringCache",key="#user.id")
	public List<Function> findMenu(User user) {
		//隐藏业务：超管特别
		if(user.getUsername().equals("admin")){
			//超管查询所有
			return functionDAO.findAllMenu();
		}else{
			//普通用户，根据权限查询
			//hql
//			return functionDAO.findMenuByUser(user);
			//sql
			return functionDAO.findMenuByUserId(user.getId());
		}
		
		
	}

}
