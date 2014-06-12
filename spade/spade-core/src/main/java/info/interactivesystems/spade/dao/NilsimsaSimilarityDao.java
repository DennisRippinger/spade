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

import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * The Class NilsimsaSimilarityDao.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class NilsimsaSimilarityDao implements GenericDao<NilsimsaSimilarity> {

    @Resource
    private MongoOperations operations;

    @Override
    public void delete(NilsimsaSimilarity obj) {
        operations.remove(obj);

    }

    @Override
    public NilsimsaSimilarity find(String id) {
        return operations.findById(id, NilsimsaSimilarity.class);
    }

    @Override
    public void save(NilsimsaSimilarity t) {
        operations.save(t);
    }

    public List<NilsimsaSimilarity> findWithinRange(Double similarity) {
        Query query = Query.query(
            Criteria.where("proccesed").exists(false)
                .and("similarity").gte(similarity)
            );
        return operations.find(query, NilsimsaSimilarity.class);
    }

}
