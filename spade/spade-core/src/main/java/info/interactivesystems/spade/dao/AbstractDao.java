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

import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * The Interface GenericDao. Offers generic methods for all entities.
 *
 * @param <T> the desired type.
 * @author Dennis Rippinger
 */
@Transactional
public abstract class AbstractDao<T> {

	@Resource
	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	public AbstractDao(Class<T> clazz) {
		this.entityClass = clazz;
	}

	@SuppressWarnings("unchecked")
	public T find(Serializable id) {
		if (id == null) {
			throw new PersistenceException("ID must not be null");
		}
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}

	public void save(T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	public void update(T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	public void delete(T entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

}