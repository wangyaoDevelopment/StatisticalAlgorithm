<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>指标管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="jquery-easyui-1.4.4/demo/demo.css" type="text/css"></link>
	<link rel="stylesheet" href="jquery-easyui-1.4.4/themes/icon.css" type="text/css"></link>
	<link rel="stylesheet" href="jquery-easyui-1.4.4/themes/default/easyui.css" type="text/css"></link>
	<style type="text/css">
	      body{
	          padding:0px;
	      }
	</style>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>	
    <script type="text/javascript">
        $(document).ready(function(){
        	 $('#tt').tree({
     	        url:'<%=basePath%>target/tree.do'
     	     });

        });
    </script>

</head>
<body>
    <ul id="tt">
    </ul>
</body>
</html>
