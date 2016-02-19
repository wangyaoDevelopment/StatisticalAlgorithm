package com.sxkl.target.dao.impl;

import java.util.ArrayList;
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
import com.sxkl.target.dao.TargetDao;
import com.sxkl.target.model.Target;
import com.sxkl.target.model.TargetData;
import com.sxkl.target.model.TopScore;

@Repository("targetDaoImpl")
public class TargetDaoImpl extends HibernateDaoSupport implements TargetDao{

	@Autowired  
    public void setSessionFactoryOverride(SessionFactory sessionFactory){  
        super.setSessionFactory(sessionFactory);  
    }

	@SuppressWarnings("unchecked")
	public List<Target> getAllTarget() {
		return (List<Target>) this.getHibernateTemplate().find("from Target");
	}

	public Target getTargetTreeByRootNode() {
		String hql = "from Target t where t.parent.id is null";
		List<Target> data = (List<Target>) this.getHibernateTemplate().find(hql);
		if(data != null && data.size() > 0){
			return data.get(0);
		}
		return null;
	}

	public List<Target> getTargetsByLevel(int level) {
		String hql = "from Target t where t.level=?";
		Integer[] param = new Integer[]{level};
		List<Target> data = (List<Target>) this.getHibernateTemplate().find(hql,param);
		if(data != null && data.size() > 0){
			return data;
		}
		return new ArrayList<Target>();
	}

	@SuppressWarnings("unchecked")
	public Target getRootTarget() {
		String hql = "from Target t where t.parent is null";
		List<Target> data = (List<Target>) this.getHibernateTemplate().find(hql);
		if(data != null && data.size() > 0){
			return data.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Target> getAllLeafTarget() {
		String hql = "from Target t where t.children is null";
		List<Target> data = (List<Target>) this.getHibernateTemplate().find(hql);
		if(data != null && data.size() > 0){
			return data;
		}
		return new ArrayList<Target>();
	}

	public Target getTargetById(String id) {
		String hql = "from Target t where t.id=?";
		String[] param = new String[]{id};
		List<Target> data = (List<Target>) this.getHibernateTemplate().find(hql,param);
		if(data != null && data.size() > 0){
			return data.get(0);
		}
		return null;
	}

	public void updateTarget(Target target) {
		this.getHibernateTemplate().update(target);
	}

	public void deleteTarget(Target target) {
		this.getHibernateTemplate().delete(target);
	}

	public List<Target> getChildTargetById(String id) {
		String hql = "from Target t where t.parent.id=?";
		String[] param = new String[]{id};
		List<Target> data = (List<Target>) this.getHibernateTemplate().find(hql,param);
		if(data != null && data.size() > 0){
			return data;
		}
		return new ArrayList<Target>();
	}

	public List<TargetData> getTargetDatasByMarkPlanId(String markPlanId) {
		String hql = "from TargetData t where t.markPlan.id=?";
		String[] param = new String[]{markPlanId};
		List<TargetData> data = (List<TargetData>) this.getHibernateTemplate().find(hql,param);
		if(data != null && data.size() > 0){
			return data;
		}
		return new ArrayList<TargetData>();
	}

	public void addTargetData(TargetData data) {
		this.getHibernateTemplate().save(data);
	}

	public TargetData getTargetDatasByTargetIdAndMarkPlanId(String targetId,String markPlanId) {
		String hql = "from TargetData t where t.target.id=? and t.markPlan.id=?";
		String[] param = new String[]{targetId,markPlanId};
		List<TargetData> data = (List<TargetData>) this.getHibernateTemplate().find(hql,param);
		if(data != null && data.size() > 0){
			return data.get(0);
		}
		return null;
	}

	public void updateTargetData(TargetData targetData) {
		this.getHibernateTemplate().update(targetData);
	}

	@SuppressWarnings("unchecked")
	public List<Target> getRootTargetListPage(final int start, final int limit) {
		final String hql = "from Target p where p.root is null and p.parent is not null order by p.text desc";
		List<Target> targets = (List<Target>) getHibernateTemplate().execute(
				new HibernateCallback() {
			           public Object doInHibernate(Session session)throws HibernateException{
				            List list = PageNoUtil.getList(session,hql,start,limit);
				            return list;
			           }
				});
		return targets;
	}

	@SuppressWarnings("unchecked")
	public int getRootTargetListPageNum() {
		String hql = "from Target p where p.root is null and p.parent is not null order by p.text desc";
		List<Target> targets = (List<Target>) this.getHibernateTemplate().find(hql);
		return targets.size();
	}

	@SuppressWarnings("unchecked")
	public List<Target> getTargetsByIds(final String[] targetIds) {
		final String hql = "from Target t where t.id in (:ids)";
		List<Target> targets = (List<Target>) this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setParameterList("ids", targetIds);
				List<Target> list = query.list();
				if(list == null || list.size() == 0){
					return new ArrayList<Target>();
				}
				return list;
			}
		});
		return targets;
	}

	public List<TopScore> getTopScoreByMarkPlanIdAndPersonId(String markPlanId,String personId) {
		String hql = "from TopScore ts where ts.markPlan.id=? and ts.person.id=?";
		String[] params = new String[]{markPlanId,personId};
		List<TopScore> datas = (List<TopScore>) this.getHibernateTemplate().find(hql, params);
		if(datas == null){
			return new ArrayList<TopScore>();
		}
		return datas;
	}

	public void addTopScore(TopScore topScore) {
		this.getHibernateTemplate().save(topScore);
	}

	@SuppressWarnings("unchecked")
	public List<Target> getTargetsPageByIds(final String[] targetIds, final int start,final int limit) {
		final String hql = "from Target t where t.id in (:ids)";
		List<Target> targets = (List<Target>) this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setParameterList("ids", targetIds);
		        query.setFirstResult(start);
		        query.setMaxResults(limit);
				List<Target> list = query.list();
				if(list == null || list.size() == 0){
					return new ArrayList<Target>();
				}
				return list;
			}
		});
		return targets;
	}

	@SuppressWarnings("unchecked")
	public int getTargetMaxLevel() {
		Integer level = 0;
		final String hql = "select max(t.level) from Target t";
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
		level = Integer.valueOf(num.toString());
		return level;
	}

	public void addTarget(Target target) {
		this.getHibernateTemplate().save(target);
	}
	
}
