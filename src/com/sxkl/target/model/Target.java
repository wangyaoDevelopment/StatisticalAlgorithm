package com.sxkl.target.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_target")
public class Target implements  Comparable<Target>{
	
	@Id
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid") 
	@Column(name="t_id", nullable=true, length=32)
	private String id;//指标主键
	@Column(name="t_text", nullable=true, length=30)
	private String text;//指标名称
	@Column(name="t_level", nullable=true)
	private int level;//指标层级
	@Transient
	private double weight;//指标权重
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_parent_id",nullable=true)
	private Target  parent;//父指标
	@ManyToOne(cascade=CascadeType.ALL,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="t_root_id",nullable=true)
	private Target  root;//根指标
	@OneToMany(targetEntity=Target.class,cascade=CascadeType.ALL, fetch = FetchType.EAGER)  
	@JoinColumn(name = "t_parent_id")
	private Set<Target> children = new TreeSet<Target>();//子指标
	@Transient
	private double topScore;
	@Transient
	private double score;
	
	public Target() {
	}
	public Target(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
//	public int compareTo(Target o) {
//		return this.getText().compareTo(o.getText()); 
//	}
	public Set<Target> getChildren() {
		return children;
	}
	public void setChildren(Set<Target> children) {
		this.children = children;
	}
	@JsonIgnore
	public Target getParent() {
		return parent;
	}
	public void setParent(Target parent) {
		this.parent = parent;
	}
	public Set<Target> getBrothers(Target target){
		Set<Target> targets = target.getParent().getChildren();
//		targets.remove(target);
		return targets;
	}
	public int compareTo(Target o) {
		return this.getText().compareTo(o.getText());
	}
	public double getTopScore() {
		return topScore;
	}
	public void setTopScore(double topScore) {
		this.topScore = topScore;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	@JsonIgnore
	public Target getRoot() {
		return root;
	}
	public void setRoot(Target root) {
		this.root = root;
	}
}
