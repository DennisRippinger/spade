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

import org.springframework.stereotype.Repository;

import info.interactivesystems.spade.entities.User;

/**
 * The Class UserDao for the {@link User} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class UserDao extends DaoHelper implements GenericDao<User> {

    @Override
    public void delete(User obj) {
        helperDeletion(obj);
    }

    @Override
    public User find(String id) {
        return helperFind(id, User.class);
    }

    @Override
    public void save(User t) {
        helperSave(t);
    }

}
