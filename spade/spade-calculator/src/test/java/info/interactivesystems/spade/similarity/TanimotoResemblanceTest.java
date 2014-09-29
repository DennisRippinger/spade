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

import info.interactivesystems.spade.dto.SimilartyMesurement;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The Class TanimotoResemblanceTest.
 *
 * @author Dennis Rippinger
 */
@Test(groups = {"functionTest"})
public class TanimotoResemblanceTest extends SimilarityTest {

	public TanimotoResemblanceTest() {
		super();
		calculator = new TanimotoResemblance();
		assertThat(calculator).isNotNull();
	}

	@Test
	public void calculateSimilarity_same() {
		List<String> detectedSentences = getTestString();

		for (String testSentence : detectedSentences) {
			SimilartyMesurement calculatedSimilarity = calculator
					.calculateSimilarity(testSentence, testSentence);

			assertThat(calculatedSimilarity.getSimilarty()).isEqualTo(1.0);
		}
	}

	@Test
	public void calculateSimilarity_different() {

		SimilartyMesurement calculatedSimilarity = calculator
				.calculateSimilarity(testStringOne, testStringTwo);

		assertThat(calculatedSimilarity.getSimilarty()).isBetween(0.7, 0.8);
	}
}
