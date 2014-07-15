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
import info.interactivesystems.spade.entities.User;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * The Class UserDao for the {@link User} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class UserDao extends AbstractDao<User> {

    private static final String HINDEX = "hIndex";

    public UserDao() {
        super(User.class);
    }

    public Boolean checkIfAlreadyExists(String id) {
        User find = find(id);

        return find != null;
    }

    public User findByRandomID(Long id) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(User.class);
        criteria.add(Restrictions.eq("randomID", id));

        return (User) criteria.uniqueResult();
    }

    /**
     * Lists all Users with an Hindex >= the minIndex parameter.
     * 
     * @param minIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findUsersWithHIndex(Double minIndex) {

        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(User.class);
        criteria.add(Restrictions.ge(HINDEX, minIndex));

        return criteria.list();
    }

    /**
     * Lists all Users with an Hindex >= the maxIndex parameter.
     * 
     * @param minIndex the minimum index value.
     * @param limit maximum amount of users returned.
     * @return the User list
     */
    public List<User> findUsersWithHIndex(Double minIndex, Integer limit) {

        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(User.class);
        criteria.add(Restrictions.ge(HINDEX, minIndex));
        criteria.add(Restrictions.ge("numberOfReviews", 10));
        
        // TODO Topmost hIndex user has 21k reviews.
        criteria.add(Restrictions.le(HINDEX, 20.0));

        criteria.addOrder(Order.desc(HINDEX));
        criteria.setMaxResults(limit);

        return initialize(criteria);
    }

    /**
     * In this case EAGER loading will result in the n+1 error. touching the collection tells Hibernate to collect the
     * information in a proper way.
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<User> initialize(Criteria criteria) {
        List<User> result = criteria.list();
        for (User user : result) {
            user.setReviews(getUniqueReviews(user.getReviews()));
        }
        return result;
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
}
