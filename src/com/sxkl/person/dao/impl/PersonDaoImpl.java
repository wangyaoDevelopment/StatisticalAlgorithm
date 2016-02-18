package com.sxkl.person.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.sxkl.common.utils.PageNoUtil;
import com.sxkl.person.dao.PersonDao;
import com.sxkl.person.model.Person;

@Repository("personDaoImpl")
public class PersonDaoImpl extends HibernateDaoSupport implements PersonDao{
	
	@Autowired  
    public void setSessionFactoryOverride(SessionFactory sessionFactory){  
        super.setSessionFactory(sessionFactory);  
    }

	@SuppressWarnings("unchecked")
	public List<Person> getAllPerson() {
		return (List<Person>) this.getHibernateTemplate().find("from Person");
	}

	public void addPerson(Person person) {
		this.getHibernateTemplate().save(person);
	}

	@SuppressWarnings("unchecked")
	public List<Person> getListPage(final int start, final int limit) {
		final String hql = "from Person p order by p.name desc";
		List<Person> persons = (List<Person>) getHibernateTemplate().execute(
				new HibernateCallback() {
			           public Object doInHibernate(Session session)throws HibernateException{
				            List list = PageNoUtil.getList(session,hql,start,limit);
				            return list;
			           }
				});
		return persons;
	}

	@SuppressWarnings("unchecked")
	public int getListPageNum() {
		Integer count = 0;
		final String hql = "select count(p.id) from Person p";
		Object num = this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				List list = query.list();
				if(list == null || list.size() == 0){
					return 0;
				}
				return list.get(0);
			}
		});
		count = Integer.valueOf(num.toString());
		return count;
	}

	public Person getPersonById(String personId) {
		String hql = "from Person p where p.id=?";
		String[] param = new String[]{personId};
		List<Person> persons = (List<Person>) this.getHibernateTemplate().find(hql, param);
		if(persons !=null && persons.size() > 0){
			return persons.get(0);
		}
		return null;
	}

	public void deletePerson(Person person) {
		this.getHibernateTemplate().delete(person);
	}

}
