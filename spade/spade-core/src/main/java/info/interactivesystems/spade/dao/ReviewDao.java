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

import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ReviewDao for the {@link Review} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class ReviewDao implements GenericDao<Review> {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public void delete(Review obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public Review find(String id) {
        return (Review) sessionFactory.getCurrentSession().get(Review.class, id);
    }

    @Override
    public void save(Review t) {
        sessionFactory.getCurrentSession().save(t);
    }

    public List<Review> getReviewsOfCategory(ProductCategory category) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from Product WHERE type = :type");
        query.setParameter("type", category.getId());

        List<Review> result = new LinkedList<Review>();

        @SuppressWarnings("unchecked")
        List<Product> productList = query.list();

        for (Product product : productList) {
            result.addAll(product.getReviews());
        }

        return result;
    }
}