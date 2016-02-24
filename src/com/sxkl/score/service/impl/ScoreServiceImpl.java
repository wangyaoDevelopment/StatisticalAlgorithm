package com.sxkl.score.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kingbase.common.utils.StatisticalAlgorithmUtils;
import com.sxkl.common.utils.IDUtils;
import com.sxkl.markplan.dao.MarkPlanDao;
import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.person.dao.PersonDao;
import com.sxkl.person.model.Person;
import com.sxkl.score.dao.ScoreDao;
import com.sxkl.score.model.Score;
import com.sxkl.score.model.StatisticsDataBean;
import com.sxkl.score.service.ScoreService;
import com.sxkl.target.dao.TargetDao;
import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TopScore;
import com.sxkl.target.service.TargetService;

@Service("scoreServiceImpl")
public class ScoreServiceImpl implements ScoreService{
	
	@Autowired
	@Qualifier("targetServiceImpl")
	private TargetService targetServiceImpl;

	@Autowired
	@Qualifier("scoreDaoImpl")
	private ScoreDao scoreDaoImpl;
	
	@Autowired
	@Qualifier("targetDaoImpl")
	private TargetDao targetDaoImpl;
	
	@Autowired
	@Qualifier("personDaoImpl")
	private PersonDao personDaoImpl;
	
	@Autowired
	@Qualifier("markPlanDaoImpl")
	private MarkPlanDao markPlanDaoImpl;
	
	private Map<Target, Map<Person, Double>> getZFractionByLevel(List<Person> persons,String markPlanId, int level, List<Target> rootTargets){
		Map<Target, Map<Person, Double>> map = new HashMap<Target, Map<Person, Double>>();
		for(Target rootTarget : rootTargets){
			Map<Person,Double> scores = new HashMap<Person,Double>();
			for(Person person : persons){
				double temp = getPersonTargetScoreByLevel(person,markPlanId,level,rootTarget);
				scores.put(person, temp);
			}
			map.put(rootTarget, calculateZFraction(scores,persons));
		}
		return map;
	}
	
	private Map<Target, Map<Person, Double>> getZFractionByUnLevel(List<Person> persons,String markPlanId,List<Target> targets){
		Map<Target, Map<Person, Double>> map = new HashMap<Target, Map<Person, Double>>();
		for(Target target : targets){
			Map<Person,Double> scores = new HashMap<Person,Double>();
			for(Person person : persons){
				double temp = getPersonTargetScoreByUnLevel(person,markPlanId,target);
				scores.put(person, temp);
			}
			map.put(target, calculateZFraction(scores,persons));
		}
		return map;
	}
	
	private double getPersonTargetScoreByUnLevel(Person person,String markPlanId, Target target) {
		List<Score> scores = scoreDaoImpl.getScoreByPersonAndMarkPlan(person.getId(),markPlanId);
		double sum = 0;
		for(Score score : scores){
			if(score.getTarget().getId().equals(target.getId())){
				sum += score.getScore();
			}
		}
		return sum;
	}

	private Map<Person, Double> calculateZFraction(Map<Person, Double> scores, List<Person> persons) {
		double sum = 0;//所有人总分
		for (Map.Entry<Person, Double> entry : scores.entrySet()) {
			sum += entry.getValue();
	    }
		//求所有人平均分
		double avg = sum/persons.size();
		//求平均方差
		double averageVariance = getAverageVariance(avg,scores,persons);
		//求平均方差平方根
		double averageVarianceSqrt = Math.sqrt(averageVariance);
		//求标准方差
		Map<Person,Double> standardVariance = getStandardVariance(scores,avg,averageVarianceSqrt,persons);
		//求归一化分数
		Map<Person,Double> normalizedScore = getNormalizedScore(persons,standardVariance);
		return normalizedScore;
	}

