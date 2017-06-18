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

</head>
<body class="easyui-layout"> 

	<div region="north" title="标题" style="height:100px;">北</div>
	<div data-options="region:'west',title:'菜单导航'" style="width:200px;">
		<!-- 可折叠组件 -->
		<div  class="easyui-accordion" fit="true" border="false" animate="true">
			<div title="基本功能">基本功能的内容</div>
			<div data-options="title:'系统管理',selected:false">系统管理的内容</div>
		</div>
	</div>
	<div data-options="region:'center'" >中</div>
	<div data-options="region:'east',border:false" style=width:100px;">东</div>
	<div data-options="region:'south'" style="height:50px;">南</div>

</body>
</html>