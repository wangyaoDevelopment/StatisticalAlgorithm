package com.sxkl.score.service;

import java.util.List;

import com.sxkl.score.model.Score;

public interface ScoreService {

	String addScore(String markPlanId, String personId, String targetId,double score);

	List<Score> getScoreByPersonId(String personId);

	String getZFractionByLevelList(String markPlanId, String targetId, int level, int start, int limit);

	String getZFractionByUnLevelList(String markPlanId, String[] targetIds,String targetId, int start, int limit);

	String statisticsCharByLevel(String markPlanId, int level);

}
