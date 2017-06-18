package cn.itcast.bos.dto.bc;

import javax.xml.bind.annotation.XmlRootElement;

//dto
@XmlRootElement(name="customer")
public class Customer {
	private Integer id;//OID属性
	private String name;//客户名称
	private String address;//住所
	private String telephone;//联系电话
	private String decidedZoneId;//定区编号（客户和定区关联的字段）
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getDecidedZoneId() {
		return decidedZoneId;
	}
	public void setDecidedZoneId(String decidedZoneId) {
		this.decidedZoneId = decidedZoneId;
	}
	
}
