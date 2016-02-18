package com.sxkl.person.service;

import java.util.List;

import com.sxkl.person.model.Person;

public interface PersonService {

	public List<Person> listAllPerson();

	public void addPerson(String name);

	public String getListPage(int start, int limit);

	public void editPerson(String id, String name);

	public void deletePerson(String id);

}
