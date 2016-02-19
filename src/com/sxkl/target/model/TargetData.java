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

@Entity
@Table(name="t_target_data")
public class TargetData {
	
	@Id
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid") 
	@Column(name="t_d_id", nullable=false, length=32)
	private String id;//权重数据主键
	@Column(name="t_d_weight", nullable=true)
	private double weight;//权重
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_target_id",nullable=true)
	private Target target;//指标
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_mark_plan_id",nullable=true)
	private MarkPlan markPlan;//打分计划
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
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
