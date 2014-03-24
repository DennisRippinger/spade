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

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.SimilartyMesurement;

import java.util.List;

import org.testng.annotations.Test;

/**
 * Tests similarity for the Levenshtein Similarity.
 */
public class LevenshteinSimilarityTest extends SimilarityTest {

    public LevenshteinSimilarityTest() {
        super();
        calculator = new LevenshteinDistance();
        assertThat(calculator).isNotNull();
    }

    @Test
    public void calculateSimilarity_same() {
        List<String> detectedSentences = getTestString();

        for (String testSentence : detectedSentences) {
            SimilartyMesurement calculatedSimilarity = calculator
                .calculateSimilarity(testSentence, testSentence);

            assertThat(calculatedSimilarity.getSimilarty()).isEqualTo(0.0);
        }
    }

    @Test
    public void calculateSimilarity_different() {

        String testStringOne = "I did extensive research before selecting the "
            + "SD600, " + "and I am thrilled with my purchase.";
        String testStringTwo = "I did extensive research before selecting the "
            + "Kodak EasyShare C875, "
            + "and I am thrilled with my purchase.";

        SimilartyMesurement calculatedSimilarity = calculator
            .calculateSimilarity(testStringOne, testStringTwo);

        assertThat(calculatedSimilarity.getSimilarty()).isEqualTo(19.0);
    }
}
