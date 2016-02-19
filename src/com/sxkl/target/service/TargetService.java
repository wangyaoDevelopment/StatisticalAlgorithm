package com.sxkl.target.service;

import java.util.List;

import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.person.model.Person;
import com.sxkl.score.model.Score;
import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TopScore;

/**
 * 指标服务层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface TargetService {

	/**
	 * 获取全部指标集合
	 * @return 指标集合
	 */
	public List<Target> listAllTarget();

	/**
	 * 获取指标树
	 * @return 指标树
	 */
	public Target getTargetTree();
	
	/**
	 * 设置指标权重
	 * @param targetId 指标ID
	 * @param markPlanId 打分计划ID
	 * @param weight 权重
	 * @return 操作结果
	 */
	public String setTargetWeight(String targetId,String markPlanId, double weight);
	
	/**
	 * 添加子指标
	 * @param children 待添加子指标
	 * @param parentId 父指标ID
	 * @return 操作结果
	 */
	public String addChildrenTarget(Target children,String parentId);
	
	/**
	 * 根据ID删除指标
	 * @param id 指标ID
	 * @return 操作结果
	 */
	public String deleteTargetById(String id);

	/**
	 * 添加指标
	 * @param target 待添加指标
	 * @param id 指标ID
	 * @param type 添加类型
	 * @return 操作结果
	 */
	public String addTarget(Target target, String id, String type);

	/**
	 * 删除指标
	 * @param id 指标ID
	 * @return 操作结果
	 */
	public String deleteTarget(String id);

	/**
	 * 更新指标
	 * @param target 待更新指标
	 * @param id 指标ID
	 * @return 操作结果
	 */
	public String editTarget(Target target, String id);
    
	/**
	 * 获取打分界面指标树
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 * @return 操作结果
	 */
	public Target getScoreTargetTree(String markPlanId, String personId);

	/**
	 * 递归设置指标最大分值
	 * @param id 指标ID
	 * @param score 得分
	 * @param scores 得分集合
	 * @param targetDatas 权重数据集合
	 * @return 指标
	 */
	public Target recursiveSetTopScore(String id, double score, List<Score> scores, List<TargetData> targetDatas);

	/**
	 * 获取设置权重界面指标树
	 * @param markPlanId 打分计划ID
	 * @param mark 权重
	 * @return 指标
	 */
	public Target getWeightTargetTree(String markPlanId, double mark);

	/**
	 * 检查打分计划是否已设置权重
	 * @param markPlanId 打分计划ID
	 * @return 操作结果 0是未设置权重 1是已设置 2是该计划已打分 
	 */
	public String checkMarkPlanWeight(String markPlanId);

	/**
	 * 获取根指标集合
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 根指标json格式
	 */
	public String getRootTargetList(int start, int limit);

	/**
	 * 递归设置指标最大分值
	 * @param id 指标ID
	 * @param score 得分
	 * @param markPlan 打分计划
	 * @param person 人员
	 * @param topScores 最大分值集合
	 * @return 指标
	 */
	public Target recursiveSetTopScore(String id, double score,MarkPlan markPlan, Person person, List<TopScore> topScores);

	/**
	 * 检查打分计划参与人是否已设置最大分值
	 * @param markPlanId 打分计划ID
	 * @param personId 人员ID
	 */
	public void checkTopScore(String markPlanId, String personId);

	/**
	 * 分页查询根指标
	 * @param targetIds 指标ID数组
	 * @param start 开始条目
	 * @param limit 每页条目数
	 * @return 根指标json格式
	 */
	public String getTargetsPageByIds(String[] targetIds, int start, int limit);

	/**
	 * 获取指标层级下拉框数据
	 * @return 指标层级下拉框数据
	 */
	public String getTargetLevelComboBox();

}
