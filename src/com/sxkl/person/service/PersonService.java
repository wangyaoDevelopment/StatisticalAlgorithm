package com.sxkl.person.service;

import java.util.List;

import com.sxkl.person.model.Person;

public interface PersonService {

	public List<Person> listAllPerson();

	public void addPerson(Person person);

	public String getListPage(int start, int limit);

}
