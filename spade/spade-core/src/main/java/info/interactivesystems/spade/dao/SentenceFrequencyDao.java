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

import info.interactivesystems.spade.entities.SentenceFrequency;
import info.interactivesystems.spade.util.ProductCategory;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class SentenceFrequencyDao.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class SentenceFrequencyDao implements GenericDao<SentenceFrequency> {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public void delete(SentenceFrequency obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    /**
     * SentenceFrequency works with a composite key. Therefore this method will not work.
     * 
     * @return Will return null.
     */
    public SentenceFrequency find(String id) {
        return null;
    }

    /**
     * Find a SentenceFrequency Instance.
     * 
     * @param id the Hash of a sentence.
     * @param category the category number.
     * @return the frequency value.
     */
    public SentenceFrequency find(String id, ProductCategory category) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("FROM SentenceFrequency WHERE id = :id AND category = :category");
        query.setParameter("id", id);
        query.setParameter("category", category.getId());
        SentenceFrequency result = (SentenceFrequency) query.uniqueResult();

        return result;
    }

    @Override
    public void save(SentenceFrequency t) {
        sessionFactory.getCurrentSession().saveOrUpdate(t);
    }

}
