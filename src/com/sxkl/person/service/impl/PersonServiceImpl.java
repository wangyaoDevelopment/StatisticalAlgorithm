package com.sxkl.person.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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

	public String addPerson(String name) {
		try {
			Person  person = new Person(IDUtils.getUUID());
			person.setName(name);
			personDaoImpl.addPerson(person);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return "添加人员失败";
		}
		return "添加人员成功";
	}

	public String getListPage(int start, int limit) {
		JSONObject json = new JSONObject();
		try {
			List<Person> persons = personDaoImpl.getListPage(start,limit);
			int personsNum = personDaoImpl.getListPageNum();
			JsonConfig config = new JsonConfig();
			//人员的markPlans属性不转化为json格式
			config.setExcludes(new String[]{"markPlans"});
			JSONArray data = JSONArray.fromObject(persons,config);
			json.put("data", data);
			json.put("total", personsNum);
			json.put("start", start);
			json.put("limit", limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public String editPerson(String id, String name) {
		try {
			Person person = this.personDaoImpl.getPersonById(id);
			person.setName(name);
		} catch (Exception e) {
			logger.error("修改人员失败:{}",e.getLocalizedMessage());
			return "修改人员失败";
		}
		return "修改人员成功";
	}

	public String deletePerson(String id) {
		try {
			Person person = this.personDaoImpl.getPersonById(id);
			this.personDaoImpl.deletePerson(person);
		} catch (Exception e) {
			logger.error("删除人员失败:{}",e.getLocalizedMessage());
			return "删除人员失败";
		}
		return "删除人员成功";
	}
}
