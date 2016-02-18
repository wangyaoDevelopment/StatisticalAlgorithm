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
	@RequestMapping(value="/add.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String addPerson(String name){
		try{
			personServiceImpl.addPerson(name);
		} catch(Exception e){
			logger.error(e.toString(), e);
			return "添加人员失败";
		}
		return "添加人员成功";
	}
	
	@ResponseBody
	@RequestMapping(value="/edit.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String editPerson(String id,String name){
		try{
			personServiceImpl.editPerson(id,name);
		} catch(Exception e){
			logger.error(e.toString(), e);
			return "修改人员失败";
		}
		return "修改人员成功";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String deletePerson(String id){
		try{
			personServiceImpl.deletePerson(id);
		} catch(Exception e){
			logger.error(e.toString(), e);
			return "删除人员失败";
		}
		return "删除人员成功";
	}
	
	@ResponseBody
	@RequestMapping(value = "/list.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getPersonList(int start, int limit) {
		String result = this.personServiceImpl.getListPage(start,limit);
		return result;
	}

}
