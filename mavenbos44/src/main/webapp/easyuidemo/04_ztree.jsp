<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 引入jQuery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
<!-- EasyUI核心 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<!-- EasyUI国际化 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<!-- EasyUI主题样式CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css"/>
<!-- EasyUI图标 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css"/>
<!-- zTree的js -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ztree/jquery.ztree.all-3.5.js"></script>
<!-- zTree样式 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ztree/zTreeStyle.css"/>

<script type="text/javascript">
	$(function(){
		//============标准json数据树
		//1.属性
		var setting = {	};//属性是默认值都
		//2.树节点的数据
		var zNodes=[
		   //每一个json对象是一个节点对象        ,name属性是节点的名字
		   {name:"搜索引擎",
			   iconOpen:"${pageContext.request.contextPath}/js/ztree/img/diy/1_open.png", 
			   iconClose:"${pageContext.request.contextPath}/js/ztree/img/diy/1_close.png",
			   children:[
			             //每一个json对象是一个子节点对象   
			             {name:"百度",icon:"${pageContext.request.contextPath}/js/ztree/img/diy/5.png",url:"http://www.baidu.com"},
			             {name:"谷歌",icon:"${pageContext.request.contextPath}/js/ztree/img/diy/3.png"}
			             ] 
		   
		   } ,        
		   {name:"门户网站" ,isParent:true}         
		            ];
		
		//3.初始化树方法
		//参数1：ul组件
		//参数2：ztree属性
		//参数3：节点的数据
		$.fn.zTree.init($("#standardDataTree"), setting, zNodes);
		
		//============简单json数据树
		//1.属性
		var setting2={
				data: {
					simpleData: {
						enable: true//默认是false，如果是true，打开了简单json数据树的加载策略
						,
						idKey: "id",
						pIdKey: "pId",
						rootPId: 0
					}
				},
				callback: {
					//点击事件
					//参数1：点击事件对象
					//参数2：点击的树的节点的id的值
					//参数3：点击的树的节点的json对象
					//参数4：点击的方式（）
					onClick: function(event, treeId, treeNode, clickFlag){
						//alert(11);
						//判断该节点是否有链接，如果有，则说明是子节点
						if(treeNode.page!=undefined&&treeNode.page!=""){
							//判断，与菜单同名的选项卡是否已经打开了，
							if($("#myTabs").tabs("exists",treeNode.name)){
								//已经打开:切换,选中
								$("#myTabs").tabs("select",treeNode.name)
							}else{
								//没有打开：创建一个新的tab出来
								$("#myTabs").tabs("add",{
									//新选项卡的属性		
										//title:'新选项卡',
										title:treeNode.name,
										closable:true,
										//href:"http://www.baidu.com"//不支持跨域访问，ajax原理
										//href:treeNode.page
										//href:"http://localhost/mavenbos44/"
										//content:"我是内容"
										content:'<div style="width:100%;height:100%;overflow:hidden;">'
											+ '<iframe src="'
											+ treeNode.page
											+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>'
										
									}
								);	
							}
						}
						
					}
				}
			};
		//2.树节点的数据
		var zNodes2=[
		     //每一个json对象是一个节点
		     {id:1,pId:0,name:'搜索引擎'},
		     {id:101,pId:1,name:'百度',page:"http://www.baidu.com"},
		     {id:102,pId:1,name:'谷歌'},
		     {id:2,pId:0,name:'门户网站'}
		             
		            ];
		
		//3.初始化树方法
		$.fn.zTree.init($("#simpleDataTree"), setting2, zNodes2);
	});

</script>

</head>
<body class="easyui-layout"> 

	<div region="north" title="标题" style="height:100px;">北</div>
	<div data-options="region:'west',title:'菜单导航'" style="width:200px;">
		<!-- 可折叠组件 -->
		<div  class="easyui-accordion" fit="true" border="false" animate="true">
			<div title="基本功能">
				<!-- ztree树加载初始化 -->
				<!-- 标准json数据的树 -->
				<ul id="standardDataTree" class="ztree"></ul>
				<!-- 简单json数据的树 -->
				<ul id="simpleDataTree" class="ztree"></ul>
			
			</div>
			<div data-options="title:'系统管理',selected:false">系统管理的内容</div>
		</div>
	</div>
	<div data-options="region:'center'" >
		<!-- 选项卡组件 -->
		<div id="myTabs" class="easyui-tabs" fit="true" border="false">
			<div title="选项卡1">选项卡1的内容</div>
			<div data-options="title:'选项卡2',closable:true,selected:true">选项卡2的内容</div>
			<div data-options="title:'选项卡3',closable:true">选项卡3的内容</div>
		</div>
	
	</div>
	<div data-options="region:'east',border:false" style=width:100px;">东</div>
	<div data-options="region:'south'" style="height:50px;">南</div>

</body>
</html>