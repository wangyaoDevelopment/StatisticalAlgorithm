package com.sxkl.markplan.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sxkl.markplan.service.MarkPlanService;

/**
 * 打分计划控制器
 * @author wangyao
 * @date 2016-02-19
 */
@Controller
@RequestMapping(value="/markplan")
public class MarkPlanController {

	@Autowired
	@Qualifier("markPlanServiceImpl")
	private MarkPlanService markPlanServiceImpl;
	
	/**
	 * 分页查询打分计划
	 * @param start 起始条目
	 * @param limit 每页条目数
	 * @return 打分计划
	 */
	@ResponseBody
	@RequestMapping(value = "/list.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getMarkPlanPage(int start, int limit) {
		String result = this.markPlanServiceImpl.getListPage(start,limit);
		return result;
	}
	
	/**
	 * 添加打分计划
	 * @param name 打分计划名称
	 * @param mark 打分计划分制
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/addMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String addMarkPlan(String name,double mark) {
		return this.markPlanServiceImpl.addMarkPlan(name,mark);
	}
	
	/**
	 * 更新打分计划
	 * @param id 打分计划ID
	 * @param name 打分计划名称
	 * @param mark 打分计划分制
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/editMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String editMarkPlan(String id,String name,double mark) {
		return this.markPlanServiceImpl.editMarkPlan(id,name,mark);
	}
	
	/**
	 * 删除打分计划
	 * @param id 打分计划ID
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/delMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String delMarkPlan(String id) {
		return this.markPlanServiceImpl.delMarkPlan(id);
	}
	
	/**
	 * 检查该打分计划是否已经打分
	 * @param id 打分计划ID
	 * @param mark 分制
	 * @return 检查结果 0是未打分  1是已经打分  
	 */
	@ResponseBody
	@RequestMapping(value="/checkMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String checkMarkPlan(String id,double mark) {
		return this.markPlanServiceImpl.checkMarkPlan(id,mark);
	}
	
	/**
	 * 设置权重前检查该打分计划是否打分
	 * @param markPlanId 打分计划ID
	 * @return 检查结果 0是未打分  1是已经打分 
	 */
	@ResponseBody
	@RequestMapping(value="/checkMarkPlanForSetWeight.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String checkMarkPlanForSetWeight(String markPlanId) {
		return this.markPlanServiceImpl.checkMarkPlanForSetWeight(markPlanId);
	}
	
	/**
	 * 为打分计划添加参与人员
	 * @param markPlanId 打分计划ID
	 * @param personIds 参与人员ID
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/setMarkPlanPerson.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String setMarkPlanPerson(String markPlanId,String[] personIds) {
		return this.markPlanServiceImpl.setMarkPlanPerson(markPlanId,personIds);
	}
	
	/**
	 * 获取该打分计划参与人员的集合
	 * @param markPlanId 打分计划ID
	 * @return 该打分计划的参与人员
	 */
	@ResponseBody
	@RequestMapping(value="/selectedPersonList.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String selectedPersonList(String markPlanId) {
		String result = this.markPlanServiceImpl.selectedPersonList(markPlanId);
		return result;
	}
	
	/**
	 * 检查该打分计划是否已选择参与人员
	 * @param markPlanId 打分计划ID
	 * @return 检查结果 0是未选择  1是选择
	 */
	@ResponseBody
	@RequestMapping(value="/checkSamplingPopulation.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String checkSamplingPopulation(String markPlanId) {
		return this.markPlanServiceImpl.checkSamplingPopulation(markPlanId);
	}
}
