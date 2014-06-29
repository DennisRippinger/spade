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

import info.interactivesystems.spade.entities.User;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * The Class UserDao for the {@link User} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class UserDao extends AbstractDao<User> {

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
     * Lists all Users with an Hindex >= the maxIndex parameter.
     * 
     * @param maxIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findUsersWithHIndex(Double maxIndex) {

        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(User.class);
        criteria.add(Restrictions.ge("hIndex", maxIndex));

        return criteria.list();
    }

}
