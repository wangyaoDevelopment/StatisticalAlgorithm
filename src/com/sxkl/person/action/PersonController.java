package com.sxkl.person.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.person.model.Person;
import com.sxkl.person.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	@Qualifier("personServiceImpl")
	private PersonService personServiceImpl;
	private Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	@RequestMapping(value="/toList.do")
	public ModelAndView toList()throws Exception{
		ModelAndView mv = new ModelAndView();
		try{
			List<Person> personList = personServiceImpl.listAllPerson();
			mv.addObject("personList", personList);
			mv.setViewName("person_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="/list.do")
	public List<Person> getPersonList(){
		List<Person> personList = null;
		try{
			personList = personServiceImpl.listAllPerson();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return personList;
	}
	
	@ResponseBody
	@RequestMapping(value="/addPerson.do")
	public String addPerson(Person person){
		try{
			personServiceImpl.addPerson(person);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/list.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getWorkMainPage(int start, int limit) {
		String result = this.personServiceImpl.getListPage(start,limit);
		return result;
	}

}
