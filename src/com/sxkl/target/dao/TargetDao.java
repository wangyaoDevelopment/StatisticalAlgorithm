package com.sxkl.target.dao;

import java.util.List;

import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TopScore;

public interface TargetDao {

	public List<Target> getAllTarget();

	public Target getTargetTreeByRootNode();

	public List<Target> getTargetsByLevel(int level);

	public Target getRootTarget();

	public List<Target> getAllLeafTarget();

	public Target getTargetById(String id);

	public void updateTarget(Target target);

	public void deleteTarget(Target target);

	public List<Target> getChildTargetById(String id);

	public List<TargetData> getTargetDatasByMarkPlanId(String markPlanId);

	public void addTargetData(TargetData data);

	public TargetData getTargetDatasByTargetIdAndMarkPlanId(String targetId,String markPlanId);

	public void updateTargetData(TargetData targetData);

	public List<Target> getRootTargetListPage(int start, int limit);

	public int getRootTargetListPageNum();

	public List<Target> getTargetsByIds(String[] targetIds);

	public List<TopScore> getTopScoreByMarkPlanIdAndPersonId(String markPlanId,String personId);

	public void addTopScore(TopScore topScore);

}
