package cn.itcast.bos.domain.qp;
// Generated 2017-3-12 17:03:03 by Hibernate Tools 3.2.2.GA


import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * WorkOrderManage generated by hbm2java
 */
@Entity
@Table(name="T_QP_WORKORDERMANAGE"
    ,schema="BOS44"
)
@Indexed//标识该类型可以进入索引库
@Analyzer(impl=IKAnalyzer.class)//分词器，如果不配置，默认是英文分词器,这里使用ik分词器
public class WorkOrderManage  implements java.io.Serializable {


     private String id;
     private String arrivecity;
     private String product;
     private BigDecimal num;
     private BigDecimal weight;
     private String floadreqr;
     private String prodtimelimit;
     private String prodtype;
     private String sendername;
     private String senderphone;
     private String senderaddr;
     private String receivername;
     private String receiverphone;
     private String receiveraddr;
     private BigDecimal feeitemnum;
     private BigDecimal actlweit;
     private String vol;
     private String managercheck;
     private Date updatetime;

    public WorkOrderManage() {
    }

    public WorkOrderManage(String arrivecity, String product, BigDecimal num, BigDecimal weight, String floadreqr, String prodtimelimit, String prodtype, String sendername, String senderphone, String senderaddr, String receivername, String receiverphone, String receiveraddr, BigDecimal feeitemnum, BigDecimal actlweit, String vol, String managercheck, Date updatetime) {
       this.arrivecity = arrivecity;
       this.product = product;
       this.num = num;
       this.weight = weight;
       this.floadreqr = floadreqr;
       this.prodtimelimit = prodtimelimit;
       this.prodtype = prodtype;
       this.sendername = sendername;
       this.senderphone = senderphone;
       this.senderaddr = senderaddr;
       this.receivername = receivername;
       this.receiverphone = receiverphone;
       this.receiveraddr = receiveraddr;
       this.feeitemnum = feeitemnum;
       this.actlweit = actlweit;
       this.vol = vol;
       this.managercheck = managercheck;
       this.updatetime = updatetime;
    }
   
     //@GenericGenerator(name="generator", strategy="uuid")
     @Id 
     //@GeneratedValue(generator="generator")
    
     @DocumentId//标识该属性主键
    @Column(name="ID", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Field//要索引该属性
    @Column(name="ARRIVECITY", length=20)
    public String getArrivecity() {
        return this.arrivecity;
    }
    
    public void setArrivecity(String arrivecity) {
        this.arrivecity = arrivecity;
    }
    
    @Field//要索引该属性
    @Column(name="PRODUCT", length=20)
    public String getProduct() {
        return this.product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    @Column(name="NUM", precision=22, scale=0)
    public BigDecimal getNum() {
        return this.num;
    }
    
    public void setNum(BigDecimal num) {
        this.num = num;
    }
    
    @Column(name="WEIGHT", precision=22, scale=0)
    public BigDecimal getWeight() {
        return this.weight;
    }
    
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    
    @Column(name="FLOADREQR")
    public String getFloadreqr() {
        return this.floadreqr;
    }
    
    public void setFloadreqr(String floadreqr) {
        this.floadreqr = floadreqr;
    }
    
    @Column(name="PRODTIMELIMIT", length=40)
    public String getProdtimelimit() {
        return this.prodtimelimit;
    }
    
    public void setProdtimelimit(String prodtimelimit) {
        this.prodtimelimit = prodtimelimit;
    }
    
    @Column(name="PRODTYPE", length=40)
    public String getProdtype() {
        return this.prodtype;
    }
    
    public void setProdtype(String prodtype) {
        this.prodtype = prodtype;
    }
    
    @Column(name="SENDERNAME", length=20)
    public String getSendername() {
        return this.sendername;
    }
    
    public void setSendername(String sendername) {
        this.sendername = sendername;
    }
    
    @Column(name="SENDERPHONE", length=20)
    public String getSenderphone() {
        return this.senderphone;
    }
    
    public void setSenderphone(String senderphone) {
        this.senderphone = senderphone;
    }
    
    @Column(name="SENDERADDR", length=200)
    public String getSenderaddr() {
        return this.senderaddr;
    }
    
    public void setSenderaddr(String senderaddr) {
        this.senderaddr = senderaddr;
    }
    
    @Column(name="RECEIVERNAME", length=20)
    public String getReceivername() {
        return this.receivername;
    }
    
    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }
    
    @Column(name="RECEIVERPHONE", length=20)
    public String getReceiverphone() {
        return this.receiverphone;
    }
    
    public void setReceiverphone(String receiverphone) {
        this.receiverphone = receiverphone;
    }
    
    @Column(name="RECEIVERADDR", length=200)
    public String getReceiveraddr() {
        return this.receiveraddr;
    }
    
    public void setReceiveraddr(String receiveraddr) {
        this.receiveraddr = receiveraddr;
    }
    
    @Column(name="FEEITEMNUM", precision=22, scale=0)
    public BigDecimal getFeeitemnum() {
        return this.feeitemnum;
    }
    
    public void setFeeitemnum(BigDecimal feeitemnum) {
        this.feeitemnum = feeitemnum;
    }
    
    @Column(name="ACTLWEIT", precision=22, scale=0)
    public BigDecimal getActlweit() {
        return this.actlweit;
    }
    
    public void setActlweit(BigDecimal actlweit) {
        this.actlweit = actlweit;
    }
    
    @Column(name="VOL", length=20)
    public String getVol() {
        return this.vol;
    }
    
    public void setVol(String vol) {
        this.vol = vol;
    }
    
    @Column(name="MANAGERCHECK", length=1)
    public String getManagercheck() {
        return this.managercheck;
    }
    
    public void setManagercheck(String managercheck) {
        this.managercheck = managercheck;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="UPDATETIME", length=7)
    public Date getUpdatetime() {
        return this.updatetime;
    }
    
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }




}


