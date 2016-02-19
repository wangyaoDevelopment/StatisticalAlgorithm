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

/**
 * 人员控制器
 * @author wangyao
 * @date 2016-02-19
 */
@Controller
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	@Qualifier("personServiceImpl")
	private PersonService personServiceImpl;
	private Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	/**
	 * 跳转到人员列表界面
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * 添加人员
	 * @param name 姓名
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/add.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String addPerson(String name){
		return personServiceImpl.addPerson(name);
	}
	
	/**
	 * 修改人员
	 * @param id 人员ID
	 * @param name 人员姓名
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/edit.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String editPerson(String id,String name){
		return personServiceImpl.editPerson(id,name);
	}
	
	/**
	 * 删除人员
	 * @param id 人员ID
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/delete.do",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	public String deletePerson(String id){
		return personServiceImpl.deletePerson(id);
	}
	
	/**
	 * 分页查询人员列表
	 * @param start 起始条目
	 * @param limit 每页条目数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String getPersonList(int start, int limit) {
		return this.personServiceImpl.getListPage(start,limit);
	}

}
