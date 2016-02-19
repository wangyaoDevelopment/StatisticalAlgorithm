<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'statistics_unLevel.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--> 

  </head>
  
  <body>
      <div id="tbar"></div>
      <div id="main" style="height:450px"></div>
  </body>
  <script src="<%=basePath%>js/echarts/dist/echarts.min.js"></script>
  <script type="text/javascript">
	   var markPlanId = "${markPlanId}";
	   var projectPath = '<%=basePath%>';
  </script>
  <script type="text/javascript" src="<%=basePath%>js/markplan/statistics_unlevel.js"></script>
</html>
