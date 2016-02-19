package com.sxkl.markplan.dao.impl;

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
import com.sxkl.markplan.dao.MarkPlanDao;
import com.sxkl.markplan.model.MarkPlan;

@Repository("markPlanDaoImpl")
public class MarkPlanDaoImpl extends HibernateDaoSupport implements MarkPlanDao{

	@Autowired  
    public void setSessionFactoryOverride(SessionFactory sessionFactory){  
        super.setSessionFactory(sessionFactory);  
    }

	@SuppressWarnings("unchecked")
	public List<MarkPlan> getListPage(final int start, final int limit) {
		final String hql = "from MarkPlan m order by m.name desc";
		List<MarkPlan> markPlans = (List<MarkPlan>) getHibernateTemplate().execute(
				new HibernateCallback() {
			           public Object doInHibernate(Session session)throws HibernateException{
				            List list = PageNoUtil.getList(session,hql,start,limit);
				            return list;
			           }
				});
		return markPlans;
	}

	@SuppressWarnings("unchecked")
	public int getListPageNum() {
		Integer count = 0;
		final String hql = "select count(m.id) from MarkPlan m";
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

	public MarkPlan getMarkPlanById(String id) {
		List<MarkPlan> list = (List<MarkPlan>) this.getHibernateTemplate().find("from MarkPlan m where m.id=?", new String[]{id});
		return list.get(0);
	}

	public void updateMarkPlan(MarkPlan mp) {
		this.getHibernateTemplate().update(mp);
	}

	public void addMarkPlan(MarkPlan mp) {
		this.getHibernateTemplate().save(mp);
	}

	public void delMarkPlan(MarkPlan mp) {
		this.getHibernateTemplate().delete(mp);
	}
}
