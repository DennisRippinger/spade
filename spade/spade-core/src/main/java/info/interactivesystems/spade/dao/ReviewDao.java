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

import info.interactivesystems.spade.entities.Review;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 * The Class ReviewDao for the {@link Review} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class ReviewDao extends AbstractDao<Review> {

    public ReviewDao() {
        super(Review.class);
    }

    public Boolean checkIfAlreadyExists(String id) {
        Review find = find(id);

        return find != null;
    }

    @SuppressWarnings("unchecked")
    public List<Review> findReviewByProductID(String productID) {

        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(Review.class);
        criteria.add(Restrictions.eq("review.Product_fk", productID));

        return (List<Review>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Review> findReviewFromUser(String userID) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(Review.class);
        criteria.add(Restrictions.eq("user.id", userID));
        criteria.add(Restrictions.eq("unique", true));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Review> findReviewsByCategory(String category) {

        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(Review.class, "review");

        criteria.createAlias("review.product", "product");
        criteria.add(Restrictions.eq("product.category", category));
        criteria.add(Restrictions.eq("unique", true));
        
        criteria.setProjection(Projections.projectionList()
            .add(Projections.property("id"), "id")
            .add(Projections.property("nilsimsa"), "nilsimsa"))
            .setResultTransformer(Transformers.aliasToBean(Review.class));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Review> findReviewFromUserInCategory(String userID, String category) {

        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(Review.class, "review");

        criteria.createAlias("review.product", "product");
        criteria.add(Restrictions.eq("product.category", category));
        criteria.add(Restrictions.eq("user.id", userID));
        criteria.add(Restrictions.eq("unique", true));
        
        criteria.setProjection(Projections.projectionList()
            .add(Projections.property("id"), "id")
            .add(Projections.property("nilsimsa"), "nilsimsa"))
            .setResultTransformer(Transformers.aliasToBean(Review.class));

        return criteria.list();
    }

}