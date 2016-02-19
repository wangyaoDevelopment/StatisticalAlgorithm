package com.sxkl.target.dao;

import java.util.List;

import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TopScore;

/**
 * 指标持久层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface TargetDao {

	/**
	 * 获取所有指标
	 * @return 指标集合
	 */
	public List<Target> getAllTarget();

	/**
	 * 根据根指标获取指标树
	 * @return 指标树
	 */
	public Target getTargetTreeByRootNode();

	/**
	 * 根据层级获取指标树
	 * @param level 层级
	 * @return 指标树
	 */
	public List<Target> getTargetsByLevel(int level);

	/**
	 * 获取根指标
	 * @return 根指标
	 */
	public Target getRootTarget();

	/**
	 * 获取所有指标集合
	 * @return 指标集合
	 */
	public List<Target> getAllLeafTarget();

	/**
	 * 根据指标ID获取指标
	 * @param id 指标ID
	 * @return 指标
	 */
	public Target getTargetById(String id);

	/**
	 * 更新指标
	 * @param target 待更新指标
	 */
	public void updateTarget(Target target);

	/**
	 * 删除指标
	 * @param target 待删除指标
	 */
	public void deleteTarget(Target target);

	/**
	 * 获取特定指标子指标
	 * @param id 指标ID
	 * @return 特定指标子指标集合
	 */
	public List<Target> getChildTargetById(String id);

	/**
	 * 根据打分计划ID获取权重数据
	 * @param markPlanId 打分计划ID
	 * @return 权重数据集合
	 */
	public List<TargetData> getTargetDatasByMarkPlanId(String markPlanId);

	/**
	 * 添加权重数据
	 * @param data 权重数据
	 */
	public void addTargetData(TargetData data);

	/**
	 * 获取特定打分计划某指标的权重
	 * @param targetId 指标ID
	 * @param markPlanId 打分计划ID
	 * @return 指标权重 
	 */
	public TargetData getTargetDatasByTargetIdAndMarkPlanId(String targetId,String markPlanId);

	/**
	 * 更新指标权重
	 * @param targetData 待更新指标权重数据
	 */
	public void updateTargetData(TargetData targetData);

	/**
	 * 分页查询根指标 
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 根指标
	 */
	public List<Target> getRootTargetListPage(int start, int limit);

	/**
	 * 获取根指标数目
	 * @return 根指标数目
	 */
	public int getRootTargetListPageNum();

	/**
	 * 获取特定指标集合
	 * @param targetIds 指标ID数组
	 * @return 特定指标集合
	 */
	public List<Target> getTargetsByIds(String[] targetIds);

	/**
	 * 获取打分计划参与人的最大分值集合
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 * @return 打分计划参与人的最大分值集合
	 */
	public List<TopScore> getTopScoreByMarkPlanIdAndPersonId(String markPlanId,String personId);

	/**
	 * 添加最大分值
	 * @param topScore 待添加最大分值
	 */
	public void addTopScore(TopScore topScore);

	/**
	 * 分页查询指标列表
	 * @param targetIds 特定指标ID数组
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 指标列表
	 */ 
	public List<Target> getTargetsPageByIds(String[] targetIds, int start,int limit);

	/**
	 * 获取指标最大层级
	 * @return 指标最大层级
	 */
	public int getTargetMaxLevel();

	/**
	 * 添加指标
	 * @param target 待添加指标
	 */
	public void addTarget(Target target);

}
