package com.sxkl.markplan.dao;

import java.util.List;

import com.sxkl.markplan.model.MarkPlan;

public interface MarkPlanDao {

	public List<MarkPlan> getListPage(int start, int limit);

	public int getListPageNum();

	public MarkPlan getMarkPlanById(String id);

	public void updateMarkPlan(MarkPlan mp);

	public void addMarkPlan(MarkPlan mp);

	public void delMarkPlan(MarkPlan mp);

}
