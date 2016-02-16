<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>人员添加</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="jquery-easyui-1.4.4/demo/demo.css" type="text/css"></link>
	<link rel="stylesheet" href="jquery-easyui-1.4.4/themes/icon.css" type="text/css"></link>
	<link rel="stylesheet" href="jquery-easyui-1.4.4/themes/default/easyui.css" type="text/css"></link>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>	
    <style type="text/css">
        #te{
            height:20px;
        }
    </style>
    <script type="text/javascript">
	    $(function(){
		    function save(){
                alert('--');
			}
			
	        $('#btn').bind('click', function(){
	            alert('easyui');
	        });
	    });
    </script>
  </head>
  
  <body>
     <div id="te"></div>
     <form id="ff" method="post">
	    <div>
	        <label for="name">姓名:</label>
	        <input class="easyui-validatebox" type="text" name="name" data-options="required:true" />
	        <a id="btn" href="javascript:void(0)" onclick="save()"  class="easyui-linkbutton" data-options="iconCls:'icon-add'">保存</a>
	    </div>
	 </form>
  </body>
</html>
