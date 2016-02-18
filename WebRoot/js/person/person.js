Ext.onReady(function(){
	
     Ext.define("MyApp.model.Person",{
	     extend: "Ext.data.Model",
         fields: [
	        { name: 'id', type: 'string' },
	        { name: 'name', type: 'string' }
          ]
	});
	
	var personStore = Ext.create("Ext.data.Store", {
	    model: "MyApp.model.Person",
	    autoLoad: true,
	    pageSize: 20,
	    proxy: {
	        type: "ajax",
	        url: projectName+"person/list.do",
	        reader: {  
	            type: "json",  
	            root: "data",  
	            totalProperty: "total"  
	        }  
	    }
	});
	
	var pagebar = Ext.create("Ext.toolbar.Paging", {  
	    store: personStore,  
	    displayInfo: true,  
	    displayMsg: "显示{0}-{1}条,共计{2}条",  
	    emptyMsg: "没有数据"  
	  
	    }); 
	    
		var personListGrid = Ext.create("Ext.grid.Panel", {
	    xtype: "grid",
	    store: personStore,
	    width: "100%",
	    height: "100%",
	    //margin: 30,
	    columnLines: true,
	    renderTo: Ext.getBody(),
	    simpleSelect: true,
	    tbar: [{  
	        xtype: "button",  
	        frame: true,  
	        text: "添加",  
	        scale: "small",  
	        tooltip: "添加人员",  
	        handler: function(){
				addPersonWin.setTitle("添加人员");
				personForm.form.reset();
				Ext.getCmp("btnAdd").show();
				Ext.getCmp("btnEdit").hide();
				addPersonWin.show();
	        }  
	    },{  
	        xtype: "button",  
	        frame: true,  
	        text: "编辑",  
	        scale: "small",  
	        tooltip: "编辑",  
	        handler: function(){
	             var rows = personListGrid.getSelectionModel().getSelection();
				 if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				 }
				 addPersonWin.setTitle("编辑人员");
				 personForm.form.setValues(rows[0].data);
				 Ext.getCmp("btnAdd").hide();
				 Ext.getCmp("btnEdit").show();
				 addPersonWin.show();
	        }  
	    },{  
	        xtype: "button",  
	        frame: true,  
	        text: "删除",  
	        scale: "small",  
	        tooltip: "删除",  
	        handler: function(){
	            deletePerson();
	        }  
	    },{  
	        xtype: "button",  
	        frame: true,  
	        text: "刷新",  
	        scale: "small",  
	        tooltip: "刷新",  
	        handler: function(){
	            personStore.load();
	        }  
	    }],  
	    //分页功能   
	    bbar: pagebar, 
	    selModel: {
	        mode: "SINGLE"
	    },
	    selType: "checkboxmodel",
	    columns: [
	        { text: 'ID', dataIndex: 'id',hidden:true },
	        { text: '姓名', dataIndex: 'name', width:'70%' }
	    ],
	    listeners: {
	        itemdblclick: function (me, record, item, index, e, eOpts) {
	          
	        }
	    }
	});
	
	var personForm = Ext.create("Ext.form.Panel", {
		border : false,
		fieldDefaults : {
			msgTarget : 'side', // 提示信息在右旁边显示图标
			labelWidth : 50,
			allowBlank : false
		},
		defaults : {
			padding : 15,
			width : 180
		},
		defaultType : "textfield",
		items : [{
					fieldLabel : '姓名',
					name : 'name'
		}]
	});
	
	var addPersonWin = Ext.create("Ext.window.Window", {
		closeAction : 'hide',
		resizable : false,
		closable : true, // 是否可以关闭
		modal : true, // 是否为模态窗口
		items : personForm,
		buttons : [{
					text : '添加',
					id : "btnAdd",
					handler : function() {
						if (personForm.form.isValid()) {
							addPerson();
						}
					}
				}, {
					text : '修改',
					id : "btnEdit",
					handler : function() {
						if (personForm.form.isValid()) {
							editPerson();
						}
					}
				}, {
					id : "btnCancel",
					text : '重置',
					handler : function() {
						personForm.form.reset();
					}
				}]
	});
	
	function addPerson(){
		Ext.Ajax.request({
			method : "post",
			url : projectName+'person/add.do',
			params : {
				name : personForm.form.findField('name').getValue()
			},
			callback : function(options, success, response) {
				if (success) {
					var text = response.responseText;
					personStore.load();
					Ext.Msg.alert("提示", text);
					addPersonWin.hide();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function editPerson(){
		var rows = personListGrid.getSelectionModel().getSelection();
	     Ext.Ajax.request({
			method : "post",
			url : projectName+'person/edit.do',
			params : {
				id : rows[0].data.id,
				name : personForm.form.findField('name').getValue()
			},
			callback : function(options, success, response) {
				if (success) {
					var text = response.responseText;
					personStore.load();
					Ext.Msg.alert("提示", text);
					addPersonWin.hide();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function deletePerson(){
		var rows = personListGrid.getSelectionModel().getSelection();
	     Ext.Ajax.request({
			method : "post",
			url : projectName+'person/delete.do',
			params : {
				id : rows[0].data.id
			},
			callback : function(options, success, response) {
				if (success) {
					var text = response.responseText;
					personStore.load();
					Ext.Msg.alert("提示", text);
					addPersonWin.hide();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
})
