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

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Component;

/**
 * The first calculations of NilsimsaSimilartyCalculator returned a huge result set. This calculations adds relevant features to
 * the entity.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class NilsimsaEnricher {

    @Resource
    private NilsimsaSimilarityDao nisimsaDao;

    @Resource
    private ReviewContentService service;

    public void enrichNilsimsaValues(Double range) {
        List<NilsimsaSimilarity> relevantSet = nisimsaDao.findWithinRange(range);

        log.info("Found '{}' Similarity Items within range", relevantSet.size());

        for (NilsimsaSimilarity similarity : relevantSet) {
            Review reviewA = service.findReview(similarity.getProductA());
            Review reviewB = service.findReview(similarity.getProductB());

            Integer daysBetween = calculateDifference(reviewA.getReviewDate(), reviewB.getReviewDate());
            Boolean sameAuthor = calculateSameAuthorshit(reviewA, reviewB);
            Integer wordDistance = calculateWordDistance(reviewA, reviewB);

            similarity.setDayDistance(daysBetween);
            similarity.setSameAuthor(sameAuthor);
            similarity.setWordDistance(wordDistance);

            similarity.setProccesed(true);

            nisimsaDao.save(similarity);
        }

    }

    private Integer calculateWordDistance(Review reviewA, Review reviewB) {
        Integer difference = reviewA.getWordCount() - reviewB.getWordCount();
        if (difference < 0) {
            difference = difference * -1;
        }
        return difference;
    }

    private Boolean calculateSameAuthorshit(Review reviewA, Review reviewB) {
        return reviewA.getAuthorId().equals(reviewB.getAuthorId());
    }

    private Integer calculateDifference(Date reviewDate, Date reviewDate2) {
        Integer days = Days.daysBetween(new DateTime(reviewDate), new DateTime(reviewDate2)).getDays();
        if (days < 0) {
            days = days * -1;
        }
        return days;
    }

}
