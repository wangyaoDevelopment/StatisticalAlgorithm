package com.sxkl.target.action;

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

import com.sxkl.target.model.Target;
import com.sxkl.target.service.TargetService;

/**
 * 指标控制器
 * @author wangyao
 * @date 2016-02-19
 */
@Controller
@RequestMapping("/target")
public class TargetController {
	
	@Autowired
	@Qualifier("targetServiceImpl")
	private TargetService targetServiceImpl;
	private Logger logger = LoggerFactory.getLogger(TargetController.class);
	
	/**
	 * 跳转到指标列表界面	
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toList.do")
	public ModelAndView toList()throws Exception{
		ModelAndView mv = new ModelAndView();
		try{
			List<Target> targetList = targetServiceImpl.listAllTarget();
			mv.addObject("targetList", targetList);
			mv.setViewName("target_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 获取指标数
	 * @return 指标数
	 */
	@ResponseBody
	@RequestMapping(value="/tree.do")
	public Target tree(){
		return targetServiceImpl.getTargetTree();
	}
	
	/**
	 * 添加指标
	 * @param text 指标名称
	 * @param id 指标ID
	 * @param type 添加指标类别
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public String addTarget(String text,String id,String type){
		Target target = new Target(text);
		return targetServiceImpl.addTarget(target,id,type);
	}
	
	/**
	 * 删除指标
	 * @param id 指标ID
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/delete.do",method=RequestMethod.POST)
	public String deleteTarget(String id){
		return targetServiceImpl.deleteTarget(id);
	}
	
	/**
	 * 更新指标 
	 * @param text 指标名称
	 * @param id 指标ID
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public String editTarget(String text,String id){
		Target target = new Target(text);
		return targetServiceImpl.editTarget(target,id);
	}
	
	/**
	 * 获取打分界面指标数
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 * @return 打分界面指标数
	 */
	@ResponseBody
	@RequestMapping(value="/scoreTree.do")
	public Target scoreTree(String markPlanId,String personId){
		return targetServiceImpl.getScoreTargetTree(markPlanId,personId);
	}
	
	/**
	 * 获取设置权重界面指标数
	 * @param markPlanId 打分计划ID
	 * @param mark 分制
	 * @return 权重界面指标数
	 */
	@ResponseBody
	@RequestMapping(value="/weightTree.do")
	public Target weightTree(String markPlanId,double mark){
		return targetServiceImpl.getWeightTargetTree(markPlanId,mark);
	}
	
	/**
	 * 设置指标权重
	 * @param targetId 指标ID
	 * @param markPlanId 打分计划ID
	 * @param weight 权重
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/setTargetWeight.do")
	public String setTargetWeight(String targetId,String markPlanId,double weight){
		return targetServiceImpl.setTargetWeight(targetId,markPlanId,weight);
	}
	
	/**
	 * 检查打分计划是否已设置权重
	 * @param markPlanId 打分计划ID
	 * @return 操作结果 0是未设置权重 1是已设置 2是该计划已打分 
	 */
	@ResponseBody
	@RequestMapping(value="/checkMarkPlanWeight.do")
	public String checkMarkPlanWeight(String markPlanId){
		return targetServiceImpl.checkMarkPlanWeight(markPlanId);
	}
	
	/**
	 * 获取根指标集合
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 根指标json格式
	 */
	@ResponseBody
	@RequestMapping(value="/getRootTargetList.do")
	public String getRootTargetList(int start, int limit){
		return targetServiceImpl.getRootTargetList(start,limit);
	}
	
	/**
	 * 检查打分计划参与人是否已设置最大分值
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 */
	@ResponseBody
	@RequestMapping(value="/checkTopScore.do",method=RequestMethod.POST)
	public void checkTopScore(String markPlanId,String personId){
		targetServiceImpl.checkTopScore(markPlanId,personId);
	}
	
	/**
	 * 分页查询根指标
	 * @param targetIds 指标ID数组
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 根指标json格式
	 */
	@ResponseBody
	@RequestMapping(value="/getRootTargetListByIds.do",method=RequestMethod.GET)
	public String getRootTargetListByIds(String[] targetIds,int start, int limit){
	    return targetServiceImpl.getTargetsPageByIds(targetIds,start,limit);
	}
	
	/**
	 * 获取指标层级下拉框数据
	 * @return 指标层级下拉框数据
	 */
	@ResponseBody
	@RequestMapping(value="/getTargetLevelComboBox.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getTargetLevelComboBox(){
	    return targetServiceImpl.getTargetLevelComboBox();
	}
	
	
}
