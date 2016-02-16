package com.sxkl.common.utils;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class PageNoUtil {
	
	/**
     * @param             session :一个会话
     * @param            hql:是需要执行的hql语句，
     * @param            offset 设置开始位置
     * @param              length:读取记录条数
     * return             返回结果集List<?>表示一个泛型的List
     */
    public static List<?> getList( Session session,String hql,int offset,int length,Object[] param){
       Query query = session.createQuery(hql);
       if(param.length > 0){
    	   int num = param.length;
    	   for(int i = 0; i < num; i++){
    		   query.setParameter(i, param[i]);
           }
       }
       query.setFirstResult(offset);
       query.setMaxResults(length);
       List<?> list = query.list();
       return list;
    }
    
    public static List<?> getList( Session session,String hql,Object[] param){
        Query query = session.createQuery(hql);
        if(param.length > 0){
     	   int num = param.length;
     	   for(int i = 0; i < num; i++){
     		   query.setParameter(i, param[i]);
            }
        }
        List<?> list = query.list();
        return list;
     }
    
    public static List<?> getList( Session session,String hql,int offset,int length){
        Query query = session.createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(length);
        List<?> list = query.list();
        return list;
     }

}