	/**
	 * 求归一化分数
	 * @param persons 采样人群
	 * @param standardVariance 标准方差
	 * @return 归一化分数
	 */
	private Map<Person, Double> getNormalizedScore(List<Person> persons,Map<Person, Double> standardVariance) {
		double ymax = 100;
		double ymin = 10;
		Collection<Double> datas = standardVariance.values();
		double xmax = Collections.max(datas);
		double xmin = Collections.min(datas);
		Map<Person,Double> result = new HashMap<Person,Double>();
		for(Person person : persons){
			double data = standardVariance.get(person);
			double value = ymin + ((ymax-ymin)/(xmax-xmin))*(data-xmin);
			result.put(person, value);
		}
		return result;
	}

	private Map<Person, Double> getStandardVariance(Map<Person, Double> scores,double avg, double averageVarianceSqrt, List<Person> persons) {
		Map<Person,Double> standardVariance = new HashMap<Person,Double>();
		for(Person person : persons){
			standardVariance.put(person,((scores.get(person) - avg)/averageVarianceSqrt));
		}
		return standardVariance;
	}

	private double getAverageVariance(double avg,Map<Person, Double> scores, List<Person> persons) {
		double sum = 0;
		for(Person person : persons){
			double avgV = Math.pow((scores.get(person) - avg),2);
			sum += avgV;
		}
		return sum/persons.size();
	}
	
	/**
	 * 统计指定指标单人的平均得分
	 * @param person
	 * @param markPlanId
	 * @param targets
	 * @return
	 */
	private double getPersonScoreUnLevel(Person person,String markPlanId,List<Target> targets){
		List<Score> scores = scoreDaoImpl.getScoreByPersonAndMarkPlan(person.getId(),markPlanId);
		if(targets.size() == 0){
			return 0;
		}
		double sum = 0;
		for(Target target : targets){
			for(Score score : scores){
				if(score.getTarget().getId().equals(target.getId())){
					sum += score.getScore();
				}
			}
		}
		return sum/targets.size();
	}

	/**
	 * 统计层次结构单人某指标的得分
	 * @param person
	 * @param markPlanId
	 * @param level 层级
	 * @param rootTarget 
	 * @return
	 */
	private double getPersonTargetScoreByLevel(Person person,String markPlanId, int level, Target rootTarget){
		List<Score> scores = scoreDaoImpl.getScoreByPersonAndMarkPlan(person.getId(),markPlanId);
		List<Target> levelTargets = targetDaoImpl.getTargetsByLevel(level);//指定层级子节点
		//计算指定层级分数
		double theLevelSum = getTheLevelScore(levelTargets, scores,markPlanId,rootTarget);
		//计算未包含在指定层级的分支分数
		List<Target> targets = new ArrayList<Target>();
		for(int i = level-1; i > 0; i--){
			List<Target> temps = targetDaoImpl.getTargetsByLevel(i);
			for(Target temp : temps){
				if(temp.getChildren() == null || temp.getChildren().size() == 0){
					targets.add(temp);
				}
			}
		}
		double unContaintLevelSum = getTheLevelScore(targets, scores,markPlanId,rootTarget);
		return theLevelSum + unContaintLevelSum;
	}
	
	private double getTheLevelScore(List<Target> targets,List<Score> scores, String markPlanId, Target rootTarget){
		List<TargetData> targetDatas = this.targetDaoImpl.getTargetDatasByMarkPlanId(markPlanId);
		double sum = 0;
		List<Target> targetTemps = getTargetByRoot(rootTarget,targets);
		for(Target target : targetTemps){
			for(Score score : scores){
				if(score.getTarget().getId().equals(target.getId())){
					sum += score.getScore()*getWeight(target,targetDatas);
				}
			}
		}
		return sum;
	}
	
	private List<Target> getTargetByRoot(Target rootTarget, List<Target> targets) {
		List<Target> targetTemps = new ArrayList<Target>();
		for(Target target : targets){
			if(target.getRoot() == null){
				if(target.getId().equals(rootTarget.getId())){
					targetTemps.add(target);
				}
			}else{
				if(target.getRoot().getId().equals(rootTarget.getId())){
					targetTemps.add(target);
				}
			}
		}
		return targetTemps;
	}

