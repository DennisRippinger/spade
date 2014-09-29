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

import info.interactivesystems.spade.PropertyTestUtil;
import info.interactivesystems.spade.nlp.SentenceDetector;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The Class SimilarityTest.
 *
 * @author Dennis Rippinger
 */
public abstract class SimilarityTest {
	protected final String testStringOne = "I did extensive research before selecting the SD600, "
			+ "and I am thrilled with my purchase.";
	protected final String testStringTwo = "I did extensive research before selecting the Kodak EasyShare C875, "
			+ "and I am thrilled with my purchase.";
	protected SimilarityCalculator calculator;
	protected XMLConfiguration exampleSentences;
	protected SentenceDetector sentenceDetector;

	SimilarityTest() {
		sentenceDetector = new SentenceDetector();
		exampleSentences = PropertyTestUtil.getExampleSentences();
		assertThat(sentenceDetector).isNotNull();
		assertThat(exampleSentences).isNotNull();
	}

	/**
	 * Get some test String from the test repository.
	 *
	 * @return the test string
	 */
	protected List<String> getTestString() {
		String case1 = exampleSentences.getString("cases.one.data");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case1);
		return detectedSentences;
	}

}
