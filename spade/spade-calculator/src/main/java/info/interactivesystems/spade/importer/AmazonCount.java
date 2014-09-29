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
package info.interactivesystems.spade.importer;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Calculates an Baysian average value to gain an average that is near to amazons original.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class AmazonCount {

	private static final Integer OBERSERVATIONS = 5;
	private static final Integer MAXSIZE = 2441053;
	private static final Double AMAZON_GLOBAL_AVG = 4.190279903844344;

	@Resource
	private ReviewContentService service;

	public void countNumberOfReviews() {
		for (Long count = 1L; count <= MAXSIZE; count++) {
			Product currentProduct = service.findByID(count);

			if (currentProduct != null) {
				Set<Review> reviews = currentProduct.getReviews();
				currentProduct.setNoOfReviews(reviews.size());
				currentProduct.setRating(getAverage(reviews));

				service.saveProduct(currentProduct);
			}

			Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
			if (rand == 500) {
				log.info("Current Item ID '{}'", count);
			}
		}
	}

	/**
	 * Bayesian average calculation.
	 *
	 * @param reviews
	 * @return
	 */
	public Double getAverage(Set<Review> reviews) {
		Double numberOfStars = 0.0;

		for (Review review : reviews) {
			numberOfStars += review.getRating();
		}

		Double nominator = (OBERSERVATIONS * AMAZON_GLOBAL_AVG) + numberOfStars;
		Integer denominator = OBERSERVATIONS + reviews.size();

		return nominator / denominator;
	}

}