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
package info.interactivesystems.spade.recommender;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * H_v Index calculations.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class HIndex {

	private static final Double AMAZON_GLOBAL_AVG = 4.150117735337405;


	@Resource
	private ReviewContentService service;

	public void calculateHIndex(Long from, Long to) {

		for (Long counter = from; counter <= to; counter++) {

			User currentUser = service.findUserByID(counter);

			List<Review> reviewsFromUser = service.findReviewFromUser(currentUser.getId());
			Double result;
			if (reviewsFromUser.size() > 1) {
				List<Product> productsFromUser = findAllProducts(reviewsFromUser);

				Double userAverage = calculateUserAverage(reviewsFromUser);

				Double h1 = calculateH1(reviewsFromUser, productsFromUser, userAverage);
				Double h2 = calculateH2(reviewsFromUser, userAverage);

				if (h2 > 0.0) {
					result = h1 / h2;
					currentUser.setHIndex(result);
				} else {
					result = 0.0;
				}

			} else {
				result = 0.0;
			}

			currentUser.setHIndex(result);
			service.saveUser(currentUser);

			// Reduce output noise
			Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
			if (rand == 500) {
				log.info("Current User '{}' has an hIndex of '{}'. Position '{}'", currentUser.getName(), result, counter);
			}

		}

		log.info("FINISHED hIndex on '{}'.", getHostnameSecure());

	}

	private Double calculateH2(List<Review> reviewsFromUser, Double userAverage) {
		Double result = 0.0;
		for (Review review : reviewsFromUser) {
			Double h2 = Math.pow(review.getRating() - userAverage, 2);
			result += h2;
		}
		return result;
	}

	private Double calculateH1(List<Review> reviewsFromUser, List<Product> productsFromUser, Double userAverage) {
		Double result = 0.0;

		for (Integer pointer = 0; pointer < productsFromUser.size(); pointer++) {
			Review currentReview = reviewsFromUser.get(pointer);
			Product currentProduct = productsFromUser.get(pointer);

			Double h1 = currentReview.getRating() - userAverage - currentProduct.getRating() + AMAZON_GLOBAL_AVG;
			h1 = Math.pow(h1, 2);
			result += h1;
		}

		return result;
	}

	private List<Product> findAllProducts(List<Review> reviewsFromUser) {
		List<Product> result = new ArrayList<Product>();
		for (Review review : reviewsFromUser) {
			Product product = review.getProduct();
			result.add(product);
		}
		return result;
	}

	private Double calculateUserAverage(List<Review> reviewsFromUser) {
		Double result = 0.0;

		for (Review review : reviewsFromUser) {
			result += review.getRating();
		}
		result = result / reviewsFromUser.size();

		return result;
	}

	private String getHostnameSecure() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn("Could not getHostName", e);
		}

		return "Empty";
	}
}
