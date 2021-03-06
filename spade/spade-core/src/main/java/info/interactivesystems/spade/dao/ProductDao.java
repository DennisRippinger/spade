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

import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class ProductDao for the {@link Product} entities.
 *
 * @author Dennis Rippinger
 */
@Repository
public class ProductDao extends AbstractDao<Product> {

	public ProductDao() {
		super(Product.class);
	}

	public Boolean checkIfAlreadyExists(String id) {
		Product find = find(id);

		return find != null;
	}

	public Product findByID(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Product.class);
		criteria.add(Restrictions.eq("randomID", id));

		return (Product) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Review> findReviewsByCategory(String category) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Product.class);
		criteria.add(Restrictions.eq("category", category));

		List<Review> result = new LinkedList<>();
		for (Product product : (List<Product>) criteria.list()) {
			for (Review review : product.getReviews()) {
				if (review.isUnique()) {
					result.add(review);
				}
			}

		}

		return result;
	}

}
