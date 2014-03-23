package info.interactivesystems.spade.dao;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DaoHelper {

	@Resource
	private SessionFactory sessionFactory;

	public void helperDeletion(Object obj) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.delete(obj);
		tx.commit();
	}

	public <T> T helperFind(String id, Class<T> t) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		@SuppressWarnings("unchecked")
		T object = (T) session.get(t, id);
		tx.commit();

		return object;
	}
}