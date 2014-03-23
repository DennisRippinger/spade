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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

/**
 * The Class ReviewDao for the {@link Review} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class ReviewDao extends DaoHelper implements GenericDao<Review> {

    @Override
    public void delete(Review obj) {
        helperDeletion(obj);
    }

    @Override
    public Review find(String id) {
        return helperFind(id, Review.class);
    }

    @Override
    public void save(Review t) {
        helperSave(t);
    }

    public List<Review> getReviewsOfCategory(ProductCategory category) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Product WHERE type = :type");
        query.setParameter("type", category.getId());

        List<Review> result = new LinkedList<Review>();

        @SuppressWarnings("unchecked")
        List<Product> productList = query.list();
        tx.commit();
        
        for (Product product : productList) {
            result.addAll(product.getReviews());
        }


        return result;
    }

}