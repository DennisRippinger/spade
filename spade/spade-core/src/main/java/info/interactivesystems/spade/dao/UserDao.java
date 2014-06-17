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

import java.util.List;

import info.interactivesystems.spade.entities.User;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * The Class UserDao for the {@link User} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class UserDao implements GenericDao<User> {

    @Resource
    private MongoOperations operations;

    @Override
    public void delete(User obj) {
        operations.remove(obj);
    }

    @Override
    public User find(String id) {
        return operations.findById(id, User.class);
    }

    @Override
    public void save(User t) {
        operations.save(t);
    }

    public List<User> findAll() {
        return operations.findAll(User.class);
    }

    public Boolean checkIfAlreadyExists(String id) {
        User find = find(id);
        if (find == null) {
            return false;
        } else {
            return true;
        }
    }

    public User findByID(Integer id) {
        Criteria criteria = Criteria.where("randomID").is(id);
        User user = operations.findOne(Query.query(criteria), User.class);

        return user;
    }

    /**
     * Lists all Users with an Hindex >= the maxIndex parameter.
     * 
     * @param maxIndex
     * @return
     */
    public List<User> findUsersWithHIndex(Double maxIndex) {
        Criteria criteria = Criteria.where("hIndex").gte(maxIndex);
        List<User> result = operations.find(Query.query(criteria), User.class);

        return result;
    }

}
