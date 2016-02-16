package com.sxkl.markplan.service.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sxkl.common.utils.IDUtils;
import com.sxkl.markplan.dao.MarkPlanDao;
import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.markplan.service.MarkPlanService;
import com.sxkl.score.dao.ScoreDao;
import com.sxkl.score.model.Score;

@Service("markPlanServiceImpl")
public class MarkPlanServiceImpl implements MarkPlanService{
	
	@Autowired
	@Qualifier("markPlanDaoImpl")
	private MarkPlanDao markPlanDaoImpl;
	
	@Autowired
	@Qualifier("scoreDaoImpl")
	private ScoreDao scoreDaoImpl;

	public String getListPage(int start, int limit) {
		JSONObject json = new JSONObject();
		try {
			List<MarkPlan> markPlans = markPlanDaoImpl.getListPage(start,limit);
			int markPlansNum = markPlanDaoImpl.getListPageNum();
			JSONArray data = JSONArray.fromObject(markPlans);
			json.put("data", data);
			json.put("total", markPlansNum);
			json.put("start", start);
			json.put("limit", limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public String addMarkPlan(String name,double mark) {
		try {
			MarkPlan mp = new MarkPlan(IDUtils.getUUID(),name,mark);
			this.markPlanDaoImpl.addMarkPlan(mp);
		} catch (Exception e) {
			return "添加打分计划失败";
		}
		return "添加打分计划成功";
	}

	public String editMarkPlan(String id, String name,double mark) {
		try {
			MarkPlan mp = markPlanDaoImpl.getMarkPlanById(id);
			mp.setName(name);
			mp.setMark(mark);
			this.markPlanDaoImpl.updateMarkPlan(mp);
		} catch (Exception e) {
			return "修改打分计划失败";
		}
		return "修改打分计划成功";
	}

	public String delMarkPlan(String id) {
		try {
			List<Score> scores = scoreDaoImpl.checkMarkByMarkPlanId(id);
			if(scores != null && scores.size() > 0){
				return "该计划已打分,无法删除";
			}
			MarkPlan mp = this.markPlanDaoImpl.getMarkPlanById(id);
			this.markPlanDaoImpl.delMarkPlan(mp);
		} catch (Exception e) {
			return "删除打分计划失败";
		}
		return "删除打分计划成功";
	}

	public String checkMarkPlan(String markPlanId,double mark) {
		List<Score> scores = scoreDaoImpl.checkMarkByMarkPlanId(markPlanId);
		if(scores != null && scores.size() > 0){
			return "1";
		}
		MarkPlan mp = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
		if(mp.getMark() == mark){
			return "0";
		}else{
			return "1";
		}
	}

	public String checkMarkPlanForSetWeight(String markPlanId) {
		List<Score> scores = scoreDaoImpl.checkMarkByMarkPlanId(markPlanId);
		if(scores != null && scores.size() > 0){
			return "1";
		}
		return "0";
	}

}
