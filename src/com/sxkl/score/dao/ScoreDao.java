package com.sxkl.score.dao;

import java.util.List;

import com.sxkl.score.model.Score;

public interface ScoreDao {

	public List<Score> getScoreByPersonAndMarkPlan(String personId, String markPlanId);

	public void addScore(Score scoreBean);

	public Score getScoreByParam(String markPlanId, String personId,String targetId);

	public List<Score> checkMarkByMarkPlanId(String markPlanId);

	public List<Score> getScoreByPersonId(String personId);

	public void updateScore(Score temp);

}
