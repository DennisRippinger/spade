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

import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * The Class NilsimsaSimilarityDao.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class NilsimsaSimilarityDao extends AbstractDao<NilsimsaSimilarity> {

    public static final Integer WINDOW_SIZE = 10;
    
    private static final String SIMILARITY = "similarity";

    public NilsimsaSimilarityDao() {
        super(NilsimsaSimilarity.class);
    }

    public List<NilsimsaSimilarity> find(Double similarity, Boolean sameAuthor, Integer window) {
        Criteria criteria = getSameAuthorCriteria(similarity, sameAuthor);
        criteria.setFirstResult((window -1) * WINDOW_SIZE);
        criteria.setMaxResults(WINDOW_SIZE);

        return initialize(criteria);
    }

    private Criteria getSameAuthorCriteria(Double similarity, Boolean sameAuthor) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);
        criteria.add(Restrictions.ge(SIMILARITY, similarity))
            .add(Restrictions.eq("sameAuthor", sameAuthor));

        return criteria;
    }

    public List<NilsimsaSimilarity> find(Double similarity, Boolean sameAuthor, Integer wordDistance, Integer limit) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);
        criteria.add(Restrictions.ge(SIMILARITY, similarity))
            .add(Restrictions.eq("sameAuthor", sameAuthor))
            .add(Restrictions.le("wordDistance", wordDistance));
        criteria.setMaxResults(limit);

        return initialize(criteria);
    }

    public List<NilsimsaSimilarity> find(Double similarity, Integer limit) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);
        criteria.add(Restrictions.ge(SIMILARITY, similarity));
        criteria.setMaxResults(limit);

        return initialize(criteria);
    }

    public List<NilsimsaSimilarity> find(Double similarity, String category, Integer limit) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);
        criteria.add(Restrictions.ge(SIMILARITY, similarity))
            .add(Restrictions.eq("category", category));
        criteria.setMaxResults(limit);

        return initialize(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<String> getCategories() {
        List<String> result = new ArrayList<>();
        result.add("All");

        SQLQuery distinctQuery = sessionFactory.getCurrentSession()
            .createSQLQuery("SELECT DISTINCT(category) FROM spade.similarities;");

        result.addAll(distinctQuery.list());

        return result;
    }

    @SuppressWarnings("unchecked")
    public NilsimsaSimilarity findSimilarityByReviewId(String reviewId) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);

        criteria.add(Restrictions.disjunction()
            .add(Restrictions.eq("reviewA.id", reviewId))
            .add(Restrictions.eq("reviewB.id", reviewId))
            );
        criteria.addOrder(Order.desc("similarity"));

        List<NilsimsaSimilarity> resultList = criteria.list();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }

        return null;
    }

    private List<Review> getUniqueReviews(List<Review> reviews) {
        List<Review> result = new ArrayList<>();

        for (Review review : reviews) {
            if (review.isUnique()) {
                result.add(review);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<NilsimsaSimilarity> initialize(Criteria criteria) {
        List<NilsimsaSimilarity> result = criteria.list();
        for (NilsimsaSimilarity nilsimsaSimilarity : result) {
            nilsimsaSimilarity.getUserA().setReviews(getUniqueReviews(nilsimsaSimilarity.getUserA().getReviews()));
            nilsimsaSimilarity.getUserB().setReviews(getUniqueReviews(nilsimsaSimilarity.getUserB().getReviews()));
        }
        return result;
    }
}
