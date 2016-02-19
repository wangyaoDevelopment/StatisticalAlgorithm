package com.sxkl.markplan.service;

/**
 * 打分计划服务层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface MarkPlanService {

	/**
	 * 分页查询打分计划
	 * @param start 起始条目
	 * @param limit 每页条目数
	 * @return 打分计划集合
	 */
	public String getListPage(int start, int limit);

	/**
	 * 添加打分计划
	 * @param name 打分计划名称
	 * @param mark 打分计划分制
	 * @return 操作结果
	 */
	public String addMarkPlan(String name, double mark);

	/**
	 * 更新打分计划
	 * @param id 打分计划ID
	 * @param name 打分计划名称
	 * @param mark 打分计划分制
	 * @return 操作结果
	 */
	public String editMarkPlan(String id, String name, double mark);

	/**
	 * 删除打分计划
	 * @param id 打分计划ID
	 * @return 操作结果
	 */
	public String delMarkPlan(String id);

	/**
	 * 检查该打分计划是否已经打分
	 * @param id 打分计划ID
	 * @param mark 分制
	 * @return 检查结果 0是未打分  1是已经打分  
	 */
	public String checkMarkPlan(String id, double mark);

	/**
	 * 设置权重前检查该打分计划是否打分
	 * @param markPlanId 打分计划ID
	 * @return 检查结果 0是未打分  1是已经打分 
	 */
	public String checkMarkPlanForSetWeight(String markPlanId);

	/**
	 * 为打分计划添加参与人员
	 * @param markPlanId 打分计划ID
	 * @param personIds 参与人员ID
	 * @return 操作结果
	 */
	public String setMarkPlanPerson(String markPlanId, String[] personIds);

	/**
	 * 获取该打分计划参与人员的集合
	 * @param markPlanId 打分计划ID
	 * @return 该打分计划参与人员的集合
	 */
	public String selectedPersonList(String markPlanId);

	/**
	 * 检查该打分计划是否已选择参与人员
	 * @param markPlanId 打分计划ID
	 * @return 检查结果 0是未选择  1是选择
	 */
	public String checkSamplingPopulation(String markPlanId);

}
