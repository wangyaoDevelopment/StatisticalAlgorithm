package com.sxkl.person.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sxkl.common.utils.IDUtils;
import com.sxkl.markplan.model.MarkPlan;
import com.sxkl.person.dao.PersonDao;
import com.sxkl.person.model.Person;
import com.sxkl.person.service.PersonService;

@Service("personServiceImpl")
public class PersonServiceImpl implements PersonService{

	@Autowired
	@Qualifier("personDaoImpl")
	private PersonDao personDaoImpl;

	public List<Person> listAllPerson() {
		List<Person> persons = personDaoImpl.getAllPerson();
		if(persons == null || persons.isEmpty()){
			persons = new ArrayList<Person>();
		}
		return persons;
	}

	public void addPerson(Person person) {
		person.setId(IDUtils.getUUID());
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
}
