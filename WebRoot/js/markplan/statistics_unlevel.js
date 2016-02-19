Ext.onReady(function(){
	    Ext.define("MyApp.model.TargetLevel",{
		     extend: "Ext.data.Model",
	         fields: [
		        { name: 'id', type: 'string' },
		        { name: 'name', type: 'string' }
	          ]
		});
		
		var levelStore = Ext.create('Ext.data.Store', {
			model: "MyApp.model.TargetLevel",
			autoLoad : true,
            proxy: {
	            type: 'ajax',
	            url: projectPath+'target/getTargetLevelComboBox.do',
	            reader: {
	                type: 'json',
	                totalProperty: 'total',
	                root: 'datas'
	            }
	        }
		});
		
		var levelComboBox = Ext.create('Ext.form.ComboBox', {
			name : 'levelComboBox',
		    store: levelStore,
		    triggerAction: 'all',
		    queryMode: 'local',
		    displayField: 'name',
		    valueField: 'id'
		});
	    
	    var statisticsChartTbar = Ext.create("Ext.toolbar.Toolbar", {
	    	renderTo : 'tbar',
			items : [levelComboBox,
			  '-', {
			    text : '统计',
				handler : function() {
					statisticsChartByLevel();
				}
			}]
		});
		
    var option;
    function drawChart(){
        option = {
    	    title : {
    	        text: '指标归一化分数图'
    	    },
    	    tooltip : {
    	        trigger: 'axis'
    	    },
    	    legend: {
    	        data:[]
    	    },
    	    toolbox: {
    	        show : true,
    	        feature : {
    	            mark : {show: true},
    	            dataView : {show: true, readOnly: false},
    	            magicType : {show: true, type: ['line', 'bar']},
    	            restore : {show: true},
    	            saveAsImage : {show: true}
    	        }
    	    },
    	    calculable : true,
    	    xAxis : [],
    	    yAxis : [
    	        {
    	            type : 'value'
    	        }
    	    ],
    	    series : []
    	};
    }
    
    function statisticsChartByLevel(){
         Ext.Ajax.request({
            url : projectPath+'score/statisticsCharByLevel.do',
            method : 'POST',
            params : {
                markPlanId : markPlanId,
                level : levelComboBox.getValue() || 1
            },
            callback : function(gd, success, resp) {
                try {
                	drawChart();
                    var data = Ext.decode(resp.responseText);
                    var categories = data.categories;
                    var arr = data.series;
                    option.legend.data = [];
                    option.series = [];
                    Ext.Array.each(arr, function(v) {
                                var name = v["name"];
                                var bars = v["data"];
                                option.legend.data.push(name);
                                option.series.push({
                                            name : name,
                                            type : 'bar',
                                            data : bars
                                        });
                            });
                    option.xAxis = [];
                    option.xAxis.push({
                                type : 'category',
                                data : categories
                            });
                    var myChart = echarts.init(document.getElementById('main')); 
                    myChart.setOption(option);    
                } catch (e) {
                    Ext.MessageBox.alert('ERROR','无数据')
                }
            }
        });
    }
    
    
});