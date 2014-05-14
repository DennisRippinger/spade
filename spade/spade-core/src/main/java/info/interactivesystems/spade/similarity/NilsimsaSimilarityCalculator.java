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
    private ReviewContentService service;

    @Resource
    private NilsimsaSimilarityDao nisimsaDao;

    @Resource
    private NilsimsaHash hash;

    /**
     * Calculate similarity between unique reviews.
     */
    public void calculateSimilarityBetweenUniqueReviews() {

        List<Review> uniqueReviews = service.findAllUniqueReviews();
        Integer max = uniqueReviews.size() - 1;
        Integer start = 0;

        for (Review outerReview : uniqueReviews) {
            for (Integer current = start; current < max; current++) {
                Review innerReview = uniqueReviews.get(current);
                Integer sameBits = hash.compare(outerReview.getNilsimsa(), innerReview.getNilsimsa());

                NilsimsaSimilarity similarity = new NilsimsaSimilarity();
                similarity.setProductA(outerReview.getId());
                similarity.setProductB(innerReview.getId());
                similarity.setSimilarity(sameBits / 128.0);

                nisimsaDao.save(similarity);
            }
            start++;

            // Reduce output noise
            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID '{}'", start);
            }
        }

    }

}
