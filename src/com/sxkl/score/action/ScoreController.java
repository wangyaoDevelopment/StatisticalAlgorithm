package com.sxkl.score.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.score.service.ScoreService;

/**
 * 打分模块控制器
 * @author wangyao
 * @date 2016-02-19
 */
@Controller
@RequestMapping(value="/score")
public class ScoreController {

	@Autowired
	@Qualifier("scoreServiceImpl")
	private ScoreService scoreServiceImpl;
	private Logger logger = LoggerFactory.getLogger(ScoreController.class);
	
	/**
	 * 添加得分
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 * @param targetId 指标ID
	 * @param score 得分
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value = "/addScore.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String addScore(String markPlanId,String personId,String targetId,double score) {
		return this.scoreServiceImpl.addScore(markPlanId,personId,targetId,score);
	}
	
	/**
	 * 层级统计Z分数
	 * @param markPlanId 打分计划ID
	 * @param targetId 指标ID
	 * @param level 层级
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return Z分数统计结果
	 */
	@ResponseBody
	@RequestMapping(value = "/zFractionByLevelList.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getZFractionByLevelList(String markPlanId,String targetId,int level,int start, int limit) {
		return this.scoreServiceImpl.getZFractionByLevelList(markPlanId,targetId,level,start,limit);
	}
	
	/**
	 * 指定指标Z分数统计
	 * @param markPlanId 打分计划ID
	 * @param targetIds 指定指标ID数组
	 * @param targetId 要显示Z分数的特定指标ID
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 指定指标Z分数统计结果
	 */
	@ResponseBody
	@RequestMapping(value = "/zFractionByUnLevelList.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String zFractionByUnLevelList(String markPlanId,String[] targetIds,String targetId,int start, int limit) {
		return this.scoreServiceImpl.getZFractionByUnLevelList(markPlanId,targetIds,targetId,start,limit);
	}
	
	/**
	 * 跳转到层级图表统计界面
	 * @param markPlanId 打分计划ID
	 * @return 层级图表统计界面
	 */
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
	
	/**
	 * 图表层级统计
	 * @param markPlanId 打分计划ID
	 * @param level 层级
	 * @return 图表层级统计结果
	 */
	@ResponseBody
	@RequestMapping(value = "/statisticsCharByLevel.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String statisticsCharByLevel(String markPlanId,int level) {
		return this.scoreServiceImpl.statisticsCharByLevel(markPlanId,level);
	}
	
	@ResponseBody
	@RequestMapping(value="/test")
	public String test(){
		return this.scoreServiceImpl.test();
		
	}
}
