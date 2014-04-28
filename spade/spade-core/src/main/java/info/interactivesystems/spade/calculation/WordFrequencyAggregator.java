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
import info.interactivesystems.spade.dao.WordFrequencyDao;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.WordFrequency;
import info.interactivesystems.spade.nlp.SentenceDetector;
import info.interactivesystems.spade.util.Authority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * The Class WordFrequencyAggregator.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class WordFrequencyAggregator {

    private static final Pattern wordPattern = Pattern.compile("[\\w']+");

    @Resource
    private ReviewDao reviewDao;

    @Resource
    private WordFrequencyDao wordFrequencyDao;

    @Resource
    private SentenceDetector sentenceDetector;

    private Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();

    public void aggregateWordFrequency(Authority authority) {
        Review currentReview = new Review();
        for (Integer reviewID = 1; currentReview == null; reviewID++) {

            // Amazon Case currently hard coded
            currentReview = reviewDao.find(String.format("R%010d", reviewID));
            if (currentReview != null) {
                extractWordFrequencies(currentReview);
                logSometimes(reviewID);
            }
        }
        log.info("Found '{}' distinct words", wordFrequencies.size());

        persistFrequencies(authority);
    }

    private void extractWordFrequencies(Review currentReview) {
        List<String> sentences = sentenceDetector.detectSentencesFromCorpus(currentReview.getContent());

        for (String sentence : sentences) {
            Matcher matcher = wordPattern.matcher(sentence);
            while (matcher.find()) {
                incrementWordCount(matcher.group());
            }
        }
    }

    private void incrementWordCount(String word) {
        Integer frequency = wordFrequencies.get(word);
        if (frequency != null) {
            frequency++;
            wordFrequencies.put(word, frequency);

            return;
        }
        wordFrequencies.put(word, 1);
    }

    private void logSometimes(Object current) {
        Integer rand = ThreadLocalRandom.current().nextInt(1, 5000);
        if (rand == 2500) {
            log.info("Current Item '{}'", current);
        }

    }

    private void persistFrequencies(Authority authority) {
        for (Entry<String, Integer> wordFrequencyPair : wordFrequencies.entrySet()) {
            WordFrequency wordFrequency = new WordFrequency();
            wordFrequency.setWord(wordFrequencyPair.getKey());
            wordFrequency.setFrequency(wordFrequencyPair.getValue());
            wordFrequency.setAuthority(authority);

            wordFrequencyDao.save(wordFrequency);

            logSometimes(wordFrequencyPair.getKey());
        }

    }

}
