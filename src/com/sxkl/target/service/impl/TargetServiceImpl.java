package com.sxkl.target.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sxkl.common.utils.IDUtils;
import com.sxkl.markplan.dao.MarkPlanDao;
import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.person.dao.PersonDao;
import com.sxkl.person.model.Person;
import com.sxkl.score.dao.ScoreDao;
import com.sxkl.score.model.Score;
import com.sxkl.target.dao.TargetDao;
import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TopScore;
import com.sxkl.target.service.TargetService;

@Service("targetServiceImpl")
public class TargetServiceImpl implements TargetService{

	@Autowired
	@Qualifier("targetDaoImpl")
	private TargetDao targetDaoImpl;
	
	@Autowired
	@Qualifier("scoreDaoImpl")
	private ScoreDao scoreDaoImpl;
	
	@Autowired
	@Qualifier("markPlanDaoImpl")
	private MarkPlanDao markPlanDaoImpl;
	
	@Autowired
	@Qualifier("personDaoImpl")
	private PersonDao personDaoImpl;
	
	public List<Target> listAllTarget() {
		List<Target> targets = targetDaoImpl.getAllTarget();
		if(targets == null || targets.isEmpty()){
			targets = new ArrayList<Target>();
		}
		return targets;
	}

	public Target getTargetTree() {
		Target target = targetDaoImpl.getTargetTreeByRootNode();
//		return recursiveTree(target.getId());
		return target;
	}
	
	public Target recursiveTree(String id) {
		Target node = targetDaoImpl.getTargetById(id);
		List<Target> childNodes = targetDaoImpl.getChildTargetById(id); 
		//遍历子节点
		for(Target child : childNodes){
			Target target = recursiveTree(child.getId()); //递归
			node.getChildren().add(target);
		}
		return node;
	}

	public String setTargetWeight(String id, double weight) {
		try {
			Target target = targetDaoImpl.getTargetById(id);
			if(target == null){
				return "查无此节点";
			}
			target.setWeight(weight);
			//target.setTopScore(target.getParent().getTopScore()*weight);
			targetDaoImpl.updateTarget(target);
		} catch (Exception e) {
			return "设置节点权重失败";
		}
		return "设置节点权重成功";
	}

	public String addChildrenTarget(Target children, String parentId) {
		try {
			Target parent = targetDaoImpl.getTargetById(parentId);
			children.setId(IDUtils.getUUID());
			children.setParent(parent);
			children.setLevel(parent.getLevel()+1);
			if(parent.getRoot() == null){
				children.setRoot(parent);
			}else{
				children.setRoot(parent.getRoot());
			}
			parent.getChildren().add(children);
			targetDaoImpl.updateTarget(parent);
		} catch (Exception e) {
			e.printStackTrace();
			return "添加子节点失败";
		}
		return "添加子节点成功";
	}

	public String deleteTargetById(String id) {
		try {
			Target target = targetDaoImpl.getTargetById(id);
			target.getParent().getChildren().remove(target);
			target.setParent(null);
			targetDaoImpl.deleteTarget(target);
		} catch (Exception e) {
			return "删除节点失败";
		}
		return "删除节点成功";
	}

	public String addTarget(Target target, String id, String type) {
		// type 0  兄弟分类   1 子分类
		try {
			if(type.equals("0")){
				this.addBrotherTarget(target,id);
			}else if(type.equals("1")){
				this.addChildrenTarget(target, id);
			}
		} catch (Exception e) {
			return "添加结点失败";
		}
		return "添加结点成功";
	}

	private void addBrotherTarget(Target target, String id) throws Exception{
		Target brother = this.targetDaoImpl.getTargetById(id);
		Target parent = brother.getParent();
		target.setId(IDUtils.getUUID());
		target.setParent(parent);
		target.setLevel(brother.getLevel());
		target.setRoot(parent.getRoot());
		if(brother.getRoot() == null){
			target.setRoot(null);
		}else{
			target.setRoot(brother.getRoot());
		}
		parent.getChildren().add(target);
		targetDaoImpl.updateTarget(parent);
	}

