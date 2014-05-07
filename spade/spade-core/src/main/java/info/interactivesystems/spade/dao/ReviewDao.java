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

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * The Class ReviewDao for the {@link Review} entities.
 * 
 * @author Dennis Rippinger
 */
@Repository
public class ReviewDao implements GenericDao<Review> {

    @Resource
    private MongoOperations operations;

    @Override
    public void delete(Review obj) {
        operations.remove(obj);
    }

    @Override
    public Review find(String id) {
        return operations.findById(id, Review.class);
    }

    @Override
    public void save(Review t) {
        operations.save(t);
    }

    public Boolean checkIfAlreadyExists(String id) {
        Review find = find(id);
        if (find == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<Review> findReviewByProductID(String productID) {
        Criteria criteria = Criteria.where("product").is(productID);
        List<Review> result = operations.find(Query.query(criteria), Review.class);

        return result;
    }

}