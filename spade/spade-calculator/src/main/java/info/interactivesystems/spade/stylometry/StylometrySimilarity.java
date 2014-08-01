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
package info.interactivesystems.spade.stylometry;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.Stylometry;
import info.interactivesystems.spade.entities.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Component
public class StylometrySimilarity {

    @Resource
    private ReviewContentService service;

    public void calculatePCA(User user) {

        if (user.getNumberOfReviews() <= 1) {
            return;
        }

        List<Review> reviews = service.findReviewFromUser(user.getId());
        Map<Review, List<Double>> reviewMap = new HashMap<>();

        Integer outerCounter = 0;
        Integer innerCounter = 1;
        Integer extraCounter = 2;
        for (; outerCounter <= reviews.size() - 1; outerCounter++) {

            for (; innerCounter < reviews.size(); innerCounter++) {
                Double similarity = calculateSimilarity(reviews.get(outerCounter), reviews.get(innerCounter));

                addEntry(similarity, reviewMap, reviews.get(outerCounter), reviews.get(innerCounter));
            }

            innerCounter = extraCounter++;
        }

        Double meanUserSimilarity = 0.0;
        for (Entry<Review, List<Double>> set : reviewMap.entrySet()) {

            Double meanSimilarity = 0.0;
            for (Double similarity : set.getValue()) {
                meanSimilarity += similarity;
            }
            meanSimilarity /= set.getValue().size();

            meanUserSimilarity += meanSimilarity;

            Review review = set.getKey();
            review.setMeanSimilarity(meanSimilarity);

            service.saveReview(review);
        }
        meanUserSimilarity /= reviewMap.size();

        user.setMeanStylometry(meanUserSimilarity);
        service.saveUser(user);

        // Reduce output noise
        Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
        if (rand == 500) {
            log.info("Current User '{}' has an meanDifference of '{}'. Position '{}'", user.getName(), meanUserSimilarity, user.getRandomID());
        }

    }

    private Double calculateSimilarity(Review review, Review review2) {
        Double[] v1 = createVector(review.getStylometry());
        Double[] v2 = createVector(review2.getStylometry());

        Double d1 = 0.0;
        Double d2 = 0.0;
        Double d21 = 0.0;
        Double d22 = 0.0;

        for (Integer counter = 0; counter < v1.length; counter++) {
            d1 += v1[counter] * v2[counter];
            d21 += Math.pow(v1[counter], 2);
            d22 += Math.pow(v2[counter], 2);
        }
        d21 = Math.sqrt(d21);
        d22 = Math.sqrt(d22);

        d2 = d21 * d22;

        return d1 / d2;
    }

    private Double[] createVector(Stylometry stylometry) {
        Double[] result = {
            stylometry.getAri(),
            stylometry.getGfi(),
            stylometry.getDensity(),
            stylometry.getWordLevel(),
            stylometry.getCommaOccurence() * 100,
            stylometry.getDigitBigrams() * 100,
            stylometry.getDigitTrigrams() * 100,
            stylometry.getDollarOccurence() * 100,
            stylometry.getQuestionmarkOccurence() * 100
        };
        return result;
    }

    private void addEntry(Double similarity, Map<Review, List<Double>> reviewMap, Review reviewA, Review reviewB) {
        putToMap(similarity, reviewMap, reviewA);
        putToMap(similarity, reviewMap, reviewB);
    }

    private void putToMap(Double similarity, Map<Review, List<Double>> reviewMap, Review review) {

        List<Double> list;

        if (reviewMap.containsKey(review)) {
            list = reviewMap.get(review);
        } else {
            list = new LinkedList<>();
        }

        list.add(similarity);
        reviewMap.put(review, list);
    }

}
