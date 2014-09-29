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
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Class TargetItemRetrieval. Implements the second step of the UnRAP Paper.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class TargetItemRetrieval {

	@Resource
	private ReviewContentService service;

	public void retrievalOfTargetItems() {

		List<User> users = service.findUsersWithHIndex(2.0, 10057);
		Integer count = 1;

		for (User user : users) {
			List<Review> reviews = service.findHighestVarianceReview(user.getId());
			incrementVariance(reviews);
			count++;

			Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
			if (rand == 500) {
				log.info("Current Item ID '{}'", count);
			}
		}

	}

	@Transactional
	private void incrementVariance(List<Review> reviews) {
		for (Review review : reviews) {
			Product product = review.getProduct();
			Integer noOfTopDifferences = product.getNoOfTopDifferences();

			if (noOfTopDifferences == null) {
				noOfTopDifferences = 1;
			} else {
				noOfTopDifferences++;
			}

			product.setNoOfTopDifferences(noOfTopDifferences);
			service.saveProduct(product);
		}
	}
}
