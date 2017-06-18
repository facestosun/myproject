package cn.itcast.bos.dao.impl.qp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.itcast.bos.domain.qp.WorkOrderManage;

//工作订单dao实现
//约定：
//1）实现类必须能被扫描到：<jpa:repositories base-package="cn.itcast.bos.dao"/>
//2)实现类的类名必须叫接口类名+Impl（也可以改repository-impl-postfix="Impl"实现类后缀）
public class WorkOrderManageDAOImpl {
	
	//注入EntityManager entityManager;
	@PersistenceContext//从spring容器中获取实体管理对象
	private EntityManager entityManager;//受spring管理的
	
	//全文检索工作单
	public Page<WorkOrderManage> searchByLucene(Pageable pageable, String conditionName, String conditionValue){
		//编写全文检索的逻辑代码（会改即可）
		//分析：将关键字和值交给lucence（Hibernatesearch的api），绑定数据库的表查询，
		//Hibernate search会“自动”先从全文搜索中找id，然后通过id查询数据库
		//-----lucence的query对象
		//---第一类搜索：一类长词分词模糊匹配
		//参数1：lucence版本
		//参数2：索引的字段的名字（查询的字段的名字）词条的名字
		//参数3：分词器
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, conditionName, new IKAnalyzer());
		//查询的关键字（解析器会自动分词后再查询）
		Query fenciQuery=null;
		try {
			//具体分词查询条件
			fenciQuery = queryParser.parse(conditionValue);
		} catch (ParseException e) {
			throw new RuntimeException("不能解析分词这个关键字: " + conditionValue, e);
		}
		////---第二类搜索：短词直接匹配
		
		//参数：直接字段名和值
		Query duanciQuery=new WildcardQuery(new Term(conditionName,"*"+conditionValue+"*"));
		
		//将两个query结合起来---生成lunce的最终查询对象
		BooleanQuery query = new BooleanQuery();
		//参数1：查询规则对象，参数2：规则是否参与
		// Occur.MUST:该规则，必须参与检索，结果必须满足该规则 and
		//Occur.MUST_NOT:检索的结果必须不满足该规则，排除的性质 not and
		//Occur.SHOULD可以参与检索规则，相当于or
		query.add(fenciQuery, Occur.SHOULD);//must:相当于and，SHOULD相当于or
		query.add(duanciQuery, Occur.SHOULD);
		
		
		//-----------------------------------------根据EntityManager来创建一个全文实体管理对象
		//jpa整合HibernateSearch
		 FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		 //Hibernate整合HibernateSearch
//		 FullTextSession fullTextSession= org.hibernate.search.Search.getFullTextSession(session);
		 
		 //++++返回的是全文检索的查询对象---------最终要要Hibernate search。。。。---
		 //参数1。lucence Query对象（查询规则）
		 //参数2：实体类,带有索引注解的实体类（结果会自动封装回来）
		 FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, WorkOrderManage.class);
		//-----上述代码，Hibernate search结合了lucence和jpa的查询数据库的查询，查询内部，都hs封装
		 //---封装结果集page对象
		 int total = fullTextQuery.getResultSize();//列表的总的数量
		 //现在分页数据，是Hibernate search来分调用jpa
		 fullTextQuery.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());//起始索引（页码-1）*pageSize
		 fullTextQuery.setMaxResults(pageable.getPageSize());//每页最大记录数
		 List<WorkOrderManage> content = fullTextQuery.getResultList();//数据列表
		 
		 //结果—手动封装
		 //参数1：当前页记录列表
		 //参数2：分页请求对象
		 //参数3：总记录数
		 Page<WorkOrderManage> page = new PageImpl<WorkOrderManage>(content, pageable, total);
		
		return page;

		
		
		
		
	}
	
}
