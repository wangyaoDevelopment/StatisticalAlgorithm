function init() {
	Ext.MessageBox.alert("ExtJS", "Hello ExtJS");
}
Ext.onReady(function(){
	var store = Ext.create('Ext.data.TreeStore', {
	    root: {
	    	text: 'rr',
	        expanded: true,
	        leaf: false,
	        children: [{ 
	        	          id: 'targetManagement',
	                      text: "指标管理", 
	                      leaf: true 
	                 },{  
	                 	  id: 'markManagement',
	                      text: "打分管理", 
	                      leaf: true 
	                  }]
	    }
	});
	
	var menuTree = Ext.create('Ext.tree.Panel', {
	    width: 200,
	    height: 150,
	    store: store,
	    rootVisible: false,
	    margins: '0',
	    listeners: {
            //点击行触发事件
            itemclick: function (record,node) {
            	var tabUrl;
            	if(node.data.id=='targetManagement'){
            	   tabUrl = 'target_list.jsp';
            	}else if(node.data.id=='markManagement'){
            	   tabUrl = 'markplan.jsp';
            	}
                openTabPanel(node,tabUrl);
            }
        }
	});
	
	var tabPanel =  Ext.create('Ext.TabPanel',{
		id: 'mainTabPanel',
        activeTab: 0,
        enableTabScroll: true,
        items: [{
            id: 'homePage',
            title: '我的主页',
            autoLoad:{url:'z.jsp',scripts:true,nocache:true}
        }]
	});
	
	function openTabPanel(node,tabUrl){
        var n = tabPanel.getComponent('tab-'+node.data.id);
        if (!n) { // 判断是否已经打开该面板
            n = tabPanel.add({
            	    id: 'tab-'+node.data.id,
	                title: node.data.text,
	                layout: 'fit',
	                closable: true,
	                autoScroll: true,
	                html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+tabUrl+'"></iframe>'
	                //autoLoad: {url:tabUrl,scripts:true,nocache:true}
            });
        }
        tabPanel.setActiveTab(n);
	}

	var viewPort = Ext.create('Ext.Viewport',{
		 layout: 'border',
	     items: [{
	        title: '菜单',
	        region:'west',
	        xtype: 'panel',
	        width: 200,
	        collapsible: true,
	        id: 'menu',
	        layout: 'fit',
	        items:[menuTree]
	    },{
	        region: 'center',
	        xtype: 'panel',
	        layout: 'fit',
	        items: [tabPanel]
	    }],
	    renderTo: Ext.getBody()
	});
	
});

