Ext.onReady(function(){
     //Ext.MessageBox.alert('info',markPlanId);
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
		
    var title = 'title';
    var myChart;
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
	    // 基于准备好的dom，初始化echarts图表
        myChart = echarts.init(document.getElementById('main')); 
        // 为echarts对象加载数据 
        myChart.setOption(option); 
        
        
    }
    
    drawChart();
    
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
                    var rst = Ext.decode(resp.responseText);
                    var data = rst;
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
                    myChart.setOption(option);    
                    //Ext.tools.tips("获取数据失败！");
                } catch (e) {
                    Ext.MessageBox.alert('ERROR',e)
                }
                 
            }
        });
    }
    
    
});