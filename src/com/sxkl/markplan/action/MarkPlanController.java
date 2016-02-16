package com.sxkl.markplan.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sxkl.markplan.service.MarkPlanService;

@Controller
@RequestMapping(value="/markplan")
public class MarkPlanController {

	@Autowired
	@Qualifier("markPlanServiceImpl")
	private MarkPlanService markPlanServiceImpl;
	
	@ResponseBody
	@RequestMapping(value = "/list.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getWorkMainPage(int start, int limit) {
		String result = this.markPlanServiceImpl.getListPage(start,limit);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/addMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String addMarkPlan(String name,double mark) {
		return this.markPlanServiceImpl.addMarkPlan(name,mark);
	}
	
	@ResponseBody
	@RequestMapping(value="/editMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String editMarkPlan(String id,String name,double mark) {
		return this.markPlanServiceImpl.editMarkPlan(id,name,mark);
	}
	
	@ResponseBody
	@RequestMapping(value="/delMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String delMarkPlan(String id) {
		return this.markPlanServiceImpl.delMarkPlan(id);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkMarkPlan.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String checkMarkPlan(String id,double mark) {
		return this.markPlanServiceImpl.checkMarkPlan(id,mark);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkMarkPlanForSetWeight.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String checkMarkPlanForSetWeight(String markPlanId) {
		return this.markPlanServiceImpl.checkMarkPlanForSetWeight(markPlanId);
	}
	
}
