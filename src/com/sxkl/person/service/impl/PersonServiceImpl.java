package com.sxkl.person.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sxkl.common.utils.IDUtils;
import com.sxkl.person.dao.PersonDao;
import com.sxkl.person.model.Person;
import com.sxkl.person.service.PersonService;

@Service("personServiceImpl")
public class PersonServiceImpl implements PersonService{

	@Autowired
	@Qualifier("personDaoImpl")
	private PersonDao personDaoImpl;
	private Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

	public List<Person> listAllPerson() {
		List<Person> persons = personDaoImpl.getAllPerson();
		if(persons == null || persons.isEmpty()){
			persons = new ArrayList<Person>();
		}
		return persons;
	}

	public void addPerson(String name) {
		Person  person = new Person(IDUtils.getUUID());
		person.setName(name);
		personDaoImpl.addPerson(person);
	}

	public String getListPage(int start, int limit) {
		JSONObject json = new JSONObject();
		try {
			List<Person> persons = personDaoImpl.getListPage(start,limit);
			int personsNum = personDaoImpl.getListPageNum();
			JSONArray data = JSONArray.fromObject(persons);
			json.put("data", data);
			json.put("total", personsNum);
			json.put("start", start);
			json.put("limit", limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public void editPerson(String id, String name) {
		try {
			Person person = this.personDaoImpl.getPersonById(id);
			person.setName(name);
		} catch (Exception e) {
			logger.error("修改人员失败:{}",e.getLocalizedMessage());
		}
	}

	public void deletePerson(String id) {
		try {
			Person person = this.personDaoImpl.getPersonById(id);
			this.personDaoImpl.deletePerson(person);
		} catch (Exception e) {
			logger.error("删除人员失败:{}",e.getLocalizedMessage());
		}
	}
}
