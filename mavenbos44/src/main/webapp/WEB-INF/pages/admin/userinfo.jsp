<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<script type="text/javascript">
	$(function(){
		$("body").css({visibility:"visible"});
		//保存按钮
		$('#save').click(function(){
			if($('#form').form("validate")){
				$('#form').submit();
			}
		});
		
		
		
		//初始化角色选择
		
		//异步请求查询所有角色
		$.post("${pageContext.request.contextPath}/role_list.action",function(data){
			//data=[{},{}]
			//循环
			$(data).each(function(){
				//拼接checkbox出来
				//原因：jquery的问题
				//jquery转换dom为jquery对象的时候，一次只能转换一个元素，如果有多个元素，那么只会转换第一个
				//var checkbox=$("<input type='checkbox' value='"+this.id+"'/>"+this.name);
				/* var checkbox1=$("<input type='checkbox' value='"+this.id+"'/>")[0];
				var checkbox2=$(this.name)[0];
				var c=checkbox1+checkbox2
				var checkbox=$(c); */
				//解决：使用span套以下
				var checkbox=$("<span><input name='roleIds' type='checkbox' value='"+this.id+"'/>"+this.name+"</span>");
				
				//添加checkbox到td中
				$("#roleSelectTd").append(checkbox);
			});
			
			
		});
		//返回列表
		$("#back").click(function(){
			location.href="${pageContext.request.contextPath}/page_admin_userlist.action";
		});
		
		
	});
	
	
	
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			<a id="back" icon="icon-undo" href="#" class="easyui-linkbutton" plain="true" >返回</a>
		</div>
	</div>
    <div region="center" style="overflow:auto;padding:5px;" border="false">
       <form id="form" action="${pageContext.request.contextPath}/user_add.action" method="post" >
           <table class="table-edit"  width="95%" align="center">
           		<tr class="title"><td colspan="4">基本信息</td></tr>
	           	<tr><td>用户名:</td><td><input type="text" name="username" id="username" class="easyui-validatebox" required="true" /></td>
					<td>口令:</td><td><input type="password" name="password" id="password" class="easyui-validatebox" required="true" validType="minLength[5]" /></td></tr>
				<tr class="title"><td colspan="4">其他信息</td></tr>
	           	<tr><td>工资:</td><td><input type="text" name="salary" id="salary" class="easyui-numberbox" /></td>
					<td>生日:</td><td><input type="text" data-options="editable:false" name="birthday" id="birthday" class="easyui-datebox" /></td></tr>
	           	<tr><td>性别:</td><td>
	           		<select name="gender" id="gender" class="easyui-combobox" style="width: 150px;">
	           			<option value="">请选择</option>
	           			<option value="男">男</option>
	           			<option value="女">女</option>
	           		</select>
	           	</td>
					<td>单位:</td><td>
					<select name="station" id="station" class="easyui-combobox" style="width: 150px;">
	           			<option value="">请选择</option>
	           			<option value="总公司">总公司</option>
	           			<option value="分公司">分公司</option>
	           			<option value="厅点">厅点</option>
	           			<option value="基地运转中心">基地运转中心</option>
	           			<option value="营业所">营业所</option>
	           		</select>
				</td></tr>
				<tr>
					<td>联系电话</td>
					<td colspan="3">
						<input type="text" name="telephone" id="telephone" class="easyui-validatebox" required="true" />
					</td>
				</tr>
	           	<tr><td>备注:</td><td colspan="3"><textarea name="remark" style="width:80%"></textarea></td></tr>
	           	<tr><td>选择角色:</td>
	           	<td colspan="3" id="roleSelectTd"></td>
	           	</tr>
           </table>
       </form>
	</div>
</body>
</html>