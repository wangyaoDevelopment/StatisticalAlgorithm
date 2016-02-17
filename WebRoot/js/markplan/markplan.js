Ext.onReady(function(){
	Ext.define("MyApp.model.MarkPlan",{
	     extend: "Ext.data.Model",
         fields: [
	        { name: 'id', type: 'string' },
	        { name: 'name', type: 'string' },
	        { name: 'mark', type: 'double' }
          ]
	});
	
	var markPlanStore = Ext.create("Ext.data.Store", {
	    model: "MyApp.model.MarkPlan",
	    autoLoad: true,
	    pageSize: 20,
	    proxy: {
	        type: "ajax",
	        url: projectName+"markplan/list.do",
	        reader: {  
                type: "json",  
                root: "data",  
                totalProperty: "total"  
            }  
	    }
	});
	
	var pagebar = Ext.create("Ext.toolbar.Paging", {  
        store: markPlanStore,  
        displayInfo: true,  
        displayMsg: "显示{0}-{1}条,共计{2}条",  
        emptyMsg: "没有数据"  
  
    }); 
    
	var grid = Ext.create("Ext.grid.Panel", {
	    xtype: "grid",
	    store: markPlanStore,
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
            tooltip: "添加打分计划",  
            handler: function(){
				Ext.getCmp("editMarkPlan").hide();
			    Ext.getCmp("addMarkPlan").show();
			    addMarkPlanForm.form.reset();
				addMarkPlanDialog.setTitle("添加打分计划");
                addMarkPlanDialog.show();
            }  
        },{  
            xtype: "button",  
            frame: true,  
            text: "编辑",  
            scale: "small",  
            tooltip: "编辑",  
            handler: function(){
                 var rows = grid.getSelectionModel().getSelection();
				 if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				 }
				 Ext.getCmp("editMarkPlan").show();
				 Ext.getCmp("addMarkPlan").hide();
                 addMarkPlanDialog.setTitle("编辑打分计划");
				 addMarkPlanForm.form.setValues(rows[0].data);
				 addMarkPlanDialog.show();
            }  
        },{  
            xtype: "button",  
            frame: true,  
            text: "删除",  
            scale: "small",  
            tooltip: "删除",  
            handler: delMarkPlan  
        },{  
            xtype: "button",  
            frame: true,  
            text: "刷新",  
            scale: "small",  
            tooltip: "刷新",  
            handler: function(){
                markPlanStore.load();
            }  
        },{  
            xtype: "button",  
            frame: true,  
            text: "设置权重",  
            scale: "small",  
            tooltip: "设置权重",  
            handler: function(){
            	var rows = grid.getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
				checkWeight();
            }  
        },{  
            xtype: "button",  
            frame: true,  
            text: "打分",  
            scale: "small",  
            tooltip: "打分",  
            handler: function(){
            	var rows = grid.getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
				checkMarkPlanWeight();
            }  
        },'-',{
		    text : '层级统计',
		    handler : function(){
		        var rows = grid.getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
                openStatisticsLevelWin().show();
		    }
		},'-',{
		    text : '指定指标统计',
		    handler : function(){
		        var rows = grid.getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
                openStatisticsUnLevelListWin().show();
		    }
		},'-',{
			text : '图表层级统计',
			handler : function(){
			    var rows = grid.getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
                openStatisticsChartByLevelWin().show();
			}
		}],  
        //分页功能   
        bbar: pagebar, 
	    selModel: {
	        mode: "SINGLE"     //"SINGLE"/"SIMPLE"/"MULTI"
	    },
	    selType: "checkboxmodel",
	    columns: [
	        { text: 'ID', dataIndex: 'id',hidden:true },
	        { text: '打分计划名称', dataIndex: 'name', width:'70%' },
	        { text: '分制', dataIndex: 'mark', width:'25%' }
	    ],
	    listeners: {
	        itemdblclick: function (me, record, item, index, e, eOpts) {
	           personListWin.show();
	        }
	    }
	});
	
	function openStatisticsChartByLevelWin(){
	    var statisticsChartByLevelWin = Ext.create('Ext.window.Window',{
	           title : '图表层级统计',
               height : 500,
               modal : true,
               width : 1000,
               closeAction : 'hide',
			   resizable : false,
		       closable : true,
               loader: { 
                            url: projectName+'score/gotoStatisticsChartByLevel.do',
                            autoLoad: true, 
                            scripts: true,
                            params : {
                                markPlanId : grid.getSelectionModel().getSelection()[0].data.id
                            }
                 } 
	    });
	    return statisticsChartByLevelWin;
	}
	
	function checkMarkPlanWeight(){
		var rows = grid.getSelectionModel().getSelection();
	    Ext.Ajax.request({
			method : "post",
			url : projectName+'target/checkMarkPlanWeight.do',
			params : {
				markPlanId : rows[0].data.id
			},
			callback : function(options, success, response) {
				if (success) {
					var result = response.responseText;
					if(result == '2'){
					    personListWin.show();
					    return;
					}
					if(result == '0'){
					    Ext.MessageBox.confirm(" 确认", "尚未设置权重,确定要使用默认权重吗？",function(btn){
						      if(btn == 'yes'){
						           Ext.Ajax.request({
									method : "post",
									url : projectName+'target/weightTree.do',
									params : {
										markPlanId : rows[0].data.id,
										mark : rows[0].data.mark
									},
									callback : function(options, success, response) {
										if (success) {
											personListWin.show();
										} else {
											Ext.Msg.alert("提示", "系统繁忙");
										}
									}
								});
						      }else{
						          checkWeight();
						      }
						});
					}
					if(result == '1'){
					    personListWin.show();
					}
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function addMarkPlan(){
		if(!addMarkPlanForm.form.isValid()) {
            return false;	 
	    }
		var name = addMarkPlanForm.form.findField('name').getValue();
		var mark = addMarkPlanForm.form.findField('mark').getValue();
		Ext.Ajax.request({
			method : "post",
			url : projectName+'markplan/addMarkPlan.do',
			params : {
				name : name,
				mark : mark
			},
			callback : function(options, success, response) {
				if (success) {
					var text = response.responseText;
					markPlanStore.load();
					Ext.Msg.alert("提示", text);
					addMarkPlanDialog.hide();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function checkMarkPlanForUpdate(){
	    var id = addMarkPlanForm.form.findField('id').getValue();
	    var mark = addMarkPlanForm.form.findField('mark').getValue();
		Ext.Ajax.request({
			method : "post",
			url : projectName+'markplan/checkMarkPlan.do',
			params : {
				id : id,
				mark : mark
			},
			callback : function(options, success, response) {
				if (success) {
					var result = response.responseText;
					if(result == '1'){
					    Ext.MessageBox.alert('提示','该计划已经打分,无法更改分制');
					    return false;
					}
					editMarkPlan();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function editMarkPlan(){
		var id = addMarkPlanForm.form.findField('id').getValue();
		var name = addMarkPlanForm.form.findField('name').getValue();
		var mark = addMarkPlanForm.form.findField('mark').getValue();
		Ext.Ajax.request({
			method : "post",
			url : projectName+'markplan/editMarkPlan.do',
			params : {
				id : id,
				name : name,
				mark : mark
			},
			callback : function(options, success, response) {
				if (success) {
					var text = response.responseText;
					markPlanStore.load();
					Ext.Msg.alert("提示", text);
					addMarkPlanDialog.hide();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function delMarkPlan(){
		var rows = grid.getSelectionModel().getSelection();
		Ext.MessageBox.confirm(" 确认", "确定要删除吗？",function(btn){
		      if(btn == 'yes'){
		           Ext.Ajax.request({
					method : "post",
					url : projectName+'markplan/delMarkPlan.do',
					params : {
						id : rows[0].data.id
					},
					callback : function(options, success, response) {
						if (success) {
							var text = response.responseText;
							markPlanStore.load();
							Ext.Msg.alert("提示", text);
						} else {
							Ext.Msg.alert("提示", "系统繁忙");
						}
					}
				});
		      }
		});
	}
	
	var addMarkPlanForm = Ext.create("Ext.form.Panel", {
		border : false,
		fieldDefaults : {
			msgTarget : 'side', // 提示信息在右旁边显示图标
			labelWidth : 100,
			align : "right",
			regexText : '格式错误', // 错误提示
			allowBlank : false
		},
		defaults : {
			padding : 15,
			width : 250
		},
		defaultType : "textfield",
		items : [{
		            xtype : 'hidden',
		            name : 'id'
		        },{ // 表单中的数字控件
					fieldLabel : '打分计划名称',
					name : 'name'
				},{ // 表单中的数字控件
					fieldLabel : '分制',
					name : 'mark'
				}]
	});
			
	var addMarkPlanDialog = Ext.create("Ext.window.Window", {
        id : 'addMarkPlanDialog',
		closeAction : 'hide', // 窗口关闭的方式：hide/close
		resizable : false,
		closable : true, // 是否可以关闭
		modal : true, // 是否为模态窗口
		items : addMarkPlanForm,
		buttons : [{
					text : '添加',
					id : "addMarkPlan",
					handler : addMarkPlan
				}, {
					text : '修改',
					id : "editMarkPlan",
					handler : checkMarkPlanForUpdate
				}, {
					id : "btnCancel",
					text : '重置',
					handler : function() {
						addMarkPlanForm.form.reset();
					}
				}]
	});
	
	//------------------------打分------------------------
	
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
	    pageSize: 15,
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
	
	var personPagebar = Ext.create("Ext.toolbar.Paging", {  
        store: personStore,  
        displayInfo: true,  
        displayMsg: "显示{0}-{1}条,共计{2}条",  
        emptyMsg: "没有数据"  
  
    }); 
    
	var personGrid = Ext.create("Ext.grid.Panel", {
		id : 'personGrid',
	    xtype: "grid",
	    store: personStore,
	    width: 490,
	    height: 450,
	    columnLines: true,
	    simpleSelect: true,
	    tbar: [{  
            xtype: "button",  
            frame: true,  
            text: "打分",  
            scale: "small",  
            tooltip: "开始打分",  
            handler: function(){
            	var rows = personGrid.getSelectionModel().getSelection();
				if (typeof(rows[0]) == "undefined") {
					Ext.Msg.alert("提示", "请选择要操作的行！");
					return false;
				}
				openTargetTreeWin().show();
            }  
        }],  
        //分页功能   
        bbar: personPagebar, 
	    selModel: {
	        mode: "SINGLE"
	    },
	    selType: "checkboxmodel",
	    columns: [
	        { text: 'ID', dataIndex: 'id',hidden:true },
	        { text: '姓名', dataIndex: 'name', width:'95%' }
	    ],
	    listeners: {
	        itemdblclick: function (me, record, item, index, e, eOpts) {
	            openTargetTreeWin().show();
	        }
	    }
	});
	
	 var personListWin = Ext.create("Ext.window.Window",{
		id : 'personListWin',
		title : '人员列表',
		width : 500,
		height : 500,
	    closeAction : 'hide', // 窗口关闭的方式：hide/close
		resizable : false,
		closable : true, // 是否可以关闭
		modal : true, // 是否为模态窗口
		items : personGrid
	});
	
	//打分界面
	function openTargetTreeWin(){
		function getMarkPlanId(){
		    var rows = grid.getView().getSelectionModel().getSelection();
			if (typeof(rows[0]) == "undefined") {
				return "null";
			}
			return rows[0].data.id;
		}
		
		function getPersonId(){
		    var rows = personGrid.getView().getSelectionModel().getSelection();
			if (typeof(rows[0]) == "undefined") {
				return "null";
			}
			return rows[0].data.id;
		}
		
		var targetTreeGridStore = Ext.create('Ext.data.TreeStore', {
			model : 'Task',
			proxy : {
				type : 'ajax',
				actionMethods : {
					create : "POST",
					read : "GET",
					update : "POST",
					destroy : "POST"
				},
				url : projectName+'target/scoreTree.do?markPlanId='+getMarkPlanId()+'&personId='+getPersonId()
			},
			fields : ["id","text","weight","level","topScore","score"],
			folderSort : true
		});
		
		var targetTreeGrid = Ext.create('Ext.tree.Panel', {
			useArrows : true,
			rootVisible : false,
			store : targetTreeGridStore,
			multiSelect : false,
			singleExpand : false,
			autoScroll : true,
			columns : [{ 
				xtype : 'treecolumn',
				text : '分类名称', 
				flex : 2,
				sortable : true,
				dataIndex : 'text' 
			}, {
				text : '权重',
				xtype : 'numbercolumn',
				flex : 1,
				dataIndex : 'weight',
				sortable : true,
				editor: {
	                xtype: "numberfield",
	                decimalPrecision: 2,
	                selectOnFocus: true
	            }
			}, {
				text : '最大分值',
				xtype : 'numbercolumn',
				flex : 1,
				dataIndex : 'topScore',
				sortable : true,
				editor: {
	                xtype: "numberfield",
	                decimalPrecision: 2,
	                selectOnFocus: true
	            }
			}, {
				text : '得分',
				xtype : 'numbercolumn',
				flex : 1,
				dataIndex : 'score',
				sortable : true,
				editor: {
	                xtype: "numberfield",
	                decimalPrecision: 2,
	                selectOnFocus: true
	            }
			}],
			listeners : {
				itemclick : function(record, node) {
					openSetScoreWin();
				}
			}
		});
		
		function openSetScoreWin(){
			var rows = targetTreeGrid.getView().getSelectionModel().getSelection();
			if (typeof(rows[0]) == "undefined") {
				Ext.Msg.alert("提示", "请选择要操作的行！");
				return false;
			}
			setScoreWin.show();
		}
		
		var targetTreeWin = Ext.create("Ext.window.Window",{
			//id : 'targetTreeWin',
			title : '人员打分',
			width : 700,
			height : 500,
		    closeAction : 'hide', // 窗口关闭的方式：hide/close
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : targetTreeGrid
		});
		
		var setScoreForm = Ext.create("Ext.form.Panel", {
			border : false,
			fieldDefaults : {
				msgTarget : 'side', // 提示信息在右旁边显示图标
				labelWidth : 50,
				align : "right",
				regexText : '格式错误', // 错误提示
				allowBlank : false
			},
			defaults : {
				padding : 15,
				width : 250
			},
			defaultType : "textfield",
			items : [{ // 表单中的数字控件
						fieldLabel : '得分',
						name : 'score'
					}]
		});
		
		var setScoreWin = Ext.create("Ext.window.Window",{
			title : '设置分数',
			width : 300,
			height : 140,
		    closeAction : 'hide', // 窗口关闭的方式：hide/close
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : setScoreForm,
			buttons : [{
						text : '确定',
						handler : setScore
					}, {
						text : '重置',
						handler : function() {
							setScoreForm.form.reset();
						}
					}]
		});
		
		function setScore(){
			//Ext.MessageBox.alert('markPlan',node.data.id+"##"+markPlanRows[0].data.id+'##'+personRows[0].data.id);
			if(setScoreForm.isValid()){
			    var markPlanRows = grid.getSelectionModel().getSelection();
				var personRows = personGrid.getSelectionModel().getSelection();
				var targetRows = targetTreeGrid.getView().getSelectionModel().getSelection();
				Ext.Ajax.request({
					method : "post",
					url : projectName+'score/addScore.do',
					params : {
						markPlanId : markPlanRows[0].data.id,
						personId : personRows[0].data.id,
						targetId : targetRows[0].data.id,
						score : setScoreForm.form.findField('score').getValue()
					},
					callback : function(options, success, response) {
						if (success) {
							var text = response.responseText;
							targetTreeGridStore.load();
							Ext.Msg.alert("提示", text);
							setScoreWin.hide();
						} else {
							Ext.Msg.alert("提示", "系统繁忙");
						}
					}
				});
			}
		}
		return targetTreeWin;
	}
	
	//----------------------指定层级统计------------------------
	function openStatisticsLevelWin(){
	     Ext.define("MyApp.model.StatisticsLevel",{
		     extend: "Ext.data.Model",
	         fields: [
		        { name: 'name', type: 'string' },
		        { name: 'score', type: 'double' }
	          ]
		});
		
		var statisticsLevelStore = Ext.create("Ext.data.Store", {
		    model: "MyApp.model.StatisticsLevel",
		    //autoLoad: true,
		    pageSize: 10,
		    proxy: {
		        type: "ajax",
		        url: projectName+"score/zFractionByLevelList.do",
		        reader: {  
	                type: "json",  
	                root: "data",  
	                totalProperty: "total"  
	            }
		    }
		});
		
		var statisticsLevelPagebar = Ext.create("Ext.toolbar.Paging", {  
	        store: statisticsLevelStore,  
	        displayInfo: true,  
	        displayMsg: "显示{0}-{1}条,共计{2}条",  
	        emptyMsg: "没有数据"  
	  
	    }); 
	    
		var statisticsLevelGrid = Ext.create("Ext.grid.Panel", {
		    xtype: "grid",
		    height : 455,
		    store: statisticsLevelStore,
		    columnLines: true,
		    simpleSelect: true,
	        bbar: statisticsLevelPagebar, 
		    selModel: {
		        mode: "SINGLE"
		    },
		    columns: [
		        { text: 'ID', dataIndex: 'id',hidden:true },
		        { text: '姓名', dataIndex: 'name', width:'50%' },
		        { text: '归一化分数', dataIndex: 'score', width:'50%',
		            xtype : 'numbercolumn',
					editor: {
		                xtype: "numberfield",
		                decimalPrecision: 4
		            }
		        }
		    ]
		});
		
		Ext.define("MyApp.model.RootTarget",{
		     extend: "Ext.data.Model",
	         fields: [
		        { name: 'id', type: 'string' },
		        { name: 'text', type: 'string' }
	          ]
		});
		
		var rootTargetStore = Ext.create("Ext.data.Store", {
		    model: "MyApp.model.RootTarget",
		    autoLoad: true,
		    pageSize: 13,
		    proxy: {
		        type: "ajax",
		        url: projectName+"target/getRootTargetList.do",
		        reader: {  
	                type: "json",  
	                root: "data",  
	                totalProperty: "total"  
	            }
		    }
		});
		
		var rootTargetPagebar = Ext.create("Ext.toolbar.Paging", {  
	        store: rootTargetStore,  
	        displayInfo: true,  
	        displayMsg: "显示{0}-{1}条,共计{2}条",  
	        emptyMsg: "没有数据"  
	  
	    }); 
	    
	    var levelStore = Ext.create('Ext.data.Store', {
		    fields: ['id', 'name'],
		    data : [
		        {"id":"1", "name":"一级"},
		        {"id":"2", "name":"二级"},
		        {"id":"3", "name":"三级"},
		        {"id":"4", "name":"四级"},
		        {"id":"5", "name":"五级"},
		        {"id":"6", "name":"六级"}
		    ]
		});
		
		var levelComboBox = Ext.create('Ext.form.ComboBox', {
			name : 'levelComboBox',
		    store: levelStore,
		    queryMode: 'local',
		    displayField: 'name',
		    valueField: 'id'
		});
	    
	    var rootTargetTbar = Ext.create("Ext.toolbar.Toolbar", {
			items : [levelComboBox,
			  '-', {
			    text : '统计',
				handler : function() {
					var rows = rootTargetGrid.getSelectionModel().getSelection();
					if (typeof(rows[0]) == "undefined") {
						Ext.Msg.alert("提示", "请选择要操作的行！");
						return false;
					}
					if(levelComboBox.getValue() == ''){
					    Ext.Msg.alert("提示", "请选择层级！");
					    return false;
					}
					startStatisticsLevel(rows[0].data.id);
				}
			}]
		});
	    
		var rootTargetGrid = Ext.create("Ext.grid.Panel", {
		    xtype: "grid",
		    height : 450,
		    store: rootTargetStore,
		    bbar : rootTargetPagebar,
		    tbar : rootTargetTbar,
		    columnLines: true,
		    simpleSelect: true,
	        //分页功能   
	        bbar: rootTargetPagebar, 
		    selModel: {
		        mode: "SINGLE"
		    },
		    selType: "checkboxmodel",
		    columns: [
		        { text: 'ID', dataIndex: 'id',hidden:true },
		        { text: '指标', dataIndex: 'text', width:'90%' }
		    ]
		});
		
		function startStatisticsLevel(targetId){
		    statisticsLevelStore.getProxy().extraParams = { 
                 markPlanId : grid.getView().getSelectionModel().getSelection()[0].data.id,
                 targetId : targetId,
                 level : levelComboBox.getValue()
            };
		    statisticsLevelStore.load();
		}
		
		var statisticsLevelMainPanel = Ext.create('Ext.Panel', {
		    width: 1010,
		    height : 500,
		    layout: {
		        type: 'hbox',     //指定为hbox布局
		        align: 'stretch'  //指定元素的高将充满容器的垂直空间
		    },
		    items: [{
		        xtype: 'panel',
		        flex: 1,
		        items : rootTargetGrid
		    },{
		        xtype: 'panel',
		        flex: 3,
		        items : statisticsLevelGrid
		    }]
		});
		
		 var statisticsLevelListWin = Ext.create("Ext.window.Window",{
			title : '统计',
			width : 1000,
			height : 500,
		    closeAction : 'close', // 窗口关闭的方式：hide/close
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : statisticsLevelMainPanel
		});
		
		return statisticsLevelListWin;
	}
	
	//---------------------指定指标统计-----------------
	function openStatisticsUnLevelListWin(){
		Ext.define("MyApp.model.SelectRootTarget",{
		     extend: "Ext.data.Model",
	         fields: [
		        { name: 'id', type: 'string' },
		        { name: 'text', type: 'string' }
	          ]
		});
		
		var selectTargetStore = Ext.create("Ext.data.Store", {
		    model: "MyApp.model.SelectRootTarget",
		    autoLoad: true,
		    pageSize: 13,
		    proxy: {
		        type: "ajax",
		        url: projectName+"target/getRootTargetList.do",
		        reader: {  
	                type: "json",  
	                root: "data",  
	                totalProperty: "total"  
	            }
		    }
		});
		
		var selectTargetPagebar = Ext.create("Ext.toolbar.Paging", {  
	        store: selectTargetStore,  
	        displayInfo: true,  
	        displayMsg: "显示{0}-{1}条,共计{2}条",  
	        emptyMsg: "没有数据"  
	  
	    }); 
	    
		var selectTargetGrid = Ext.create("Ext.grid.Panel", {
		    xtype: "grid",
		    height : 420,
		    store: selectTargetStore,
		    bbar : selectTargetPagebar,
		    columnLines: true,
	        rowLines: true,  
	        selType: "checkboxmodel",  
	        // simpleSelect: true,  
	        multiSelect: true,  
	        viewConfig: {  
	            forceFit: true,  
	            stripeRows: true //在表格中显示斑马线  
	        },
		    selModel: {
		        mode: "MULTI"
		    },
		    selType: "checkboxmodel",
		    columns: [
		        { text: 'ID', dataIndex: 'id',hidden:true },
		        { text: '指标', dataIndex: 'text', width:'90%' }
		    ]
		});
		
		var selectTargetWin = Ext.create("Ext.window.Window",{
			title : '选择指标',
			width : 500,
			height : 500,
		    closeAction : 'hide',
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : selectTargetGrid,
			buttons : [{
			     text : '下一步',
			     handler : function(){
			     	var rows = selectTargetGrid.getSelectionModel().getSelection();
					if (typeof(rows[0]) == "undefined") {
						Ext.Msg.alert("提示", "请选择要需要统计的指标！");
						return false;
					}
			        statisticsUnLevelListWin.show();
			     }
			}]
		});
		
	     Ext.define("MyApp.model.StatisticsUnLevel",{
		     extend: "Ext.data.Model",
	         fields: [
		        { name: 'name', type: 'string' },
		        { name: 'score', type: 'double' }
	          ]
		});
		
		var statisticsUnLevelStore = Ext.create("Ext.data.Store", {
		    model: "MyApp.model.StatisticsUnLevel",
		    pageSize: 10,
		    proxy: {
		        type: "ajax",
		        url: projectName+"score/zFractionByUnLevelList.do",
		        reader: {  
	                type: "json",  
	                root: "data",  
	                totalProperty: "total"  
	            }
		    }
		});
		
		var statisticsUnLevelPagebar = Ext.create("Ext.toolbar.Paging", {  
	        store: statisticsUnLevelStore,  
	        displayInfo: true,  
	        displayMsg: "显示{0}-{1}条,共计{2}条",  
	        emptyMsg: "没有数据"  
	  
	    }); 
	    
		var statisticsUnLevelGrid = Ext.create("Ext.grid.Panel", {
		    xtype: "grid",
		    height : 455,
		    store: statisticsUnLevelStore,
		    columnLines: true,
		    simpleSelect: true,
	        //分页功能   
	        bbar: statisticsUnLevelPagebar, 
		    selModel: {
		        mode: "SINGLE"
		    },
		    columns: [
		        { text: 'ID', dataIndex: 'id',hidden:true },
		        { text: '姓名', dataIndex: 'name', width:'50%' },
		        { text: '归一化分数', dataIndex: 'score', width:'50%',
		            xtype : 'numbercolumn',
					editor: {
		                xtype: "numberfield",
		                decimalPrecision: 4
		            }
		        }
		    ]
		});
		
		Ext.define("MyApp.model.RootTarget",{
		     extend: "Ext.data.Model",
	         fields: [
		        { name: 'id', type: 'string' },
		        { name: 'text', type: 'string' }
	          ]
		});
		
		var rootTargetStore = Ext.create("Ext.data.Store", {
		    model: "MyApp.model.RootTarget",
		    autoLoad: true,
		    pageSize: 13,
		    proxy: {
		        type: "ajax",
		        url: projectName+"target/getRootTargetList.do",
		        reader: {  
	                type: "json",  
	                root: "data",  
	                totalProperty: "total"  
	            }
		    }
		});
		
		var rootTargetPagebar = Ext.create("Ext.toolbar.Paging", {  
	        store: rootTargetStore,  
	        displayInfo: true,  
	        displayMsg: "显示{0}-{1}条,共计{2}条",  
	        emptyMsg: "没有数据"  
	    }); 
	    
	    var rootTargetTbar = Ext.create("Ext.toolbar.Toolbar", {
			items : [{
			    text : '统计',
				handler : function() {
					var rows = rootTargetGrid.getSelectionModel().getSelection();
					if (typeof(rows[0]) == "undefined") {
						Ext.Msg.alert("提示", "请选择要操作的行！");
						return false;
					}
					startStatisticsUnLevel(rows);
				}
			}]
		});
	    
		var rootTargetGrid = Ext.create("Ext.grid.Panel", {
		    xtype: "grid",
		    height : 450,
		    store: rootTargetStore,
		    bbar : rootTargetPagebar,
		    tbar : rootTargetTbar,
		    columnLines: true,
	        rowLines: true,  
	        selType: "checkboxmodel",  
	        simpleSelect: true,  
//	        multiSelect: true,  
	        viewConfig: {  
	            forceFit: true,  
	            stripeRows: true //在表格中显示斑马线  
	        },
	        bbar: rootTargetPagebar, 
		    selModel: {
		        mode: "SINGLE"
		    },
		    selType: "checkboxmodel",
		    columns: [
		        { text: 'ID', dataIndex: 'id',hidden:true },
		        { text: '指标', dataIndex: 'text', width:'90%' }
		    ]
		});
		
		function startStatisticsUnLevel(row){
			var rows = selectTargetGrid.getSelectionModel().getSelection();
			var targetIds = new Array();
			for(var i=0;i<rows.length;i++){
			    targetIds.push(rows[i].data.id)
			}
		    statisticsUnLevelStore.getProxy().extraParams = { 
                 markPlanId : grid.getView().getSelectionModel().getSelection()[0].data.id,
                 targetIds : targetIds,
                 targetId : row[0].data.id
            };
		    statisticsUnLevelStore.load();
		}
		
		var statisticsUnLevelMainPanel = Ext.create('Ext.Panel', {
		    width: 1010,
		    height : 500,
		    layout: {
		        type: 'hbox',     //指定为hbox布局
		        align: 'stretch'  //指定元素的高将充满容器的垂直空间
		    },
		    items: [{
		        xtype: 'panel',
		        flex: 1,
		        items : rootTargetGrid
		    },{
		        xtype: 'panel',
		        flex: 3,
		        items : statisticsUnLevelGrid
		    }]
		});

		 var statisticsUnLevelListWin = Ext.create("Ext.window.Window",{
			title : '统计',
			width : 1000,
			height : 500,
		    closeAction : 'close',
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : statisticsUnLevelMainPanel
		});
		
//		return statisticsUnLevelListWin;
		return selectTargetWin;
	}
	
	//------------------设置权重---------------------
	function  checkWeight(){
	    var rows = grid.getSelectionModel().getSelection();
		Ext.Ajax.request({
			method : "post",
			url : projectName+'markplan/checkMarkPlanForSetWeight.do',
			params : {
				markPlanId : rows[0].data.id
			},
			callback : function(options, success, response) {
				if (success) {
					var result = response.responseText;
					if(result == '1'){
					    Ext.MessageBox.alert('提示','该计划已经打分,无法设置权重');
					    return false;
					}
					setWeight().show();
				} else {
					Ext.Msg.alert("提示", "系统繁忙");
				}
			}
		});
	}
	
	function setWeight(){
		var markPlanRows = grid.getSelectionModel().getSelection();
		var targetWeightTreeGridStore = Ext.create('Ext.data.TreeStore', {
			model : 'Task',
			proxy : {
				type : 'ajax',
				actionMethods : {
					create : "POST",
					read : "GET",
					update : "POST",
					destroy : "POST"
				},
				url : projectName+'target/weightTree.do?markPlanId='+markPlanRows[0].data.id+'&mark='+markPlanRows[0].data.mark
			},
			fields : ["id","text","weight","level","topScore"],
			folderSort : true
		});
	
		// 获取tbar
		var tbar = Ext.create("Ext.toolbar.Toolbar", {
			items : [{
				text : '设置权重',
				handler : function() {
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
					targetWeightTreeGridStore.load();
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
			tbar : tbar,
			useArrows : true,
			rootVisible : false,
			store : targetWeightTreeGridStore,
			multiSelect : false,
			singleExpand : false,
			autoScroll : true,
			columns : [{ 
				xtype : 'treecolumn',
				text : '分类名称', 
				flex : 2,
				sortable : true,
				dataIndex : 'text' 
			}, {
				text : '权重',
				xtype : 'numbercolumn',
				flex : 1,
				dataIndex : 'weight',
				sortable : true,
				editor: {
	                xtype: "numberfield",
	                decimalPrecision: 2,
	                selectOnFocus: true
	            }
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
						fieldLabel : '分类名称',
						name : 'text',
						disabled : true

					}, { // 表单中的数字控件
						fieldLabel : '权重',
						//regex : /^\d+$/,
						//regexText : '格式错误',
						name : 'weight',
						//xtype : 'numbercolumn',
						editor: {
			                xtype: "numberfield",
			                decimalPrecision: 2
			            }
					}]
		});
	
		// 要弹出的window
		var AddDialog = Ext.create("Ext.window.Window", {
			title : '设置权重',
			closeAction : 'hide', // 窗口关闭的方式：hide/close
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : AddForm,
			buttons : [{
						text : '设置',
						handler : EditTargetWeight
					}, {
						text : '重置',
						handler : function() {
							AddForm.form.reset();
						}
					}]
		});
		
		var TargetTreeGridWin = Ext.create("Ext.window.Window", {
			title : '设置权重',
			width : 500,
			height : 500,
		    closeAction : 'hide', // 窗口关闭的方式：hide/close
			resizable : false,
			closable : true, // 是否可以关闭
			modal : true, // 是否为模态窗口
			items : TargetTreeGrid
		});
		
		// 修改分类
		function EditTargetWeight() {
			var markPlanRows = grid.getView().getSelectionModel().getSelection();
			var targetTreeRows = TargetTreeGrid.getView().getSelectionModel().getSelection();
			if (typeof(targetTreeRows[0]) == "undefined") {
				Ext.Msg.alert("提示", "请选择要操作的行！");
				return false;
			}
			Ext.Ajax.request({
				method : "post",
				url : projectName+'target/setTargetWeight.do',
				params : {
					targetId : targetTreeRows[0].data.id,
					markPlanId : markPlanRows[0].data.id,
					weight : AddForm.form.findField('weight').getValue()
				},
				callback : function(options, success, response) {
					if (success) {
						var text = response.responseText;
						targetWeightTreeGridStore.load();
						Ext.Msg.alert("提示", text);
						AddDialog.hide();
					} else {
						Ext.Msg.alert("提示", "系统繁忙");
					}
				}
			});
		}
		
		return TargetTreeGridWin;
	}
	
});