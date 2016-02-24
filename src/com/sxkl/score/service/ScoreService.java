package com.sxkl.score.service;

import java.util.List;

import com.sxkl.score.model.Score;

/**
 * 打分模块服务层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface ScoreService {

	/**
	 * 添加得分
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 * @param targetId 指标ID
	 * @param score 得分
	 * @return 操作结果
	 */
	public String addScore(String markPlanId, String personId, String targetId,double score);

	/**
	 * 获取指定人员得分集合
	 * @param personId 人员ID
	 * @return 指定人员得分集合
	 */
	public List<Score> getScoreByPersonId(String personId);

	/**
	 * 获取层级统计Z分数集合
	 * @param markPlanId 打分计划ID
	 * @param targetId 指标ID
	 * @param level 层级
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 层级统计Z分数集合
	 */
	public String getZFractionByLevelList(String markPlanId, String targetId, int level, int start, int limit);

	/**
	 * 获取指定指标的Z分数统计结果
	 * @param markPlanId 打分计划ID
	 * @param targetIds 指定指标ID数组
	 * @param targetId 显示的指标ID
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return
	 */
	public String getZFractionByUnLevelList(String markPlanId, String[] targetIds,String targetId, int start, int limit);

	/**
	 * 图表层级统计
	 * @param markPlanId 打分计划ID
	 * @param level 层级
	 * @return
	 */
	public String statisticsCharByLevel(String markPlanId, int level);

	public String test();

	public String levelTest();

}
