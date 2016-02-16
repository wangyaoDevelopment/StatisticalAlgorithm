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
    
   $(function(){ 
     InitTreeData();
    }) ;
   
     function InitTreeData()
     {
      $('#tree').tree({
          url:'<%=basePath%>target/list.do',
          checkbox:true,
          onClick:function(node){
            alert(node.text);
          },
          onContextMenu: function(e, node){  
                        e.preventDefault();  
                        $('#tree').tree('select', node.target);  
                        $('#mm').menu('show', {  
                            left: e.pageX,  
                            top: e.pageY  
                        });  
                    }  
        });
     }
     function  remove()
     {
       
         var nodes = $('#tree').tree('getChecked');
         var ids = '';
            for(var i=0; i<nodes.length; i++){
                if (ids != '') ids += ',';
                ids += nodes[i].id;
                 //$('#tree').tree('remove',nodes[i].target);
            }
         $.post("demo01.ashx",{"ids":ids,"type":"del"},function(data){
            InitTreeData();
         });
      
     }
     function update()
     {
          var node = $('#tree').tree('getSelected');
            if (node){
                node.text = '修改';  //-->txt-->DB
                node.iconCls = 'icon-save'; //-->sel-->DB
                $('#tree').tree('update', node);
            }
     }
     function  append()
     {
        
        var node = $('#tree').tree('getSelected');
        
            $('#tree').tree('append',{
                parent: (node?node.target:null),
                data:
                [
                    {
                        text:'new1',//  -->txt-->DB
                        id:'1',
                        checked:true
                    } 
                ]
            });
         
     }
    </script>

</head>
<body>
    <ul id="tree">
    </ul>
    <div id="mm" class="easyui-menu" style="width: 120px;">
        <div onclick="append()" iconcls="icon-add">
            添加节点</div>
        <div onclick="remove()" iconcls="icon-remove">
            删除节点</div>
        
        <div onclick="update()" iconcls="icon-edit">修改节点</div>
    </div></body>
</html>
