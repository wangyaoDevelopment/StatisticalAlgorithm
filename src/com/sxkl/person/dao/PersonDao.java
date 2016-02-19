package com.sxkl.person.dao;

import java.util.List;

import com.sxkl.person.model.Person;

/**
 * 人员持久层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface PersonDao {

	/**
	 * 获取所有人员集合
	 * @return 所有人员集合
	 */
	public List<Person> getAllPerson();

	/**
	 * 添加人员
	 * @param person 待添加人员
	 */
	public void addPerson(Person person);

	/**
	 * 分页查询人员列表
	 * @param start 起始条目
	 * @param limit 每页条目数
	 * @return 人员列表
	 */
	public List<Person> getListPage(int start, int limit);

	/**
	 * 获取人员总数
	 * @return 人员总数
	 */
	public int getListPageNum();

	/**
	 * 根据人员ID查询人员
	 * @param personId 人员ID
	 * @return 人员
	 */
	public Person getPersonById(String personId);

	/**
	 * 删除人员
	 * @param person 待删除人员
	 */
	public void deletePerson(Person person);

	/**
	 * 根据ID查询人员
	 * @param personIds 人员ID
	 * @return 人员集合
	 */
	public List<Person> getPersonByIds(String[] personIds);

}
