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
import info.interactivesystems.spade.dto.Nilsimsa;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.util.NilsimsaCsvImport;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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
    private NilsimsaHash hash;

    /**
     * Calculate similarity between unique reviews.
     */
    public void calculateSimilarityBetweenUniqueReviews() {

        NilsimsaCsvImport importer = new NilsimsaCsvImport();

        List<Nilsimsa> uniqueReviews = importer.getReviewsFromCSV("/Users/dennisrippinger/nilsimsa.csv");
        Integer max = uniqueReviews.size() - 1;
        Integer start = 1;

        log.info("Size of uniqueReview: '{}'", uniqueReviews.size());

        for (Nilsimsa outerReview : uniqueReviews) {
            StopWatch watch = new StopWatch();
            watch.start();
            for (Integer current = start; current < max; current++) {
                Nilsimsa innerReview = uniqueReviews.get(current);
                Integer sameBits = hash.compare(outerReview.getNilsimsa(), innerReview.getNilsimsa());
                Double percentage = sameBits / 128.0;

                if (percentage >= 80.0) {
                    NilsimsaSimilarity similarity = new NilsimsaSimilarity();
                    similarity.setProductA(outerReview.getId());
                    similarity.setProductB(innerReview.getId());
                    similarity.setSimilarity(percentage);

                    nisimsaDao.save(similarity);
                }
            }
            watch.stop();
            log.info(watch.prettyPrint());
            start++;

            // Reduce output noise
            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID '{}'", start);
            }
        }

    }

}
