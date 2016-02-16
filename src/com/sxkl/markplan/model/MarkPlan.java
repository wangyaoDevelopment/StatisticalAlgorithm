package com.sxkl.markplan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_mark_plan")
public class MarkPlan {
	
	@Id
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid") 
	@Column(name="m_p_id", nullable=false, length=32)
	private String id;
	@Column(name="m_p_name", nullable=false, length=100)
	private String name;
	@Column(name="m_mark", nullable=true)
	private double mark;//分制
	
	public MarkPlan() {
	}
	public MarkPlan(String id) {
		this.id = id;
	}
	public MarkPlan(String id, String name, double mark) {
		this.id = id;
		this.name = name;
		this.mark = mark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMark() {
		return mark;
	}
	public void setMark(double mark) {
		this.mark = mark;
	}
}
