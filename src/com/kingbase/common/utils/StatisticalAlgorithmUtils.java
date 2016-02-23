package com.kingbase.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticalAlgorithmUtils {
	
	/**
	 * 获取归一化分数(采样范围为单指标数据,无层次结构)
	 * @param targetIds 指标项ID
	 * @param proficientIds 打分专家ID
	 * @param scores 某主题的所有打分  proficientId  targetId score
	 * @return 归一化分数
	 */
	public static Map<String,Map<String,Double>> getNormalizedScoreByTarget(List<String> targetIds,List<String> proficientIds,List<Map<String,Object>> scores){
		//Map<String,Double> scoresTemp = congurateScores(targetIds,proficientIds,scores);
		Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
		for(String targetId : targetIds){
			Map<String,Double> scoresTemp = new HashMap<String,Double>();
			for(String proficientId : proficientIds){
				double temp = getProficientTargetScore(proficientId,targetId,scores);
				scoresTemp.put(proficientId, temp);
			}
			map.put(targetId, calculateNormalizedScoreByTarget(scoresTemp,proficientIds));
		}
		return map;
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
			Map<String, Double> scoresTemp, List<String> proficientIds) {
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
		Map<String,Double> normalizedScore = getNormalizedScore(proficientIds,standardVariance);
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
			List<String> proficientIds, Map<String, Double> standardVariance) {
		double ymax = 100;
		double ymin = 10;
		Collection<Double> datas = standardVariance.values();
		double xmax = Collections.max(datas);
		double xmin = Collections.min(datas);
		Map<String,Double> result = new HashMap<String,Double>();
		for(String proficientId : proficientIds){
			double data = standardVariance.get(proficientId);
			double value = ymin + ((ymax-ymin)/(xmax-xmin))*(data-xmin);
			result.put(proficientId, value);
		}
		return result;
	}
	/**
	 * 获取归一化分数(采样范围为全部指标数据,无层次结构)
	 * @param targetIds 指标项ID
	 * @param proficientIds 打分专家ID
	 * @param scores 某主题的所有打分  proficientId  targetId score
	 * @return 归一化分数
	 */
	public static Map<String,Map<String,Double>> getNormalizedScoreByAll(List<String> targetIds,List<String> proficientIds,List<Map<String,Object>> scores){
		//Map<String,Double> scoresTemp = congurateScores(targetIds,proficientIds,scores);
		Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
		for(String targetId : targetIds){
//			Map<String,Double> scoresTemp = new HashMap<String,Double>();
//			for(String proficientId : proficientIds){
//				double temp = getProficientTargetScore(proficientId,targetId,scores);
//				scoresTemp.put(proficientId, temp);
//			}
			map.put(targetId, calculateZFractionByAll(scores,targetId,proficientIds));
		}
		return getNormalizedScoreAll(map,targetIds,proficientIds);
	}

	private static Map<String, Map<String, Double>> getNormalizedScoreAll(
			Map<String, Map<String, Double>> map, List<String> targetIds,
			List<String> proficientIds) {
		Map<String, Map<String, Double>> result = new HashMap<String, Map<String, Double>>();
		List<Double> datas = new ArrayList<Double>();
		for(String targetId : targetIds){
			datas.addAll(map.get(targetId).values());
		}
		
		double ymax = 100;
		double ymin = 10;
		double xmax = Collections.max(datas);
		double xmin = Collections.min(datas);
		for(String targetId : targetIds){
			Map<String,Double> dataTemp = map.get(targetId);
			Map<String,Double> resultTemp = new HashMap<String,Double>();
			for(String proficientId : proficientIds){
				double data = dataTemp.get(proficientId);
				double value = ymin + ((ymax-ymin)/(xmax-xmin))*(data-xmin);
				resultTemp.put(proficientId, value);
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
