package com.sxkl.score.model;

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
import com.sxkl.target.model.Target;

@Entity
@Table(name="t_score")
public class Score {
	
	@Id
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid") 
	@Column(name="s_id", nullable=false, length=32)
	private String id;
	@Column(name="s_score", nullable=false)
	private double score = 0;
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="s_target_id",nullable=true)
	private Target target;
	@ManyToOne(cascade=CascadeType.ALL,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="s_person_id",nullable=false)
	private Person person;
	@ManyToOne(cascade=CascadeType.ALL,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="s_mark_plan_id",nullable=false)
	private MarkPlan markPlan;
	
	public Score() {
	}
	
	public Score(String id, double score, Target target, Person person,MarkPlan markPlan) {
		this.id = id;
		this.score = score;
		this.target = target;
		this.person = person;
		this.markPlan = markPlan;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

	public MarkPlan getMarkPlan() {
		return markPlan;
	}

	public void setMarkPlan(MarkPlan markPlan) {
		this.markPlan = markPlan;
	}
}
