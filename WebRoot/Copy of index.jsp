<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<jsp:include page="header.jsp" />
		<title>算法统计首页</title>
		<script type="text/javascript">
	        
			function addTab(title, url){  
				url = "<%=basePath%>" + url; 
			    if ($('#tb').tabs('exists', title)){    
			        $('#tb').tabs('select', title);    
			    } else { 
			        var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:94%;"></iframe>';    
			        $('#tb').tabs('add',{    
			            title:title,    
			            content:content,    
			            closable:true
			        });    
			    }    
			}    
	    </script>
	</head>

	<body class="easyui-layout">
		<div data-options="region:'west',title:'菜单',split:true"
			style="width: 150px;">
			<ul id="tt" class="easyui-tree">
				<li>
					<span>统计算法</span>
					<ul>
						<li>
							<span><a href="javascript:void(0)" onclick="addTab('人员管理','person/toList.do')">人员管理</a> </span>
						</li>
						<li>
							<span><a href="javascript:void(0)" onclick="addTab('指标管理','target/toList.do')">指标管理</a> </span>
						</li>
						<li>
							<span><a href="#">统计管理</a> </span>
						</li>
					</ul>
				</li>
			</ul>
		</div>
		
		<div data-options="region:'center',title:''" >
			<div id="tb" class="easyui-tabs">
				<div title="首頁"> 
				       <iframe scrolling="auto" frameborder="0"  src="z.jsp" style="width:100%;height:94%;"></iframe>
                </div> 
			</div>
		</div>
	</body>
</html>