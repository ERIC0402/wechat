package org.beangle.wechat.core.trigger;

import org.beangle.wechat.core.service.impl.BaseWechatImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class BaseTrigger extends BaseWechatImpl {

	private static Logger logger = LoggerFactory.getLogger(BaseTrigger.class);

	private SessionFactory sessionFactory;

	private boolean existingTransaction;

	public void openSession() {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		boolean existingTransaction = SessionFactoryUtils.isSessionTransactional(session, sessionFactory);
		if (existingTransaction) {
			logger.info("Found thread-bound Session for TransactionalQuartzTask");
		} else {
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}
	}

	public void closeSession() {
		if (existingTransaction) {
			logger.debug("Not closing pre-bound Hibernate Session after TransactionalQuartzTask");
		} else {
			SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.releaseSession(sessionHolder.getSession(), sessionFactory);
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
