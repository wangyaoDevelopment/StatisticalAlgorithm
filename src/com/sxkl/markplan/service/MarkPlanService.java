package com.sxkl.markplan.service;

public interface MarkPlanService {

	public String getListPage(int start, int limit);

	public String addMarkPlan(String name, double mark);

	public String editMarkPlan(String id, String name, double mark);

	public String delMarkPlan(String id);

	public String checkMarkPlan(String id, double mark);

	public String checkMarkPlanForSetWeight(String markPlanId);

	public String setMarkPlanPerson(String markPlanId, String[] personIds);

	public String selectedPersonList(String markPlanId);

	public String checkSamplingPopulation(String markPlanId);

}
