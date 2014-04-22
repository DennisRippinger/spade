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
package info.interactivesystems.spade.sentence;

import java.util.List;

import info.interactivesystems.spade.nlp.SentenceDetector;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * The Class SentenceLength.
 * 
 * @author Dennis Rippinger
 */
@Component
public class WordCountCalculator {

    @Resource
    private SentenceDetector sentenceDetector;

    /**
     * Gets the word count.
     * 
     * @param review the review
     * @return the word count
     */
    public Integer getWordCount(String review) {
        List<String> sentences = sentenceDetector.detectSentencesFromCorpus(review);
        Integer result = 0;
        for (String sentence : sentences) {
            String[] split = sentence.split(" ");
            result += split.length;
        }

        return result;
    }

}
