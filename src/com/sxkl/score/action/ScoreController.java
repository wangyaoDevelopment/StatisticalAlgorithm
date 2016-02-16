package com.sxkl.score.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.person.action.PersonController;
import com.sxkl.score.service.ScoreService;
import com.sxkl.target.model.Target;

@Controller
@RequestMapping(value="/score")
public class ScoreController {

	@Autowired
	@Qualifier("scoreServiceImpl")
	private ScoreService scoreServiceImpl;
	private Logger logger = LoggerFactory.getLogger(ScoreController.class);
	
	@ResponseBody
	@RequestMapping(value = "/addScore.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String addScore(String markPlanId,String personId,String targetId,double score) {
		return this.scoreServiceImpl.addScore(markPlanId,personId,targetId,score);
	}
	
	@ResponseBody
	@RequestMapping(value = "/zFractionByLevelList.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getZFractionByLevelList(String markPlanId,String targetId,int level,int start, int limit) {
		return this.scoreServiceImpl.getZFractionByLevelList(markPlanId,targetId,level,start,limit);
	}
	
	@ResponseBody
	@RequestMapping(value = "/zFractionByUnLevelList.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String zFractionByUnLevelList(String markPlanId,String[] targetIds,String targetId,int start, int limit) {
		return this.scoreServiceImpl.getZFractionByUnLevelList(markPlanId,targetIds,targetId,start,limit);
	}
	
	@RequestMapping(value = "/gotoStatisticsChartByLevel.do")
	public ModelAndView gotoStatisticsChartByLevel(String markPlanId) {
		ModelAndView mv = new ModelAndView();
		try{
//			List<Target> targetList = targetServiceImpl.listAllTarget();
			mv.addObject("markPlanId", markPlanId);
			mv.setViewName("statistics_unLevel");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/statisticsCharByLevel.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String statisticsCharByLevel(String markPlanId,int level) {
		return this.scoreServiceImpl.statisticsCharByLevel(markPlanId,level);
	}
}
