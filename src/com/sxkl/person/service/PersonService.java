package com.sxkl.person.service;

import java.util.List;

import com.sxkl.person.model.Person;

/**
 * 人员服务层接口
 * @author wangyao
 * @date 2016-02-19
 */
public interface PersonService {

	/**
	 * 获取所有人员集合
	 * @return 所有人员集合
	 */
	public List<Person> listAllPerson();

	/**
	 * 添加人员
	 * @param name 姓名
	 * @return 操作结果
	 */
	public String addPerson(String name);

	/**
	 * 分页查询人员列表
	 * @param start 起始条目
	 * @param limit 每页条目数
	 * @return 人员列表
	 */
	public String getListPage(int start, int limit);

	/**
	 * 更新人员信息
	 * @param id 人员ID
	 * @param name 姓名
	 * @return 操作结果
	 */
	public String editPerson(String id, String name);

	/**
	 * 删除人员
	 * @param id 人员ID
	 * @return 操作结果
	 */
	public String deletePerson(String id);

}
