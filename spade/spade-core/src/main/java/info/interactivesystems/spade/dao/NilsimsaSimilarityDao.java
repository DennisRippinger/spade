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

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * The Class NilsimsaSimilarityDao.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class NilsimsaSimilarityDao extends AbstractDao<NilsimsaSimilarity> {

    public NilsimsaSimilarityDao() {
        super(NilsimsaSimilarity.class);
    }

    @SuppressWarnings("unchecked")
    public List<NilsimsaSimilarity> findWithinRange(Double similarity) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);
        criteria.add(Restrictions.ge("similarity", similarity));

        return (List<NilsimsaSimilarity>) criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<NilsimsaSimilarity> find(Double similarity, Boolean sameAuthor, Integer wordDistance, Integer limit) {
        Criteria criteria = sessionFactory.getCurrentSession()
            .createCriteria(NilsimsaSimilarity.class);
        criteria.add(Restrictions.ge("similarity", similarity))
            .add(Restrictions.eq("sameAuthor", sameAuthor))
            .add(Restrictions.ge("wordDistance", wordDistance));
        criteria.setMaxResults(limit);

        return criteria.list();
    }

}
