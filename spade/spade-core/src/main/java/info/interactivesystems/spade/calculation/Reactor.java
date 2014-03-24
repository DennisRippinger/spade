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
package info.interactivesystems.spade.calculation;

import info.interactivesystems.spade.dao.ReviewDao;
import info.interactivesystems.spade.dao.SentenceFrequencyDao;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.SentenceFrequency;
import info.interactivesystems.spade.entities.SentenceSimilarity;
import info.interactivesystems.spade.entities.SimilartyMesurement;
import info.interactivesystems.spade.nlp.SentenceDetector;
import info.interactivesystems.spade.similarity.TanimotoResemblance;
import info.interactivesystems.spade.util.HashUtil;
import info.interactivesystems.spade.util.ProductCategory;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * The Class Reactor.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class Reactor {

    @Resource
    private TanimotoResemblance similarityCalculator;

    @Resource
    private SentenceDetector sentenceDetector;

    @Resource
    private SentenceFrequencyDao frequencyDao;

    @Resource
    private ReviewDao reviewDao;

    public List<SentenceSimilarity> agregateSentenceSimilarity(ProductCategory productCategory) {
        List<SentenceSimilarity> result = new LinkedList<SentenceSimilarity>();
        List<Review> reviews = reviewDao.getReviewsOfCategory(productCategory);
        try {
            for (Review outerReview : reviews) {

                List<String> referenceSentences = sentenceDetector.detectSentencesFromCorpus(outerReview.getContent());

                for (Review innerReview : reviews) {
                    if (!outerReview.equals(innerReview)) {
                        List<SentenceSimilarity> similarSentences;

                        similarSentences = getSimilarSentences(referenceSentences, outerReview, innerReview, productCategory);
                        result.addAll(similarSentences);

                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not Hash value", e);
        }
        return result;
    }

    private List<SentenceSimilarity> getSimilarSentences(List<String> referenceSentences, Review referenceReview, Review targetReview, ProductCategory productCategory)
        throws NoSuchAlgorithmException {
        List<SentenceSimilarity> result = new LinkedList<SentenceSimilarity>();

        List<String> targetSentences = sentenceDetector.detectSentencesFromCorpus(targetReview.getContent());
        for (String referenceSentence : referenceSentences) {

            String hash = HashUtil.sha1(referenceSentence);
            SentenceFrequency frequency = frequencyDao.find(hash, productCategory);
            if (frequency.getFrequency() < 0.000004) {
                continue;
            }

            for (String targetSentence : targetSentences) {

                if (targetSentence.length() >= 30) {
                    SimilartyMesurement calculateSimilarity = similarityCalculator.calculateSimilarity(referenceSentence, targetSentence);

                    if (calculateSimilarity.getSimilarty() >= TanimotoResemblance.THRESHOLD) {
                        SentenceSimilarity value = new SentenceSimilarity();
                        value.setReferenceReviewId(referenceReview.getId());
                        value.setTargetReviewId(targetReview.getId());
                        value.setReferenceSentence(referenceSentence);
                        value.setTargetSentence(targetSentence);
                        value.setSimilarity(calculateSimilarity.getSimilarty());
                        result.add(value);

                        System.out.println(value);
                    }
                }
            }
        }

        return result;
    }

}
