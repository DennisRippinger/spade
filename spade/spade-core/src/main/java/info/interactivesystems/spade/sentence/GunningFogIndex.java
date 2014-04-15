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

import info.interactivesystems.spade.nlp.SentenceDetector;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * The Class GunningFogIndex.
 * 
 * @author Dennis Rippinger
 */
@Component
public class GunningFogIndex {

    private static final char[] vowels = { 'a', 'e', 'i', 'o', 'u', 'y', 'A', 'E', 'I', 'O', 'U', 'Y' };
    private static final String[] commonSuffixes = { "es", "ed", "ing", "ic", "er", "en",
        "al", "est", "ity", "ty", "less", "ly", "ment" };
    private static final Double GFI_MULT = 0.4;
    private static final Double GFI_PERC = 100.0;

    @Resource
    private SentenceDetector sentenceDetector;

    public Double calculateReadability(String review) {
        List<String> sentences = sentenceDetector.detectSentencesFromCorpus(review);
        Integer countSentences = sentences.size();

        Double countWords = 0.0;
        Double countComplexWords = 0.0;

        for (String sentence : sentences) {
            String[] words = sentence.split(" ");
            for (String string : words) {
                if (isCompexWord(string)) {
                    countComplexWords++;
                }
                countWords++;
            }
        }

        Double result = GFI_MULT * ((countWords / countSentences) + (GFI_PERC * (countComplexWords / countWords)));

        return result;
    }

    private Boolean isCompexWord(String word) {
        // Remove common suffixes
        String clean = removeCommonSuffixes(word);
        Integer countVovel = 0;
        for (char input : clean.toCharArray()) {
            if (isVowel(input)) {
                countVovel++;
            }
        }
        if (countVovel >= 3) {
            return true;
        }
        return false;
    }

    private String removeCommonSuffixes(String word) {
        if (word.length() > 6) {
            for (String suffix : commonSuffixes) {
                if (word.endsWith(suffix)) {
                    word = word.substring(0, word.lastIndexOf(suffix));
                    continue;
                }
            }
        }
        return word;
    }

    private Boolean isVowel(char input) {

        for (char ch : vowels) {
            if (input == ch) {
                return true;
            }
        }

        return false;
    }
}
