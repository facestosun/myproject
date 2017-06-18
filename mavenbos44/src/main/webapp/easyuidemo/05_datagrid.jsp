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
<script type="text/javascript">
	$(function(){
		//初始化datagrid
		$("#grid").datagrid({
			//数据加载请求地址
			url:'data-page.json',
			columns:[[ 
			{field:'id',title:'编号',width:100,checkbox:true}, 
			{field:'name',title:'名字',width:300}, 
			{field:'price',title:'价格',width:200,align:'right'} 
			]] ,
			rownumbers:true,
			fitColumns:true,
			toolbar: [{  		
			  iconCls: 'icon-edit',  	
			  text:'修改',
			  //点击事件
			  handler: function(){alert('edit')}  	
			  },'-',{  		
			  iconCls: 'icon-help',  		
			  handler: function(){alert('help')}  	
			  }]  ,
			  pagination:true

			
		});
		
		
	});

</script>

</head>
<body> 
	<h3>3.9.1.	方式一：DataGrid加载HTML静态数据（了解）</h3>
	<table class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name'">名称</th>
				<th data-options="field:'price'">价格</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>电视机</td>
				<td>998</td>
			</tr>
			<tr>
				<td>2</td>
				<td>电冰箱</td>
				<td>9999</td>
			</tr>
		</tbody>
	</table>
	
	<h3>3.9.2.	方式二：加载Json远程数据</h3>
	<table class="easyui-datagrid" data-options="url:'data-page.json',rownumbers:true,fitColumns:true,
	toolbar:'#tb' ,pagination:true
	">
		<thead>
			<tr>
				<th data-options="field:'id',checkbox:true,width:100">编号</th>
				<th data-options="field:'name',width:300">名称</th>
				<th data-options="field:'price',width:200">价格</th>	
			</tr>
		</thead>
	</table>
	
	<div id="tb">
		<a href="javascript:void(0);"  >添加</a>
		<a data-options="iconCls:'icon-add'" href="javascript:void(0);" class="easyui-linkbutton" >修改</a>
		<a data-options="plain:true" href="javascript:void(0);" class="easyui-linkbutton" >删除</a>
		<a data-options="plain:true,iconCls:'icon-help'" href="javascript:void(0);" class="easyui-linkbutton" >帮助</a>
	</div>
	<h3>3.9.3.	方式三：使用JavaScript来加载数据</h3>
	<table id="grid"></table>
</body>
</html>