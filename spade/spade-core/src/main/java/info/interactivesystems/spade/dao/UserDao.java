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
import info.interactivesystems.spade.util.Authority;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class UserDao for the {@link User} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class UserDao implements GenericDao<User> {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public void delete(User obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public User find(String id) {
        return (User) sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    public void save(User t) {
        sessionFactory.getCurrentSession().saveOrUpdate(t);
    }

    public Boolean checkIfAlreadyExists(String id) {
        User find = find(id);
        if (find == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<User> getUsersOfAuthority(Authority authority) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("from User WHERE authority = :authority");
        query.setParameter("authority", authority);

        @SuppressWarnings("unchecked")
        List<User> userList = query.list();

        return userList;
    }
    
    public User getRandomUser() {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createSQLQuery("SELECT r1.id FROM Users" +
            " AS r1 JOIN (SELECT (RAND() * (SELECT MAX(randomID) FROM Users)) AS id) AS r2 " +
            "WHERE r1.randomID >= r2.id AND concurrentBit = 0");
        query.setMaxResults(1);

        String id = (String) query.uniqueResult();

        User randomProduct = find(id);

        return randomProduct;

    }

}
