package com.sxkl.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.orm.hibernate4.support.AsyncRequestInterceptor;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;

public class MyOpenSessionInViewFilter extends OpenSessionInViewFilter {
	
	private static final Logger myLogger = LoggerFactory.getLogger(MyOpenSessionInViewFilter.class);

	public Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		try {
			Session session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.AUTO);
			return session;
		} catch (HibernateException ex) {
			throw new DataAccessResourceFailureException( "Could not open Hibernate Session", ex);
		}
	}

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		SessionFactory sessionFactory = lookupSessionFactory(request);
		boolean participate = false;

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
		String key = getAlreadyFilteredAttributeName();

		if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
			participate = true;
		} else {
			boolean isFirstRequest = !isAsyncDispatch(request);
			if ((isFirstRequest)|| (!applySessionBindingInterceptor(asyncManager, key))) {
				this.logger.debug("Opening Hibernate Session in OpenSessionInViewFilter");
//				myLogger.info("================================Opening Hibernate Session in MyOpenSessionInViewFilter================================");
				Session session = openSession(sessionFactory);
				SessionHolder sessionHolder = new SessionHolder(session);
				TransactionSynchronizationManager.bindResource(sessionFactory,sessionHolder);

				AsyncRequestInterceptor interceptor = new AsyncRequestInterceptor(sessionFactory, sessionHolder);
				asyncManager.registerCallableInterceptor(key, interceptor);
				asyncManager.registerDeferredResultInterceptor(key, interceptor);
			}
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			if (!participate) {
				SessionHolder mySessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
				if (!isAsyncStarted(request)) {
					this.logger.debug("Closing Hibernate Session in OpenSessionInViewFilter");
					mySessionHolder.getSession().flush();
					SessionFactoryUtils.closeSession(mySessionHolder.getSession());
//					myLogger.info("================================Closing Hibernate Session in MyOpenSessionInViewFilter================================");
				}
			}
		}
	}
	
	private boolean applySessionBindingInterceptor(WebAsyncManager asyncManager, String key) {
	    if (asyncManager.getCallableInterceptor(key) == null) {
	      return false;
	    }
	    ((AsyncRequestInterceptor)asyncManager.getCallableInterceptor(key)).bindSession();
	    return true;
	  }
	
}
