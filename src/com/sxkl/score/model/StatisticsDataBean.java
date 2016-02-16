package com.sxkl.score.model;

public class StatisticsDataBean {
	
	private String name;
	private double score;
	
	
	public StatisticsDataBean() {
	}
	public StatisticsDataBean(String name, double score) {
		this.name = name;
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
}
