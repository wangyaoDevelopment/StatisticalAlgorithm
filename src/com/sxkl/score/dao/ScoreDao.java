package com.sxkl.score.dao;

import java.util.List;

import com.sxkl.score.model.Score;

/**
 * 打分模块持久层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface ScoreDao {

	/**
	 * 获取特定打分计划特定人的得分集合
	 * @param personId  人员ID
	 * @param markPlanId 打分计划ID
	 * @return 得分集合
	 */
	public List<Score> getScoreByPersonAndMarkPlan(String personId, String markPlanId);

	/**
	 * 添加得分
	 * @param scoreBean 待添加得分
	 */
	public void addScore(Score scoreBean);

	/**
	 * 根据条件查询得分
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 * @param targetId 指标ID
	 * @return 得分
	 */
	public Score getScoreByParam(String markPlanId, String personId,String targetId);

	/**
	 * 检查打分计划分制
	 * @param markPlanId 打分计划ID
	 * @return 得分集合
	 */
	public List<Score> checkMarkByMarkPlanId(String markPlanId);

	/**
	 * 获取特定人的得分集合
	 * @param personId 人员ID
	 * @return 得分集合
	 */
	public List<Score> getScoreByPersonId(String personId);

	/**
	 * 更新得分
	 * @param score 待更新得分
	 */
	public void updateScore(Score score);

	public List<Score> getScoreByMarkPlanId(String string);

}
