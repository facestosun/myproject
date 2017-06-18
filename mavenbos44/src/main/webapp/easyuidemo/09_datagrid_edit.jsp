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
			{field:'name',title:'名字',width:300,
				//编辑的组件
				editor:{
					//可编辑的组件的类型，随便，必须是表单元素，比如input
					type:"validatebox",
					//组件的属性(可选)
					options:{
						required:true
					}
				}
					
			}, 
			{field:'price',title:'价格',width:200,align:'right',
				//编辑的组件
				editor:{
					//可编辑的组件的类型，随便，必须是表单元素，比如input
					type:"datebox",
					//组件的属性(可选)
					options:{
						required:true
					}
				}	
			
			} 
			]] ,
			rownumbers:true,
			fitColumns:true,
			toolbar: [{  		
			  iconCls: 'icon-edit',  	
			  text:'修改',
			  //点击事件
			  handler: function(){alert('edit')}  	
			  },'-'
			  ,{  		
			  iconCls: 'icon-help',  		
			  handler: function(){alert('help')}  	
			  }
			  ,{  		
				  text:'编辑选中的行', 		
			  handler: function(){
				  //获取选中的行
				  var row=$("#grid").datagrid("getSelected");
				  //获取选中行的索引
				  var index= $("#grid").datagrid("getRowIndex",row);
				  //打开编辑某行,参数是索引
				  $("#grid").datagrid("beginEdit",index);
			  }  	
			  }
			  ,{  		
				  text:'结束编辑',  		
			  handler: function(){
				  
				//获取选中的行
				  var row=$("#grid").datagrid("getSelected");
				  //获取选中行的索引
				  var index= $("#grid").datagrid("getRowIndex",row);
				  //打开编辑某行,参数是索引
				  $("#grid").datagrid("endEdit",index);
				  
			  }  	
			  }
			  ,{  		
				  text:'取消编辑',  		
			  handler: function(){
				  
				//获取选中的行
				  var row=$("#grid").datagrid("getSelected");
				  //获取选中行的索引
				  var index= $("#grid").datagrid("getRowIndex",row);
				  //打开编辑某行,参数是索引
				  $("#grid").datagrid("cancelEdit",index);
				  
			  }  	
			  }
			  
			  
			  
			  ]  ,
			  pagination:true,
			  //参数1：编辑的行的索引，从0开始
			  //参数2：编辑的行的一行数据（一定一行数据，不管是否有改变）：json格式
			  //参数3：编辑的行的改变的一行的数据（只有改变的数据才有）
			  onAfterEdit:function(rowIndex, rowData, changes){
				  
				  alert("rowData:"+rowData.name+",changes:"+changes.name);
				  //保存到数据库：使用rowData
			  }

			
		});
		
		
	});

</script>

</head>
<body> 
	<h3>3.9.3.	方式三：使用JavaScript来加载数据</h3>
	<table id="grid"></table>
</body>
</html>