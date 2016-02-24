package com.kingbase.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatisticalAlgorithmUtils {
	
	/**
	 * 获取归一化分数(采样范围为单指标数据,无层次结构)
	 * @param targetIds 指标项ID
	 * @param proficientIds 打分专家ID
	 * @param scores 某主题的所有打分  proficientId  targetId score
	 * @param ymin 指定区间最小值
	 * @param ymax 指定区间最大值
	 * @return 归一化分数
	 */
	public static Map<String,Map<String,Double>> getNormalizedScoreByTarget(List<String> targetIds,List<String> proficientIds,List<Map<String,Object>> scores,double ymin,double ymax){
		//Map<String,Double> scoresTemp = congurateScores(targetIds,proficientIds,scores);
		Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
		for(String targetId : targetIds){
			Map<String,Double> scoresTemp = new HashMap<String,Double>();
			for(String proficientId : proficientIds){
				double temp = getProficientTargetScore(proficientId,targetId,scores);
				scoresTemp.put(proficientId, temp);
			}
			map.put(targetId, calculateNormalizedScoreByTarget(scoresTemp,proficientIds,ymin,ymax));
		}
		return map;
	}
	
	/**
	 * 获取归一化分数(采样范围为单指标数据,有层次结构)
	 * @param targets 指定层次指标项 targetId  parentId (weight:程序计算,后续添加)
	 * @param proficientIds 打分专家ID
	 * @param scores 某主题的所有打分  proficientId  targetId score
	 * @param weight 指标权重 targetId weight
	 * @param ymin 指定区间最小值
	 * @param ymax 指定区间最大值
	 * @return 归一化分数
	 */
	public static Map<String,Map<String,Double>> getNormalizedScoreByTargetAndLevel(
			  List<Map<String,String>> targets,List<String> proficientIds,
			  List<Map<String,Object>> scores,List<Map<String,Object>> weights, 
			  double ymin,double ymax){
		List<String> rootTargetIds = getRootTargetIds(targets);
		List<Map<String,String>> targetsWT = getTargetsWithWeight(targets,weights);
		List<Map<String, Object>> datas = congurateScores(targetsWT,rootTargetIds,proficientIds,scores,weights);
		return getNormalizedScoreByTarget(rootTargetIds, proficientIds, datas,ymin,ymax);
	}
	
	private static List<Map<String, String>> getTargetsWithWeight(
			List<Map<String, String>> targets, List<Map<String, Object>> weights) {
		List<Map<String,String>> results = new ArrayList<Map<String,String>>();
		for(Map<String, String> target : targets){
			Map<String, String> data = new HashMap<String,String>();
			data.put("targetId", target.get("targetId"));
			data.put("parentId", target.get("parentId"));
			for(Map<String, Object> weight : weights){
				if(target.get("targetId").equals(weight.get("targetId"))){
					data.put("weight", weight.get("weight")+"");
				}
			}
			results.add(data);
		}
		return results;
	}

	private static List<Map<String, Object>> congurateScores(
			List<Map<String, String>> targetsWT,List<String> rootTargetIds, 
			List<String> proficientIds, List<Map<String, Object>> scores,List<Map<String, Object>> weights) {
		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
		//计算出每个根节点得分
		for(String rootTargetId : rootTargetIds){
			for(String proficientId : proficientIds){
				Map<String, Object> data = new HashMap<String,Object>();
				double scoreNum = 0;
				for(Map<String, Object> score : scores){
					for(Map<String, String> target : targetsWT){
						if(score.get("targetId").equals(target.get("targetId")) && target.get("parentId").equals(rootTargetId) && score.get("proficientId").equals(proficientId)){
							scoreNum += (Double.valueOf(score.get("score")+"")) * (Double.valueOf(target.get("weight")));
						}
					}
				}
				data.put("targetId", rootTargetId);
				data.put("proficientId", proficientId);
				data.put("score", scoreNum);
				datas.add(data);
			}
		}
		return datas;
	}

	private static List<String> getRootTargetIds(List<Map<String, String>> targets) {
		//遍历出所有根节点
		Set<String> rootTargetIdTemp =  new HashSet<String>();
		for(Map<String, String> map : targets){
			if(map.get("parentId") != null){
				rootTargetIdTemp.add(map.get("parentId"));
			}
		}
		List<String> rootTargetIds = new ArrayList<String>(rootTargetIdTemp);
		return rootTargetIds;
	}

	private static double getProficientTargetScore(String proficientId,String targetId, List<Map<String, Object>> scores) {
		double sum = 0;
		for(Map<String, Object> map : scores){
			if(map.get("proficientId").equals(proficientId) && map.get("targetId").equals(targetId)){
				BigDecimal bd = new BigDecimal(Double.valueOf(map.get("score")+""));  
				sum += bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}
		return sum;
	}

	private static Map<String, Double> calculateNormalizedScoreByTarget(
			Map<String, Double> scoresTemp, List<String> proficientIds, double ymin, double ymax) {
		double sum = 0;//所有人总分
		for (Map.Entry<String, Double> entry : scoresTemp.entrySet()) {
			sum += entry.getValue();
	    }
		//求所有人平均分
		double avg = sum/proficientIds.size();
		//求平均方差
		double averageVariance = getAverageVariance(avg,scoresTemp,proficientIds);
		//求平均方差平方根
		double averageVarianceSqrt = Math.sqrt(averageVariance);
		//求标准方差
		Map<String,Double> standardVariance = getStandardVariance(scoresTemp,avg,averageVarianceSqrt,proficientIds);
		//求归一化分数
		Map<String,Double> normalizedScore = getNormalizedScore(proficientIds,standardVariance,ymin,ymax);
		return normalizedScore;
	}

	private static double getAverageVariance(double avg,
			Map<String, Double> scoresTemp, List<String> proficientIds) {
		double sum = 0;
		for(String proficientId : proficientIds){
			double avgV = Math.pow((scoresTemp.get(proficientId) - avg),2);
			sum += avgV;
		}
		return sum/proficientIds.size();
	}

	private static Map<String, Double> getStandardVariance(
			Map<String, Double> scoresTemp, double avg,
			double averageVarianceSqrt, List<String> proficientIds) {
		Map<String,Double> standardVariance = new HashMap<String,Double>();
		for(String proficientId : proficientIds){
			standardVariance.put(proficientId,((scoresTemp.get(proficientId) - avg)/averageVarianceSqrt));
		}
		return standardVariance;
	}
	
	private static Map<String, Double> getNormalizedScore(
			List<String> proficientIds, Map<String, Double> standardVariance, double ymin, double ymax) {
		Collection<Double> datas = standardVariance.values();
		double xmax = Collections.max(datas);
		double xmin = Collections.min(datas);
		Map<String,Double> result = new HashMap<String,Double>();
		for(String proficientId : proficientIds){
			double data = standardVariance.get(proficientId);
			double value = ymin + ((ymax-ymin)/(xmax-xmin))*(data-xmin);
			BigDecimal bd = new BigDecimal(Double.valueOf(value));  
			result.put(proficientId, bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		return result;
	}
	
	/**
	 * 获取归一化分数(采样范围为全部指标数据,无层次结构)
	 * @param targetIds 指标项ID
	 * @param proficientIds 打分专家ID
	 * @param scores 某主题的所有打分  proficientId  targetId score
	 * @param ymin 指定区间最小值
	 * @param ymax 指定区间最大值
	 * @return 归一化分数
	 */
	public static Map<String,Map<String,Double>> getNormalizedScoreByAll(List<String> targetIds,List<String> proficientIds,List<Map<String,Object>> scores,double ymin,double ymax){
		//Map<String,Double> scoresTemp = congurateScores(targetIds,proficientIds,scores);
		Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
		for(String targetId : targetIds){
			map.put(targetId, calculateZFractionByAll(scores,targetId,proficientIds));
		}
		return getNormalizedScoreAll(map,targetIds,proficientIds,ymin,ymax);
	}
	/**
	 * 获取归一化分数(采样范围为全部指标数据,有层次结构)
	 * @param targets 指定层次指标项 targetId  parentId (weight:程序计算,后续添加)
	 * @param proficientIds 打分专家ID
	 * @param scores 某主题的所有打分  proficientId  targetId score
	 * @param weight 指标权重 targetId weight
	 * @param ymin 指定区间最小值
	 * @param ymax 指定区间最大值
	 * @return 归一化分数
	 */
	public static Map<String,Map<String,Double>> getNormalizedScoreByAllAndLevel(
			  List<Map<String,String>> targets,List<String> proficientIds,
			  List<Map<String,Object>> scores,List<Map<String,Object>> weights, 
			  double ymin,double ymax){
		List<String> rootTargetIds = getRootTargetIds(targets);
		List<Map<String,String>> targetsWT = getTargetsWithWeight(targets,weights);
		List<Map<String, Object>> datas = congurateScores(targetsWT,rootTargetIds,proficientIds,scores,weights);
		return getNormalizedScoreByAll(rootTargetIds, proficientIds, datas,ymin,ymax);
	}

	private static Map<String, Map<String, Double>> getNormalizedScoreAll(
			Map<String, Map<String, Double>> map, List<String> targetIds,
			List<String> proficientIds,double ymin,double ymax) {
		Map<String, Map<String, Double>> result = new HashMap<String, Map<String, Double>>();
		List<Double> datas = new ArrayList<Double>();
		for(String targetId : targetIds){
			datas.addAll(map.get(targetId).values());
		}
		double xmax = Collections.max(datas);
		double xmin = Collections.min(datas);
		for(String targetId : targetIds){
			Map<String,Double> dataTemp = map.get(targetId);
			Map<String,Double> resultTemp = new HashMap<String,Double>();
			for(String proficientId : proficientIds){
				double data = dataTemp.get(proficientId);
				double value = ymin + ((ymax-ymin)/(xmax-xmin))*(data-xmin);
				BigDecimal bd = new BigDecimal(Double.valueOf(value+""));  
				resultTemp.put(proficientId, bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			result.put(targetId, resultTemp);
		}
		return result;
	}

	private static Map<String, Double> calculateZFractionByAll(
			List<Map<String,Object>> scores, String targetId, List<String> proficientIds) {
		double sum = 0;//所有人总分
		for(Map<String,Object> map : scores){
			BigDecimal bd = new BigDecimal(Double.valueOf(map.get("score")+""));  
			sum += bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		//求所有人平均分
		double avg = sum/scores.size();
		//求平均方差
		double averageVariance = getAverageVarianceAll(avg,scores);
		//求平均方差平方根
		double averageVarianceSqrt = Math.sqrt(averageVariance);
		//求标准方差
		Map<String,Double> standardVariance = getStandardVarianceAll(scores,avg,averageVarianceSqrt,proficientIds,targetId);
		return standardVariance;
	}

	private static Map<String, Double> getStandardVarianceAll(
			List<Map<String, Object>> scores, double avg,
			double averageVarianceSqrt, List<String> proficientIds,
			String targetId) {
		Map<String,Double> standardVariance = new HashMap<String,Double>();
		for(Map<String,Object> map : scores){
			for(String proficientId : proficientIds){
				if(map.get("targetId").equals(targetId) && map.get("proficientId").equals(proficientId)){
					standardVariance.put(proficientId,(((Double.valueOf(map.get("score")+"") - avg)/averageVarianceSqrt)));
				}
			}
			
		}
		return standardVariance;
	}

	private static double getAverageVarianceAll(double avg,
			List<Map<String, Object>> scores) {
		double sum = 0;
		for(Map<String,Object> map : scores){
			double avgV = Math.pow((Double.valueOf(map.get("score")+"") - avg),2);
			sum += avgV;
		}
		return sum/scores.size();
	}
	
}
