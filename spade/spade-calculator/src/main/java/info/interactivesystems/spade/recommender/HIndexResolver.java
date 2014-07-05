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
import java.util.concurrent.ThreadLocalRandom;

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

    private static final Integer MAXSIZE = 2441053;

    @Resource
    private ReviewContentService service;

    /**
     * Resolve h index.
     * 
     * @param maxIndex the max index
     */
    public void resolveHIndex() {
        for (Long count = 1l; count <= MAXSIZE; count++) {
            User user = service.findUserByID(count);

            List<Review> reviewsFromUser = service.findReviewFromUser(user.getId());
            calculateMaximumVariance(reviewsFromUser);
            
            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID '{}'", count);
            }
        }

        log.info("Finished calculating Variances related to HIndex");

    }

    private void calculateMaximumVariance(List<Review> reviewsFromUser) {

        for (Review review : reviewsFromUser) {
            Product currentProduct = review.getProduct();
            Double tmp = review.getRating() - currentProduct.getRating();
            tmp = Math.abs(tmp);
            review.setVariance(tmp);

            service.saveReview(review);
        }

    }

}
