Ext.onReady(function() {

	// 获取store
	var TargetTreeGridStore = Ext.create('Ext.data.TreeStore', {
		model : 'Task',
		proxy : {
			type : 'ajax',
			actionMethods : {
				create : "POST",
				read : "GET",
				update : "POST",
				destroy : "POST"
			},
			url : projectName+'target/tree.do'
		},
		fields : ["id","text"],//,"lowScroe","topMarkSystem"
		folderSort : true
	});

	// 获取tbar
	var tbar = Ext.create("Ext.toolbar.Toolbar", {
		items : [{
			text : '添加兄弟分类',
			handler : function() {
				AddDialog.setTitle("添加兄弟分类");
				AddForm.form.reset();
				Ext.getCmp("btnAdd").show();
				Ext.getCmp("btnEdit").hide();
				Ext.getCmp("pName").hide();
				Ext.getCmp("bName").show();
				if (typeof(TargetTreeGrid) == "undefined") {
					return false;
				}
				var rows = TargetTreeGrid.getView().getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
				Ext.getCmp("bName").setValue(rows[0].data.text);
				AddDialog.show();
			}
		}, '-', {
			text : '添加子分类',
			handler : function() {
				AddDialog.setTitle("添加子分类");
				AddForm.form.reset();
				Ext.getCmp("btnAdd").show();
				Ext.getCmp("btnEdit").hide();
				Ext.getCmp("bName").hide();
				Ext.getCmp("pName").show();
				if (typeof(TargetTreeGrid) == "undefined") {
					return false;
				}
				var rows = TargetTreeGrid.getView().getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
				Ext.getCmp("pName").setValue(rows[0].data.text);
				AddDialog.show();
			}
		}, '-', {
			text : '删除',
			handler : DelTarget
		}, '-', {
			text : '修改',
			handler : function() {
				AddDialog.setTitle("修改分类");
				AddForm.form.reset();
				Ext.getCmp("btnEdit").show();
				Ext.getCmp("btnAdd").hide();
				Ext.getCmp("bName").hide();
				Ext.getCmp("pName").hide();
				if (typeof(TargetTreeGrid) == "undefined") {
					return false;
				}
				var rows = TargetTreeGrid.getView().getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
				AddForm.form.setValues(rows[0].data);
				AddDialog.show();
			}
		}, '-', {
			text : "刷新",
			handler : function() {
				// 刷新treepanel
				TargetTreeGridStore.load();
			}
		}, '-', {
			text : '展开所有',
			handler : function() {
				// 展开所有函数
				TargetTreeGrid.expandAll();
			}
		}, '-', {
			text : '折叠所有',
			handler : function() {
				// 折叠所有函数
				TargetTreeGrid.collapseAll();
			}
		}]
	});

	var TargetTreeGrid = Ext.create('Ext.tree.Panel', {
        title : '指标树',
		tbar : tbar,
		useArrows : true,
		rootVisible : false,
		store : TargetTreeGridStore,
		multiSelect : false,
		singleExpand : false,
		autoScroll : true,
		columns : [{ 
			xtype : 'treecolumn',
			text : '分类名称', 
			flex : 2,
			sortable : true,
			dataIndex : 'text' 
		}],
		listeners : {
			//itemclick : function(record, node) {
				
			//}
		}
	});

	// 弹出window的form表单
	var AddForm = Ext.create("Ext.form.Panel", {
				border : false,
				fieldDefaults : {
					msgTarget : 'side', // 提示信息在右旁边显示图标
					labelWidth : 105,
					align : "right",
					regexText : '格式错误', // 错误提示
					allowBlank : false
				},
				defaults : {
					padding : 15,
					width : 380
				},
				defaultType : "textfield",
				items : [{
							xtype : "hidden",
							name : 'pId'
						}, {
							fieldLabel : '父分类名称',
							id : "pName",
							name : 'pName',
							disabled : true
						}, {
							fieldLabel : '兄弟分类名称',
							id : "bName",
							name : 'bName',
							disabled : true
						}, {
							fieldLabel : '分类名称',
							name : 'text'

						}]
			});

	// 要弹出的window
	var AddDialog = Ext.create("Ext.window.Window", {
				closeAction : 'hide', // 窗口关闭的方式：hide/close
				resizable : false,
				closable : true, // 是否可以关闭
				modal : true, // 是否为模态窗口
				items : AddForm,
				buttons : [{
							text : '添加',
							id : "btnAdd",
							handler : function() {
								if (AddForm.form.isValid()) {
									if (AddDialog.title == '添加兄弟分类') {
										// O为兄弟分类，1为子分类
										AddTarget(0);
									} else {
										AddTarget(1);
									}
								}
							}
						}, {
							text : '修改',
							id : "btnEdit",
							handler : EditTarget
						}, {
							id : "btnCancel",
							text : '重置',
							handler : function() {
								AddForm.form.reset();
							}
						}]
			});
	var viewPort = Ext.create('Ext.Viewport',{
				layout : "border",
				renderTo : 'treeGrid',
				defaults : {
					bodyStyle : "background-color: #FFFFFF;",
					frame : true
				},
				items : [{
							region : "center",
							split : true,
							items : [TargetTreeGrid]
						}

				]
			});

	// 添加分类
	function AddTarget(type) {
		var rows = TargetTreeGrid.getView().getSelectionModel().getSelection();
		var id = rows[0].data.id;
		// 0  兄弟分类   1 子分类
		Ext.Ajax.request({
					method : "post",
					url : projectName+'target/add.do',
					params : {
						//target : Ext.JSON.encode(AddForm.form.getValues()),
						text : AddForm.form.findField('text').getValue(),
						id : id,
						type : type
					},
					callback : function(options, success, response) {
						if (success) {
							var text = response.responseText;
							TargetTreeGridStore.load();
							Ext.Msg.alert("提示", text);
							AddDialog.hide();
						} else {
							Ext.Msg.alert("提示", "系统繁忙");
						}
					}
				});
	}
	// 删除分类
	function DelTarget() {
		var rows = TargetTreeGrid.getView().getSelectionModel().getSelection();
		if (typeof(rows[0]) == "undefined") {
			Ext.Msg.alert("提示", "请选择要操作的行！");
			return false;
		}
		Ext.Ajax.request({
					method : "post",
					url : projectName+'target/delete.do',
					params : {
						id : rows[0].data.id
					},
					callback : function(options, success, response) {
						if (success) {
							var text = response.responseText;
							TargetTreeGridStore.load();
							Ext.Msg.alert("提示", text);
							AddDialog.hide();
						} else {
							Ext.Msg.alert("提示", "系统繁忙");
						}
					}
				});
	}
	// 修改分类
	function EditTarget() {
		var rows = TargetTreeGrid.getView().getSelectionModel().getSelection();
		if (typeof(rows[0]) == "undefined") {
			Ext.Msg.alert("提示", "请选择要操作的行！");
			return false;
		}
		Ext.Ajax.request({
					method : "post",
					url : projectName+'target/edit.do',
					params : {
						text : AddForm.form.findField('text').getValue(),
						id : rows[0].data.id
					},
					callback : function(options, success, response) {
						if (success) {
							var text = response.responseText;
							TargetTreeGridStore.load();
							Ext.Msg.alert("提示", text);
							AddDialog.hide();
						} else {
							Ext.Msg.alert("提示", "系统繁忙");
						}
					}
				});
	}
	
});