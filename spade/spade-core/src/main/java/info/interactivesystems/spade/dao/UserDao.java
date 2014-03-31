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

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import info.interactivesystems.spade.entities.User;

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

}