	public String deleteTarget(String id) {
		try {
			Target target = targetDaoImpl.getTargetById(id);
			target.getParent().getChildren().remove(target);
			target.setParent(null);
			this.targetDaoImpl.deleteTarget(target);
		} catch (Exception e) {
			return "删除指标失败";
		}
		return "删除指标成功";
	}

	public String editTarget(Target temp, String id) {
		try {
			Target target = targetDaoImpl.getTargetById(id);
			target.setText(temp.getText());
			target.setWeight(temp.getWeight());
			//double topScore = target.getParent().getTopScore()*temp.getWeight();
			//target.setTopScore(topScore);
			//recursiveSetTopScore(target.getId(),topScore);
			this.targetDaoImpl.updateTarget(target);
		} catch (Exception e) {
			return "设置权重失败";
		}
		return "设置权重成功";
	}

	public Target recursiveSetTopScore(String targetId,double mark,List<Score> scores,List<TargetData> targetDatas) {
		Target node = targetDaoImpl.getTargetById(targetId);
		List<Target> childNodes = targetDaoImpl.getChildTargetById(targetId); 
		for(Target child : childNodes){
			Target target = recursiveSetTopScore(child.getId(),child.getWeight()*mark,scores,targetDatas);
			for(TargetData targetData : targetDatas){
				if(targetData.getTarget().getId().equals(target.getId())){
					targetData.setTopScore(mark);
				}
			}
			target.setTopScore(mark*target.getWeight());
			node.getChildren().add(target);
		}
		return node;
	}
	
	public Target recursiveSetTopScore(String targetId, double score,MarkPlan markPlan, Person person, List<TopScore> topScores){
		Target node = targetDaoImpl.getTargetById(targetId);
		List<Target> childNodes = targetDaoImpl.getChildTargetById(targetId); 
		for(Target child : childNodes){
			Target target = recursiveSetTopScore(child.getId(),score,markPlan,person,topScores);
			boolean flag = false;
			for(TopScore topScore : topScores){
				if(topScore.getTarget().getId().equals(target.getId())){
					flag = true;
					topScore.setTopScore(score);
				}
			}
			if(!flag){
				TopScore topScore = new TopScore();
				topScore.setId(IDUtils.getUUID());
				topScore.setMarkPlan(markPlan);
				topScore.setPerson(person);
				topScore.setTarget(target);
				topScore.setTopScore(score);
				this.targetDaoImpl.addTopScore(topScore);
			}
//			target.setTopScore(mark*target.getWeight());
			node.getChildren().add(target);
		}
		return node;
	}

	public Target getScoreTargetTree(String markPlanId, String personId) {
		Target target = targetDaoImpl.getTargetTreeByRootNode();
//		List<Score> scores = scoreDaoImpl.getScoreByPersonId(personId);
		List<Score> scores = scoreDaoImpl.getScoreByPersonAndMarkPlan(personId, markPlanId);
		List<TargetData> targetDatas = this.targetDaoImpl.getTargetDatasByMarkPlanId(markPlanId);
		List<TopScore> topScores = this.targetDaoImpl.getTopScoreByMarkPlanIdAndPersonId(markPlanId,personId);
		recursiveSetScore(target.getId(),scores,targetDatas,topScores);
		return target;
	}
	
	private Target recursiveSetScore(String id,List<Score> scores, List<TargetData> targetDatas, List<TopScore> topScores) {
		Target node = targetDaoImpl.getTargetById(id);
		List<Target> childNodes = targetDaoImpl.getChildTargetById(id); 
		for(Target child : childNodes){
			Target target = recursiveSetScore(child.getId(),scores,targetDatas,topScores);
			for(TargetData targetData : targetDatas){
				if(targetData.getTarget().getId().equals(target.getId())){
					target.setWeight(targetData.getWeight());
					//target.setTopScore(targetData.getTopScore());
				}
			}
			for(TopScore topScore : topScores){
				if(topScore.getTarget().getId().equals(target.getId())){
					target.setTopScore(topScore.getTopScore());
				}
			}
			for(Score score : scores){
				if(score.getTarget().getId().equals(target.getId())){
					target.setScore(score.getScore());
				}
			}
			node.getChildren().add(target);
		}
		return node;
	}

