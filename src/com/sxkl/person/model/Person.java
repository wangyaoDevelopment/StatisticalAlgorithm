package com.sxkl.person.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.markplan.model.MarkPlan;

@Entity
@Table(name="t_person")
public class Person {
	
	@Id
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid") 
	@Column(name="p_id", nullable=false, length=32)
	private String id;//人员主键
	@Column(name="p_name", nullable=true, length=10)
	private String name;//人员姓名
	@ManyToMany(mappedBy="persons",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	private Set<MarkPlan> markPlans = new HashSet<MarkPlan>();//打分计划
	
	public Person() {
	}
	public Person(String id) {
		this.id = id;
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
	public Set<MarkPlan> getMarkPlans() {
		return markPlans;
	}
	public void setMarkPlans(Set<MarkPlan> markPlans) {
		this.markPlans = markPlans;
	}
}
