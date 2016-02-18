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

@Controller
@RequestMapping("/target")
public class TargetController {
	
	@Autowired
	@Qualifier("targetServiceImpl")
	private TargetService targetServiceImpl;
	private Logger logger = LoggerFactory.getLogger(TargetController.class);
	
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
	
	@ResponseBody
	@RequestMapping(value="/tree.do")
	public Target tree(){
		return targetServiceImpl.getTargetTree();
	}
	
	@ResponseBody
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	public String addTarget(String text,String id,String type){
		Target target = new Target(text);
		return targetServiceImpl.addTarget(target,id,type);
	}
	
	@ResponseBody
	@RequestMapping(value="/delete.do",method=RequestMethod.POST)
	public String deleteTarget(String id){
		return targetServiceImpl.deleteTarget(id);
	}
	
	@ResponseBody
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	public String editTarget(String text,String id){
		Target target = new Target(text);
		return targetServiceImpl.editTarget(target,id);
	}
	
	@ResponseBody
	@RequestMapping(value="/scoreTree.do")
	public Target scoreTree(String markPlanId,String personId){
		return targetServiceImpl.getScoreTargetTree(markPlanId,personId);
	}
	
	@ResponseBody
	@RequestMapping(value="/weightTree.do")
	public Target weightTree(String markPlanId,double mark){
		return targetServiceImpl.getWeightTargetTree(markPlanId,mark);
	}
	
	@ResponseBody
	@RequestMapping(value="/setTargetWeight.do")
	public String setTargetWeight(String targetId,String markPlanId,double weight){
		return targetServiceImpl.setTargetWeight(targetId,markPlanId,weight);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkMarkPlanWeight.do")
	public String checkMarkPlanWeight(String markPlanId){
		return targetServiceImpl.checkMarkPlanWeight(markPlanId);
	}
	
	@ResponseBody
	@RequestMapping(value="/getRootTargetList.do")
	public String getRootTargetList(int start, int limit){
		return targetServiceImpl.getRootTargetList(start,limit);
	}
	
	@ResponseBody
	@RequestMapping(value="/checkTopScore.do",method=RequestMethod.POST)
	public void checkTopScore(String markPlanId,String personId){
		targetServiceImpl.checkTopScore(markPlanId,personId);
	}
	
	@ResponseBody
	@RequestMapping(value="/getRootTargetListByIds.do",method=RequestMethod.GET)
	public String getRootTargetListByIds(String[] targetIds,int start, int limit){
	    return targetServiceImpl.getTargetsPageByIds(targetIds,start,limit);
	}
	
}
