<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域设置</title>
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
<!-- 引入ocupload一键上传组件 -->	
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ocupload/jquery.ocupload-1.1.2.js"></script>	
	
<script type="text/javascript">
	function doAdd(){
		$('#addRegionWindow').window("open");
	}
	
	function doView(){
		alert("修改...");
	}
	
	function doDelete(){
		alert("删除...");
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}, {
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center'
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center'
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center'
	}, {
		field : 'postcode',
		title : '邮编',
		width : 120,
		align : 'center'
	}, {
		field : 'shortcode',
		title : '简码',
		width : 120,
		align : 'center'
	}, {
		field : 'citycode',
		title : '城市编码',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [30,50,100],
			pagination : true,
			toolbar : toolbar,
			//地址
			//url : "json/region.json",
			url : "${pageContext.request.contextPath}/region_listPage.action",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 添加、修改区域窗口
		$('#addRegionWindow').window({
	        title: '添加修改区域',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		//给某个可见元素绑定一个一键上传的组件:给导入按钮绑定
		var myUpload = $("#button-import").upload({
			//参数名<input type='file' name='upload'/>
	        name: 'upload',
	        //表单的action:请求地址:<form action=xxx..
	        action: '${pageContext.request.contextPath}/region_importData.action',
	        //表单的提交类型，默认就是多部分类型
	        //enctype: 'multipart/form-data',
	        //上传文件的时候是否还携带其他参数，底层：隐藏域
	        //params: {},
	        //是否自动提交表单，快关，默认是true
	        //autoSubmit: true,
	        //提交表单时触发的事件
	        onSubmit: function() {
	        	//alert("隐藏的表单提交了。。。")
	        },
	        //上传完成后出发的事件
	        //参数：响应的内容json
	        //response:隐藏的iframe中html的内容（页面上）
	        onComplete: function(response) {
	        	//alert("文件已经上传完成了。。");
	        	//提示用户是否上传成功，而且还要刷新列表
	        	//response.result//错误
	        	//之前操作的都是json对象
	        	//response：是json字符串：
	        	//将json字符串转换为json对象
	        	var responseJson=$.parseJSON(response);
	        	//判断
	        	if(responseJson.result){
	        		//提示
	        		$.messager.alert("恭喜","区域导入成功！","info");
	        	}else{
	        		//提示
	        		$.messager.alert("失败","区域导入失败！","error");
	        	}
	        	//刷新datagrid列表
	        	$("#grid").datagrid("load");//重新做人，从头开始
	        	
	        },
	        //选择文件打开后触发的事件
	        onSelect: function() {
	        	//alert("文件已经选则了要上传了。。");
	        	//判断用户上传的文件是否是excel
	        	//扩展名：xls,xlsx
	        	var regExp=/^.*\.xlsx?$/i;
	        	//判断
	        	if(regExp.test(this.filename())){
	        		//是excel,打开自动提交的表单开关
	        		this.autoSubmit=true;
	        	}else{
	        		//不是excel。阻止1表单继续提交
	        		this.autoSubmit=false;
	        		//提示
	        		$.messager.alert("提示","您上传的文件格式不是excel的","warning");
	        	}
	        	
	        }
	});
		
	});

	function doDblClickRow(){
		alert("双击表格数据...");
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form>
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" name="postcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode" class="easyui-validatebox" required="true"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>