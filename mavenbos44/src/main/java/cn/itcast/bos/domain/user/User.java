package cn.itcast.bos.domain.user;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import cn.itcast.bos.domain.auth.Role;

//用户的实体类po类
@Entity
@Table(name="t_user",schema="bos44")
@NamedQueries({@NamedQuery(name="User.findPasswordByUsername",query="select password from User where username =?")})//命名查询语句hql
public class User implements Serializable{
	
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -6231803899227321576L;
	@Id
	@GeneratedValue(generator="uuidGenerator")
	//定义hibernate的主键策略生成器
	@GenericGenerator(name="uuidGenerator",strategy="uuid")
	@Column(length=32)
	private String id;//OID
	@Column(length=20,nullable=false)
	private String username;
	@Column(length=32,nullable=false,name="password")
	private String password;
	@Column(columnDefinition="NUMBER")//强行指定该属性对应数据库字段的数据类型-了解并不推荐
	private Double salary;
	//告诉jpa，属性可接受的日期的类型
	@Temporal(TemporalType.DATE)
	private Date birthday;
	@Column(length=10)
	private String gender;
	@Column(length=40)
	private String station;
	@Column(length=11)
	private String telephone;
	@Column(length=255)
	private String remark;
	//为什么角色代码复制到这里会报错？
	//原因：jpa注解要么都写在声明上，要么都写在getter上，不能混着来
	
	//用户关联角色
	/*	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<Role> roles = new HashSet<Role>(0);
	
	//删除关联表维护代码
      public Set<Role> getRoles() {
        return this.roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }*/
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	
	//关键在这里：json插件将date类型转为字符串
	//解决方式两种：
	//1.再写一个getter方法
	@Transient
	public String getBirthdayJson() {
		//手动转换
		DateFormat df =new SimpleDateFormat("yyyy-MM-dd");
		if(birthday!=null){
			return df.format(birthday);
		}else{
			return null;
		}
	}
	
	//2。设置json插件转换日期的格式
	@JSON(format="yyyy-MM-dd")//序列化反序列化的日期格式
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", salary=" + salary + ", birthday=" + birthday + ", gender=" + gender + ", station=" + station + ", telephone=" + telephone + ", remark=" + remark
				+ "]";
	}
	
	
	

}
