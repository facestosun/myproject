<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作单快速录入</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
	//全局的变量：临时存放正在编辑的行的索引。初始是undefined
	var editIndex ;
	
	//新增一行
	function doAdd(){
		
		//如果正在编辑了一行
		if(editIndex != undefined){
			//结束编辑，如果编辑完成，会将索引重置
			//原因：结束编辑方法隐藏的动作：表单校验，自动执行form("validate")
			$("#grid").datagrid('endEdit',editIndex);
		}
		
		//判断当前是否有正在编辑的行
		if(editIndex==undefined){
			//alert("快速添加电子单...");
			//插入一行
			$("#grid").datagrid('insertRow',{
				//在哪一行插入
				index : 0,
				//插入的一行的数据
				row : {}
				//row : {"product":"电脑"}//有默认值
			});
			//打开了编辑
			$("#grid").datagrid('beginEdit',0);
			//正在编辑的行的索引赋值
			editIndex = 0;
		}
	}
	
	function doSave(){
		//先校验，校验不通过，则不能保存
		$("#grid").datagrid('endEdit',editIndex );
	}
	
	function doCancel(){
		if(editIndex!=undefined){
			$("#grid").datagrid('cancelEdit',editIndex);
			if($('#grid').datagrid('getRows')[editIndex].id == undefined){
				$("#grid").datagrid('deleteRow',editIndex);
			}
			editIndex = undefined;
		}
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-add',	
		text : '新增一行',
		iconCls : 'icon-edit',
		handler : doAdd
	}, {
		id : 'button-cancel',
		text : '取消编辑',
		iconCls : 'icon-cancel',
		handler : doCancel
	}, {
		id : 'button-save',
		text : '保存',
		iconCls : 'icon-save',
		handler : doSave
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		title : '工作单号',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'arrivecity',
		title : '到达地',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	},{
		field : 'product',
		title : '产品',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'num',
		title : '件数',
		width : 120,
		align : 'center',
		editor :{
			type : 'numberbox',
			options : {
				required: true
			}
		}
	}, {
		field : 'weight',
		title : '重量',
		width : 120,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}, {
		field : 'floadreqr',
		title : '配载要求',
		width : 220,
		align : 'center',
		editor :{
			type : 'validatebox',
			options : {
				required: true
			}
		}
	}] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList: [30,50,100],
			pagination : true,
			toolbar : toolbar,
			url :  "${pageContext.request.contextPath}/workOrderManage_listPage.action",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow,
			//结束编辑时触发的事件
			onAfterEdit : function(rowIndex, rowData, changes){
				//浏览器控制台日志调试信息，
				//为什么要用？如果alert，不能打印对象。
				//alert(rowData);
				//console.info(rowData);
				//console.log(rowData);
				
				//只能异步提交请求
				$.post("${pageContext.request.contextPath}/workOrderManage_add.action",rowData,function(data){
					//data：保存的结果{"result":true}
					if(data.result){
						//保存成功
						$.messager.show({  	
						  title:'恭喜',  	
						  msg:'工作订单保存成功',  	
						  timeout:2000,  	
						});  
						
						//刷新页面
						//$("#grid").datagrid("reload");

					}else{
						//保存失败
						$.messager.alert("错误","工作订单保存失败","error");
					}
					
					
					
					
				});
				
				//将正在编辑的索引置为undefined
				editIndex = undefined;
			}
		});
	});

	function doDblClickRow(rowIndex, rowData){
		alert("双击表格数据...");
		console.info(rowIndex);
		//打开编辑
		$('#grid').datagrid('beginEdit',rowIndex);
		editIndex = rowIndex;
	}
	
	//检索查询
	//参数1：用户输入的值
	//参数2：选择菜单的name属性的值
	function doSearch(value,name){
		//alert(value+"___"+name);
		
		//获取两个东西：
		//一个是用户输入的值，一个是选择那个词条（字段）
		
		//进行查询：组合条件分页查询
		//将来调用datagrid 的load方法
		//$("#grid").datagrid("load",缓存的参数);
		$("#grid").datagrid("load",{
			//缓存的参数的名字随便，后台要拿
			conditionName:name,
			conditionValue:value
		});
	}
	
</script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north">
		<input class="easyui-searchbox" data-options="searcher:doSearch,prompt:'请输入关键字',menu:'#mm'"/>
		<div id="mm">
			<div  data-options="name:'arrivecity',iconCls:'icon-ok'">到达地</div>
			<div data-options="name:'product',iconCls:'icon-ok'">产品名称</div>
		</div>
	</div>
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
</body>
</html>