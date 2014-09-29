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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * The Automated Readability Index (ARI) is a readability test designed to gauge the understandability of a text.
 *
 * @author Dennis Rippinger
 */
@Component
public class AutomatedReadabilityIndex {

	private static final Double ARI_CONSTANT_ONE = 4.71;
	private static final Double ARI_CONSTANT_TWO = 0.5;
	private static final Double ARI_CONSTANT_THREE = -21.43;

	@Resource
	private SentenceDetector sentenceDetector;

	public Double calculateReadability(String review) {
		List<String> sentences = sentenceDetector.detectSentencesFromCorpus(review);
		Integer countSentences = sentences.size();

		Double countWords = 0.0;
		Double countCharacters = 0.0;

		for (String sentence : sentences) {
			String[] words = sentence.split(" ");
			countWords += words.length;
			for (String word : words) {
				countCharacters += word.length();
			}
		}

		Double pairOne = ARI_CONSTANT_ONE * (countCharacters / countWords);
		Double pairTwo = ARI_CONSTANT_TWO * (countWords / countSentences);

		return pairOne + pairTwo + ARI_CONSTANT_THREE;
	}

}
