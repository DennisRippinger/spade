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

import info.interactivesystems.spade.entities.ShadowReview;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ShadowReviewDao for the {@link ShadowReview} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
@Deprecated
public class ShadowReviewDao implements GenericDao<ShadowReview> {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public void delete(ShadowReview obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public ShadowReview find(String id) {
        return (ShadowReview) sessionFactory.getCurrentSession().get(ShadowReview.class, id);
    }

    @Override
    public void save(ShadowReview t) {
        sessionFactory.getCurrentSession().saveOrUpdate(t);
    }

    @SuppressWarnings("unchecked")
    public List<ShadowReview> findEmptyAvgRating() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM ShadowReview review WHERE review.averageRating is null AND review.url is not null")
            .setMaxResults(100000)
            .list();
    }

    @SuppressWarnings("unchecked")
    public List<ShadowReview> findReviewFromUrl(String url) {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM ShadowReview sr WHERE url = :url")
            .setParameter("url", url)
            .list();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRandomDistinct(Integer maximum) {
        String query = "SELECT DISTINCT(r1.url) FROM Spade.Shadow_Reviews AS r1 " +
            "JOIN (SELECT (RAND() * (SELECT MAX(randomIdSort) FROM Spade.Shadow_Reviews)) AS id) AS r2 " +
            "WHERE r1.randomIdSort >= r2.id AND ((r1.url IS NOT NULL AND r1.averageRating IS NULL) OR (r1.url IS NOT NULL AND r1.averageRating = 0)) " +
            "ORDER BY r1.id ASC";
        return sessionFactory.getCurrentSession()
            .createSQLQuery(query)
            .setMaxResults(maximum)
            .list();
    }
}
