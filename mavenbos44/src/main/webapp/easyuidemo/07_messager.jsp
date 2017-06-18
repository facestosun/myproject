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
		//----警告
		//参数1：标题
		//参数2：内容
		//参数3：图标：内置4个
		//$.messager.alert('警告','注意您男朋友掉了。。。','question');
		//-----确认
		//参数3：回调，形参：代表点击的按钮的值，如果点击了确认，那么返回true，
		/* $.messager.confirm('确认信息', '您男朋友掉了，是否要捡起来？', function(r){
			if(r){
				//点击了确认
				alert("捡起来了");
			}else{
				//没有点击确认
				alert("不要了");
			}
		});  */
		//----输入信息框
		//回调函数的参数r：用户输入的值
		/* $.messager.prompt('信息输入', '请输入男朋友的名字', function(r){
			if (r){ 
				alert(r);
			}
		});  */
		//----信息提示框
		/* $.messager.show({  	
			  title:'特大好消息',  	
			  //内容都支持html标签
			  msg:'苹果手机99元一个,<a href="http://www.itcast.cn"><font color="red" size="3">详情狂戳这里</font></a>',  	
			  //关闭时间，单位毫秒
			  timeout:2000,  	
			  //效果，默认，一般不用设置
			  //showType:'fade'  
		});   */
		//-----进度条
		//$.messager.progress(); 
		//$.messager.progress();
		$.messager.progress({
			//多少毫秒10%
			interval:1000
			
		});
		//三秒后，加载完数据，自动关闭
		window.setTimeout("$.messager.progress('close');",3000);

		
	});

</script>

</head>
<body> 
</body>
</html>