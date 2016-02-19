package com.sxkl.target.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.person.model.Person;

@Entity
@Table(name="t_top_score")
public class TopScore {
	
	@Id
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid") 
	@Column(name="t_s_id", nullable=false, length=32)
	private String id;//直达分值主键
	@Column(name="t_s_top_score")
	private Double topScore;//最大分值
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_s_person_id",nullable=true)
	private Person person;//人员
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_s_target_id",nullable=true)
	private Target target;//指标
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_s_mark_plan_id",nullable=true)
	private MarkPlan markPlan;//打分计划
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getTopScore() {
		return topScore;
	}
	public void setTopScore(Double topScore) {
		this.topScore = topScore;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public MarkPlan getMarkPlan() {
		return markPlan;
	}
	public void setMarkPlan(MarkPlan markPlan) {
		this.markPlan = markPlan;
	}
	
}
