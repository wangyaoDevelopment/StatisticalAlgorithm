package com.sxkl.markplan.dao;

import java.util.List;

import com.sxkl.markplan.model.MarkPlan;

/**
 * 打分计划持久层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface MarkPlanDao {

	/**
	 * 分页查询打分计划
	 * @param start 起始条目
	 * @param limit 每页条目数
	 * @return 打分计划集合
	 */
	public List<MarkPlan> getListPage(int start, int limit);

	/**
	 * 获取打分计划条目总数
	 * @return 打分计划条目总数
	 */
	public int getListPageNum();

	/**
	 * 根据打分计划ID获取打分计划
	 * @param id 打分计划ID
	 * @return 打分计划
	 */
	public MarkPlan getMarkPlanById(String id);

	/**
	 * 更新打分计划
	 * @param mp 待更新打分计划
	 */
	public void updateMarkPlan(MarkPlan mp);

	/**
	 * 添加打分计划
	 * @param mp 待添加打分计划
	 */
	public void addMarkPlan(MarkPlan mp);

	/**
	 * 删除打分计划
	 * @param mp 待删除打分计划
	 */
	public void delMarkPlan(MarkPlan mp);

}
