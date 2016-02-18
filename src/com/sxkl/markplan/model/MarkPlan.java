package com.sxkl.markplan.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.person.model.Person;

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
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinTable(
        name="t_mark_plan_person",
        joinColumns=@JoinColumn(name="t_mark_plan_id"),
        inverseJoinColumns=@JoinColumn(name="t_person_id")
	)
	private Set<Person> persons = new HashSet<Person>();
	@Transient
	private Integer personNum;
	
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
	public Set<Person> getPersons() {
		return persons;
	}
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	public Integer getPersonNum() {
		return persons.size();
	}
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
}