	private double getWeight(Target target, List<TargetData> targetDatas){
		double weight = 1;
//		for(TargetData targetData : targetDatas){
//			if(targetData.getTarget().getId().equals(target.getId())){
//				weight *= targetData.getWeight();
//			}
//		}
		weight *= getParentsWeight(target,targetDatas);;
		return weight;
	}

	private double getParentsWeight(Target target, List<TargetData> targetDatas) {
		double weight = 1;
		if(target.getParent() == null){
			return 1;
		}
		for(TargetData targetData : targetDatas){
			if(targetData.getTarget().getId().equals(target.getId())){
				weight *= targetData.getWeight();
			}
		}
//		if(target.getParent() != null && target.getParent().getLevel() == 1){
//			return 1;
//		}
		return weight*getParentsWeight(target.getParent(),targetDatas);
	}

	public String addScore(String markPlanId, String personId, String targetId,double score) {
		try {
			Target target = this.targetDaoImpl.getTargetById(targetId);
			List<Score> scores = this.scoreDaoImpl.getScoreByPersonAndMarkPlan(personId, markPlanId);
			//List<TargetData> targetDatas = this.targetDaoImpl.getTargetDatasByMarkPlanId(markPlanId);
			//targetServiceImpl.recursiveSetTopScore(target.getId(),score,scores,targetDatas);
			MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
			Person person = this.personDaoImpl.getPersonById(personId);
			List<TopScore> topScores = this.targetDaoImpl.getTopScoreByMarkPlanIdAndPersonId(markPlanId, personId);
			targetServiceImpl.recursiveSetTopScore(target.getId(),score,markPlan,person,topScores);
			Score temp = this.scoreDaoImpl.getScoreByParam(markPlanId,personId,targetId);
			if(temp != null){
				temp.setScore(score);
				this.scoreDaoImpl.updateScore(temp);
			}else{
				//MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
				//Person person = this.personDaoImpl.getPersonById(personId);
				Score scoreBean = new Score(IDUtils.getUUID(),score,target,person,markPlan);
				this.scoreDaoImpl.addScore(scoreBean);
			}
		} catch (Exception e) {
			return "打分失败";
		}
		return "打分成功";
	}

	public List<Score> getScoreByPersonId(String personId) {
		return this.scoreDaoImpl.getScoreByPersonId(personId);
	}

