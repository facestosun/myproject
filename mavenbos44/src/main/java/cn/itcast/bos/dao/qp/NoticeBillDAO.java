package cn.itcast.bos.dao.qp;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.qp.NoticeBill;

//通知单dao
public interface NoticeBillDAO extends JpaRepository<NoticeBill, String>{

}
