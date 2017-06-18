package cn.itcast.bos.web.action.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

//action的父类，存放公用代码，简化开发
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	//声明数据模型对象引用
	protected T model;//子类可见

	@Override
	public T getModel() {
		return model;
	}
	
	//默认的构造器：子类会调用父类构造器，在实例化的时候
	public BaseAction() {
		// 初始化数据模型对象：通过反射机制
		//获取父类的参数化类型：BaseAction<T>
		/*Type baseType = this.getClass().getGenericSuperclass();
		//强转大类型为泛型化类型
		ParameterizedType parameterizedType=(ParameterizedType)baseType;
		//获取父类的参数花类型中的泛型参数的具体类型
		Class<T> clazz=(Class<T>) parameterizedType.getActualTypeArguments()[0];
		
		try {
			//实例化clazz:new xxx();
			model=clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}*/
		//----代码优化：
		//遍历寻找泛型化类型，获取T的类型
		//获取当前调用者的类型
		Class clazz = this.getClass();
		//遍历获取父类类型
		while(true){
			//父类类型
			Type type = clazz.getGenericSuperclass();
			//判断是否是泛型化类型
			if(type instanceof ParameterizedType){
				//强转大类型为泛型化类型
				ParameterizedType parameterizedType=(ParameterizedType)type;
				//获取父类的参数花类型中的泛型参数的具体类型
				Class<T> modelClass=(Class<T>) parameterizedType.getActualTypeArguments()[0];
				
				try {
					//实例化clazz:new xxx();
					model=modelClass.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				break;
			}
			//改变调用者类型引用
			clazz=(Class)type;
		}
		
	}
	
	//servlet操作的api
	//将对象放入session
	protected void setToSession(String key, Object value){
		ServletActionContext.getRequest().getSession().setAttribute(key, value);
	}
	//从session中获取对象
	protected Object getFromSession(String key){
		return ServletActionContext.getRequest().getSession().getAttribute(key);
	}
	
	//获取参数值
	protected String getFromParameter(String key){
		return ServletActionContext.getRequest().getParameter(key);
	}
	
	//值栈操作的
	//将对象压入栈顶
	protected void pushToValuestackRoot(Object value){
		ActionContext.getContext().getValueStack().push(value);
	}
	//将对象压入栈顶map中
	protected void setToValuestackRoot(String key,Object value){
		ActionContext.getContext().getValueStack().set(key, value);
	}
	//将对象压入map栈
	protected void putToValuestackMap(String key,Object value){
		ActionContext.getContext().put(key, value);
	}
	
	//自定义常量
	//json字符串
	public static final String JSON = "json";
	
	//分页参数的封装：
	//属性驱动封装
	protected int page;//页码
	protected int rows;//每页最大记录数
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	
}