	public String getZFractionByLevelList(String markPlanId,String targetId,int level,int start, int limit) {
		MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
		//List<Person> persons = personDaoImpl.getListPage(start, limit);
		List<Person> persons = new ArrayList<Person>(markPlan.getPersons());
		int personsNum = persons.size();
		List<Target> levelTargets = targetDaoImpl.getTargetsByLevel(level);//指定层级子节点
		List<Target> rootTargets = getRootTargetByLevel(levelTargets);
		Map<Target,Map<Person,Double>> datas = getZFractionByLevel(persons,markPlanId,level,rootTargets);
		Target targetTemp =this.targetDaoImpl.getTargetById(targetId);
		List<StatisticsDataBean> dataBeans = new ArrayList<StatisticsDataBean>();
		for(Person person : persons){
			StatisticsDataBean bean = new StatisticsDataBean(person.getName(), datas.get(targetTemp).get(person));
			dataBeans.add(bean);
		}
		JSONObject json = new JSONObject();
		try {
			JSONArray data = JSONArray.fromObject(dataBeans);
			json.put("data", data);
			json.put("total", personsNum);
			json.put("start", start);
			json.put("limit", limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	public String getZFractionByUnLevelList(String markPlanId,String[] targetIds,String targetId, int start, int limit) {
		MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
		//List<Person> persons = personDaoImpl.getListPage(start, limit);
		List<Person> persons = new ArrayList<Person>(markPlan.getPersons());
		int personsNum = persons.size();
		List<Target> targets = targetDaoImpl.getTargetsByIds(targetIds);
		Map<Target,Map<Person,Double>> datas = getZFractionByUnLevel(persons,markPlanId,targets);
		Target targetTemp =this.targetDaoImpl.getTargetById(targetId);
		List<StatisticsDataBean> dataBeans = new ArrayList<StatisticsDataBean>();
		for(Person person : persons){
			StatisticsDataBean bean = new StatisticsDataBean(person.getName(), datas.get(targetTemp).get(person));
			dataBeans.add(bean);
		}
		JSONObject json = new JSONObject();
		try {
			JSONArray data = JSONArray.fromObject(dataBeans);
			json.put("data", data);
			json.put("total", personsNum);
			json.put("start", start);
			json.put("limit", limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	private List<Target> getRootTargetByLevel(List<Target> levelTargets) {
		Set<Target> targets = new HashSet<Target>();
		for(Target target : levelTargets){
			if(target.getRoot() == null){
				targets.add(target);
			}else{
				targets.add(target.getRoot());
			}
			
		}
		return new ArrayList<Target>(targets);
	}

	public String statisticsCharByLevel(String markPlanId, int level) {
		
		MarkPlan markPlan = this.markPlanDaoImpl.getMarkPlanById(markPlanId);
		List<Person> persons = new ArrayList<Person>(markPlan.getPersons());
		List<Target> levelTargets = targetDaoImpl.getTargetsByLevel(level);//指定层级子节点
		List<Target> rootTargets = getRootTargetByLevel(levelTargets);
		Map<Target,Map<Person,Double>> datas = getZFractionByLevel(persons,markPlanId,level,rootTargets);
		
		JSONArray series = new JSONArray();
		String[] legendData = new String[persons.size()];
		for(int i = 0; i < persons.size(); i++){
			legendData[i] = persons.get(i).getName();
			JSONObject seriesJson = new JSONObject();
			seriesJson.put("name", persons.get(i).getName());
			Double[] seriesData = new Double[rootTargets.size()];
			for(int j = 0; j < rootTargets.size(); j++){
				if((""+datas.get(rootTargets.get(j)).get(persons.get(i))).equals("NaN")){
					seriesData[j] = 0.00;
				}else{
					BigDecimal bd = new BigDecimal(datas.get(rootTargets.get(j)).get(persons.get(i)));  
					seriesData[j] = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
			}
			seriesJson.put("data", seriesData);
			series.add(seriesJson);
		}
		JSONArray legendDataArr = JSONArray.fromObject(legendData);
		
		String[] categoryData = new String[rootTargets.size()];
		for(int i = 0; i < rootTargets.size(); i++){
			categoryData[i] = rootTargets.get(i).getText();
		}
		JSONArray categoryDataArr = JSONArray.fromObject(categoryData);
		
		JSONObject json = new JSONObject();
		try {
			json.put("legend", legendDataArr);
			json.put("categories", categoryDataArr);
			json.put("series", series);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public String test() {
		List<Target> targets = this.targetDaoImpl.getTargetsByLevel(1);
		List<String> targetIds = new ArrayList<String>();
		for(Target target : targets){
			targetIds.add(target.getId());
		}
		List<String> personIds = new ArrayList<String>();
		for(int i = 1; i < 4; i++){
			personIds.add(i+"");
		}
		List<Score> scores = this.scoreDaoImpl.getScoreByMarkPlanId("00708cf1-48fe-43d9-b0dd-4f13f100");
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
		for(Score score : scores){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("proficientId", score.getPerson().getId());
			map.put("targetId", score.getTarget().getId());
			map.put("score", score.getScore());
			datas.add(map);
		}
		Map<String,Map<String,Double>> result = StatisticalAlgorithmUtils.getNormalizedScoreByTarget(targetIds, personIds, datas,10.0,100.0);
//	    Map<String,Map<String,Double>> result = StatisticalAlgorithmUtils.getNormalizedScoreByAll(targetIds, personIds, datas,20.0,100.0);		
		for(String targetId : targetIds){
			Map<String,Double> map = result.get(targetId);
			for(String personId : personIds){
				System.out.print(map.get(personId)+"("+personId+")  ");
			}
			System.out.println();
		}
		return null;
	}
}
