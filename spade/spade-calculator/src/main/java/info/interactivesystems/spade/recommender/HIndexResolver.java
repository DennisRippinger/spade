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

import java.util.List;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Component
public class HIndexResolver {

    @Resource
    private ReviewContentService service;

    /**
     * Resolve h index.
     * 
     * @param maxIndex the max index
     */
    public void resolveHIndex(Double maxIndex) {
        List<User> users = service.findUsersWithHIndex(maxIndex);
        log.info("Found '{}' Users with an hIndex higher than '{}'", users.size(), maxIndex);

        for (User user : users) {
            List<Review> reviewsFromUser = service.findReviewFromUser(user.getId());
            Review review = getMaximumVariance(reviewsFromUser);
            review.setMaximumVariance(true);
            service.saveReview(review);
        }

        log.info("Finished calculating relevant reviews of HIndex");

    }

    private Review getMaximumVariance(List<Review> reviewsFromUser) {
        Double variance = 0.0;
        Review mostVarianceReview = null;

        for (Review review : reviewsFromUser) {
            Product currentProduct = service.findProduct(review.getProduct());
            Double tmp = review.getRating() - currentProduct.getRating();
            tmp = Math.abs(tmp);
            review.setVariance(tmp);

            if (tmp > variance) {
                variance = tmp;
                mostVarianceReview = review;
            }

            service.saveReview(review);
        }

        return mostVarianceReview;
    }

}
