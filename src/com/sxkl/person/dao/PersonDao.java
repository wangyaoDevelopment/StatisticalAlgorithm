package com.sxkl.person.dao;

import java.util.List;

import com.sxkl.person.model.Person;

public interface PersonDao {

	public List<Person> getAllPerson();

	public void addPerson(Person person);

	public List<Person> getListPage(int start, int limit);

	public int getListPageNum();

	public Person getPersonById(String personId);

	public void deletePerson(Person person);

}
