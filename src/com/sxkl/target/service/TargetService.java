package com.sxkl.target.service;

import java.util.List;

import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.person.model.Person;
import com.sxkl.score.model.Score;
import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TargetLevel;
import com.sxkl.target.model.TopScore;

public interface TargetService {

	public List<Target> listAllTarget();

	public Target getTargetTree();
	
	public String setTargetWeight(String targetId,String markPlanId, double weight);
	
	public String addChildrenTarget(Target children,String parentId);
	
	public String deleteTargetById(String id);

	public String addTarget(Target target, String id, String type);

	public String deleteTarget(String id);

	public String editTarget(Target target, String id);


	public Target getScoreTargetTree(String markPlanId, String personId);

	public Target recursiveSetTopScore(String id, double score, List<Score> scores, List<TargetData> targetDatas);

	public Target getWeightTargetTree(String markPlanId, double mark);

	public String checkMarkPlanWeight(String markPlanId);

	public String getRootTargetList(int start, int limit);

	public Target recursiveSetTopScore(String id, double score,MarkPlan markPlan, Person person, List<TopScore> topScores);

	public void checkTopScore(String markPlanId, String personId);

	public String getTargetsPageByIds(String[] targetIds, int start, int limit);

	public String getTargetLevelComboBox();

}
