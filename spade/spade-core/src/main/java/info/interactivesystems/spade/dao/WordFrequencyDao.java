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

import info.interactivesystems.spade.entities.WordFrequency;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

/**
 * The Class WordFrequencyDao.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class WordFrequencyDao implements GenericDao<WordFrequency> {

    @Resource
    private MongoOperations operations;

    @Override
    public void delete(WordFrequency obj) {
        operations.remove(obj);
    }

    @Override
    public WordFrequency find(String id) {
        return operations.findById(id, WordFrequency.class);
    }

    @Override
    public void save(WordFrequency t) {
        operations.save(t);
    }

}