	public String checkMarkByMarkPlanId(String markPlanId) {
		List<Score> scores = this.scoreDaoImpl.checkMarkByMarkPlanId(markPlanId);
		if(scores != null && scores.size() > 0){
			return "1";
		}
		return "0";
	}

	public Target getWeightTargetTree(String markPlanId,double mark) {
		Target root = this.targetDaoImpl.getRootTarget();
		MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
		List<TargetData> targetDatas = this.targetDaoImpl.getTargetDatasByMarkPlanId(markPlanId);
		recursiveSetTargetWeight(root.getId(),markPlan,mark,targetDatas);
		return root;
	}

	private Target recursiveSetTargetWeight(String id,MarkPlan markPlan,double mark,List<TargetData> targetDatas) {
		Target node = targetDaoImpl.getTargetById(id);
		List<Target> childNodes = targetDaoImpl.getChildTargetById(id); 
		for(Target child : childNodes){
			Target target = recursiveSetTargetWeight(child.getId(),markPlan,mark,targetDatas);
			boolean flag = false;
			for(TargetData targetData : targetDatas){
				if(targetData.getTarget().getId().equals(target.getId())){
					target.setWeight(targetData.getWeight());
					target.setTopScore(targetData.getTopScore());
					flag = true;
				}
			}
			if(!flag){
				double weight = 1;
				if(target.getRoot() != null){
					weight = (1.0/(child.getBrothers(child).size()));
				}
				TargetData data = new TargetData();
				data.setId(IDUtils.getUUID());
				data.setMarkPlan(markPlan);
				data.setTarget(child);
				data.setTopScore(mark);
				data.setWeight(weight);
				targetDaoImpl.addTargetData(data);
				target.setWeight(weight);
				target.setTopScore(mark);
			}
			node.getChildren().add(target);
		}
		return node;
	}

	public String setTargetWeight(String targetId, String markPlanId,double weight) {
		try {
			MarkPlan mp = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
			Target target = this.targetDaoImpl.getTargetById(targetId);
			TargetData targetData = this.targetDaoImpl.getTargetDatasByTargetIdAndMarkPlanId(targetId,markPlanId);
			if(targetData == null){
				targetData = new TargetData();
				targetData.setId(IDUtils.getUUID());
				targetData.setMarkPlan(mp);
				targetData.setTarget(target);
				targetData.setWeight(weight);
				targetData.setTopScore(mp.getMark());
				this.targetDaoImpl.addTargetData(targetData);
			}else{
				targetData.setMarkPlan(mp);
				targetData.setTarget(target);
				targetData.setWeight(weight);
				targetData.setTopScore(mp.getMark());
				this.targetDaoImpl.updateTargetData(targetData);
			}
		} catch (Exception e) {
			return "设置权重失败";
		}
		return "设置权重成功";
	}

	public String checkMarkPlanWeight(String markPlanId) {
		List<Score> scores = this.scoreDaoImpl.checkMarkByMarkPlanId(markPlanId);
		if(scores != null && scores.size() > 0){
			return "2";
		}
		List<TargetData> targetDatas = this.targetDaoImpl.getTargetDatasByMarkPlanId(markPlanId);
		if(targetDatas.size() > 0){
			return "1";
		}
		return "0";
	}

	public String getRootTargetList(int start, int limit) {
		JSONObject json = new JSONObject();
		try {
			List<Target> targets = targetDaoImpl.getRootTargetListPage(start,limit);
			int targetsNum = targetDaoImpl.getRootTargetListPageNum();
			JsonConfig config = new JsonConfig();
			config.setExcludes(new String[]{"root","parent","children","weight","topScore","score"});
			JSONArray data = JSONArray.fromObject(targets,config);
			json.put("data", data);
			json.put("total", targetsNum);
			json.put("start", start);
			json.put("limit", limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public void checkTopScore(String markPlanId, String personId) {
		Target target = this.targetDaoImpl.getRootTarget();
		MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
		Person person = this.personDaoImpl.getPersonById(personId);
		List<TopScore> topScores = this.targetDaoImpl.getTopScoreByMarkPlanIdAndPersonId(markPlanId, personId);
		if(topScores.size() > 0){
			return;
		}
		this.recursiveSetTopScore(target.getId(),markPlan.getMark(),markPlan,person,topScores);
	}

}
