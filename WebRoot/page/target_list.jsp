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
	<link  rel="stylesheet"  href="<%=basePath%>ext4.2/resources/css/ext-all-neptune.css"/>
    <script type="text/javascript" src="<%=basePath%>ext4.2/ext-all.js"></script>
    <script type="text/javascript">
        var projectName = '<%=basePath%>';
    </script>
    <script type="text/javascript" src="<%=basePath%>js/target/target.js"></script>
  </head>
  
  <body>
     <div id="treeGrid"></div>
  </body>
</html>
