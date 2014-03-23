/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.dao;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * The Class DaoHelper.
 * 
 * @author Dennis Rippinger
 */
public class DaoHelper {

	@Resource
	protected SessionFactory sessionFactory;

	/**
	 * Deletes a generic object from the database.
	 * 
	 * @param obj
	 *            the object for deletetion.
	 */
	public void helperDeletion(Object obj) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.delete(obj);
		tx.commit();
	}

	/**
	 * Finds a generic object from the database.
	 * 
	 * @param <T>
	 *            the generic return type.
	 * @param id
	 *            the id of the desired object.
	 * @param t
	 *            the generic class.
	 * @return An object of the desired class.
	 */
	public <T> T helperFind(String id, Class<T> t) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		@SuppressWarnings("unchecked")
		T object = (T) session.get(t, id);
		tx.commit();

		return object;
	}

	/**
	 * Saves a generic object to the database.
	 * 
	 * @param obj
	 *            the desired object.
	 */
	public void helperSave(Object obj) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		session.save(obj);
		tx.commit();
	}
}