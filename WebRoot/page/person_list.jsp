<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>人员列表</title>
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
	    	function addPerson(){
                $('#dialog').dialog({  
                        title: '增加人员',
                        left:330,
                        top:100,
                        width:300,
                        height:100,  
                        cache: false,  
                        modal: true,
                        closable:false,//不显示关闭按钮
                        buttons: [{
	                        text:'提交',
	                        iconCls:'icon-ok',
	                        handler:function(){
		                        var name=$('#name').val();
		                        if(name!=null && name.length>=0){
		                                $('#addPersonForm').form('submit',{   
		                                    url:'<%=basePath%>person/addPerson.do',   
		                                    onSubmit: function(){
		                                         return $(this).form('validate');
		                                    },   
		                                    success:function(data){       
		                                    	$('#person_list').datagrid('load');
	                                            $('#dialog').dialog('close');  
		                                      }   
		                                })      
		                        }
                                                                            
	                    }},{
		                    text:'取消',
		                    handler:function(){
		                        $('#dialog').dialog('close');
		                    }
	                    }]  
		                    
		          }) 
            } 
            
	    	$('#person_list').datagrid({
	    		toolbar: [{
	    			iconCls: 'icon-add',
	    			text:'添加',
	    			handler: function(){
	    			    addPerson();
	    		   }
	    		},'-',{
	    			iconCls: 'icon-remove',
	    			text:'删除',
	    			handler: function(){alert('remove')}
	    		}]
	    	});
		});
	</script>
  </head>
  
  <body>
    <table class="easyui-datagrid" title="人员列表"  id="person_list"
            data-options="pagination:true,ownumbers:true,singleSelect:true,collapsible:true,fitColumns:true,url:'<%=basePath%>person/list.do',method:'get'">
        <thead>
            <tr>
                <th data-options="field:'id',hidden:true">person ID</th>
                <th data-options="field:'name',width:800,align:'center'">姓名</th>
            </tr>
        </thead>
    </table>
    

    <!-- 弹出对话框增加form -->
    <div id="dialog">
        <form id="addPersonForm" method="post">  
            <table align="center">
                <tr align="right">
                    <td><label for="name">姓名:</label></td>
                    <td><input class="easyui-validatebox" type="text" name="name" id="name" data-options="required:true" /></td>
                </tr>
           </table>
        </form>
    </div>
  </body>
</html>
