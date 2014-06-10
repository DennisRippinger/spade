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
package info.interactivesystems.spade.similarity;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * The Class NilsimsaSimilarityCalculator.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class NilsimsaSimilarityCalculator {

    @Resource
    private NilsimsaSimilarityDao nisimsaDao;

    @Resource
    private ReviewContentService service;

    @Resource
    private NilsimsaHash hash;

    /**
     * Calculate similarity between unique reviews.
     */
    public void calculateSimilarityBetweenUniqueReviews(String category) {
        List<Review> uniqueReviews = service.findReviewsByCategory(category);
        Integer start = 0;
        Integer counter = 0;

        log.info("Size of '{}': '{}'", category, uniqueReviews.size());

        for (Review outerReview : uniqueReviews) {

            for (Integer current = start; current < uniqueReviews.size(); current++) {
                Review innerReview = uniqueReviews.get(current);
                if (innerReview.getId().equals(outerReview.getId())) {
                    continue;
                }

                Integer sameBits = hash.compare(outerReview.getNilsimsa(), innerReview.getNilsimsa());
                Double percentage = sameBits / 128.0;

                if (percentage >= 0.70) {
                    NilsimsaSimilarity similarity = new NilsimsaSimilarity();
                    similarity.setProductA(outerReview.getId());
                    similarity.setProductB(innerReview.getId());
                    similarity.setSimilarity(percentage);
                    similarity.setCategory(category);

                    nisimsaDao.save(similarity);
                    counter++;
                }
            }

            start++;

            // Reduce output noise
            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID of '{}' on '{}': {}'", category, getHostnameSecure(), start);
            }
        }

        log.info("FINISHED on '{}'. Found '{}' similar items", getHostnameSecure(), counter);

    }

    private String getHostnameSecure() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Could not getHostName");
        }

        return "Empty";
    }

}
