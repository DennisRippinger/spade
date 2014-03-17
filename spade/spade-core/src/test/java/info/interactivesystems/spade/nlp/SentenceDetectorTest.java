// Copyright 2014 Dennis Rippinger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package info.interactivesystems.spade.nlp;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.PropertyUtil;
import info.interactivesystems.spade.nlp.SentenceDetector;

import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.testng.annotations.Test;

/**
 * Tests the current NLP sentence model.
 * 
 * @author Dennis Rippinger
 */
public class SentenceDetectorTest {

	private SentenceDetector sentenceDetector;
	private XMLConfiguration exampleSentences;

	/**
	 * Instantiates a new sentence detector test.
	 */
	public SentenceDetectorTest() {
		sentenceDetector = new SentenceDetector();
		exampleSentences = PropertyUtil.getExampleSentences();

		assertThat(sentenceDetector).isNotNull();
		assertThat(exampleSentences).isNotNull();
	}

	@Test
	public void detectSentencesFromCorpusCase_1() {
		String case1 = exampleSentences.getString("cases.one.data");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case1);

		Integer requiredResult = exampleSentences.getInt("cases.one.result");

		assertThat(detectedSentences.size()).isEqualTo(requiredResult);
	}

	@Test
	public void detectSentencesFromCorpusCase_2() {
		String case2 = exampleSentences.getString("cases.two.data");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case2);

		Integer requiredResult = exampleSentences.getInt("cases.two.result");

		assertThat(detectedSentences.size()).isEqualTo(requiredResult);
	}

	@Test
	public void detectSentencesFromCorpusCase_3() {
		String case3 = exampleSentences.getString("cases.three.data");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case3);

		Integer requiredResult = exampleSentences.getInt("cases.three.result");

		assertThat(detectedSentences.size()).isEqualTo(requiredResult);
	}
}
