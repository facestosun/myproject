<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 引入Shiro标签 -->
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
<!-- 在页面引入，可以阻止控件拖出窗口 -->
<script
	src="${pageContext.request.contextPath }/js/easyui/outOfBounds.js" type="text/javascript"></script>
<script type="text/javascript">
	function doAdd(){
		//alert("增加...");
		$('#addStaffWindow').window("open");
	}
	
	function doView(){
		alert("查看...");
	}
	
	//作废按钮
	function doDelete(){
		//alert("删除...");
		//目标：获取选中的行中的数据的ids，请求服务器，交给服务器
		//第一步：先获取选中的行json
		//getChecked none 返回所有的行被选中的复选框。这个方法是可用的,因为1.3版本。 
		//getSelections none 返回所有选定的行,当没有记录被选中,我将返回空数组。 
		//getSelected none 返回第一个选中的行记录或null。 
		var rows=$("#grid").datagrid("getSelections");
		//第二步：判断用户是否选中了，如果没选中，不用请求服务器
		if(rows.length==0){
			//没有选一个
			$.messager.alert("提示","请至少选择一条数据","info");
			return;
		}
		//第三步：得到ids：逗号分割
		//两种方法：
		//方法1：循环拼接字符串
		//方法2：借助一个临时的数组
		var idArray=new Array();
		$(rows).each(function(){
			//this:每次遍历的json对象
			idArray.push(this.id);
		});
		//将数组转换为逗号分割的字符串
		var ids=idArray.join();
		//第四步：异步请求服务器
		$.post("${pageContext.request.contextPath}/staff_deleteBatch.action",{"ids":ids},function(data){
			//data：删除的结果:{result:true}
			if(data.result){
				//成功
				$.messager.alert("恭喜","作废取派员成功！","info");
			}else{
				//失败
				$.messager.alert("失败","作废取派员失败！","error");
			}
			//重新刷新datagrid的数据（dg数据重新加载）
			//load:抛弃到一切参数（分页+业务参数），相当于列表第一次加载情况（页面：1，条件无）
			//reload:保留所有参数，只是重新加载数据（请求服务器），相当于当前页面不动，页码不动。
			//这里第二个比较爽。。能保留页码
			$("#grid").datagrid("reload");
			//清除选择：这里两个随便选
			//clearSelections none 清除所有的选择。 
			//clearChecked none 清除所有选中的行。这个方法是可用的,因为版本1.3.2。 
			$("#grid").datagrid("clearSelections");
			
			
		});
		
	}
	
	function doRestore(){
		alert("将取派员还原...");
	}
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, 
	/* <shiro:hasRole name="weihu"> */
	<shiro:hasPermission name="staff:add">
	{
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : doDelete
	},
	</shiro:hasPermission>
	/* </shiro:hasRole> */
	{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : doRestore
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,//复选
	},{
		field : 'name',
		title : '姓名',
		width : 120,
		align : 'center'
	}, {
		field : 'telephone',
		title : '手机号',
		width : 120,
		align : 'center'
	}, {
		field : 'haspda',
		title : '是否有PDA',
		width : 120,
		align : 'center',
		//格式化表格中显示的值
		//参数1：字段对应json对象获取数据值
		//参数2：当前行的一行的json对象
		//参数3：当前行的索引，从0开始
		formatter : function(data,row, index){
			if(data=="1"){
				return "有";
			}else{
				return "无";
			}
		}
	}, {
		field : 'deltag',
		title : '是否作废',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="0"){
				return "正常使用"
			}else{
				return "已作废";
			}
		}
	}, {
		field : 'standard',
		title : '取派标准',
		width : 120,
		align : 'center'
	}, {
		field : 'station',
		title : '所谓单位',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 取派员信息表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			//每页最大记录数
			pageList: [2,20,200],
			//分页工具条：打开了分页的功能，数据格式也变为分页的数据格式
			pagination : true,
			toolbar : toolbar,
			//请求地址，返回指定格式的json数据
			//url : "json/staff.json",
			url : "${pageContext.request.contextPath}/staff_listPage.action",
			idField : 'id',
			columns : columns,
			//双击事件
			onDblClickRow : doDblClickRow
		});
		
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        title: '对收派员进行添加或者修改',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        //关闭事件
	        onClose:function(){
	        	//alert(11);
	        	//目标：清除表单数据
	        	//三种方式：
	        	//1)form表单的reset方法
	        	$("#staffForm")[0].reset();
				//2)easyui：form的组件的方法
				//$("#staffForm").form("reset");
				//2)easyui：form的组件的方法
				//$("#staffForm").form("clear");
				//有区别：重置（只会清除可见的元素的值）和清除（不但可见的，还有隐藏域的）
				//如果使用重置，那么必须手动清除隐藏域的值
				$("input[name='id']").val("");
	
	        }
	    });
		
		//保存按钮
		$("#save").click(function(){
			//必须表单校验全部通过才能提交表单
			//判断：是否校验全部通过
			if($("#staffForm").form("validate")){
				//提交表单
				$("#staffForm").submit();
			}
			
			
		});
		
	});

	//双击表格的事件的函数
	//参数1：双击的行的索引
	//参数2：双击的行的一行数据json对象
	function doDblClickRow(rowIndex, rowData){
		//alert("双击表格数据...");
		//目标：弹出window（和添加一样），回显数据
		
		//回显数据：填充表单
		//两种方式：
		//1）手工方式，一个一个组件手动赋值xxx.val(rowData.name)
		//2)自动填充表单
		//使用easyui的form组件自动填充
		//参数2：填充的表单数据：key，必须和表单的name的值一样
		//$("#staffForm").form("load",formJson);
		$("#staffForm").form("load",rowData);
		
		//弹出修改窗口
		$('#addStaffWindow').window("open");
		
		
	}
	
	//自定义validatebox的校验器
	$.extend($.fn.validatebox.defaults.rules, { 
		//校验器的名字
		telephone: { 
			//校验规则
			//参数1：表单输入的值
			//参数2：校验器的参数，数组，
			validator: function(value, param){ 
				//编写校验规则代码
				//弄正则的对象
				var regExp=/^1[34578]\d{9}$/;
				//校验兵返回
				return regExp.test(value); //返回true或者不是true

			}, 
			//校验错误的提示信息
			message: '手机号码不正确' 

		} 

	}); 

</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
		<!-- 认证 -->
		<%-- <shiro:authenticated> --%>
		<!-- 必须不认证 -->
		<%-- <shiro:notAuthenticated> --%>
		<!-- 授权 -->
		<%-- <shiro:hasRole name="weihu11"> --%>
		<shiro:lacksRole name="weihu">
			<input/>
		</shiro:lacksRole>
		
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="staffForm" action="${pageContext.request.contextPath }/staff_add.action" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<!-- TODO 这里完善收派员添加 table -->
					<!-- <tr>
						<td>取派员编号</td>
						<td><input type="text" name="id" class="easyui-validatebox" required="true"/></td>
					</tr> -->
					<tr>
						<td>姓名</td>
						<td><input type="text" name="name" class="easyui-validatebox" required="true" validType="length[3,7]"/>
						<!-- 隐藏域 -->
						<input type="hidden" name="id" />
						</td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" class="easyui-validatebox" required="true" data-options="validType:'telephone'"/></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
							<input type="text" name="standard" class="easyui-validatebox" required="true"/>  
						</td>
					</tr>
					</table>
					
			</form>
		</div>
	</div>
</body>
</html>	