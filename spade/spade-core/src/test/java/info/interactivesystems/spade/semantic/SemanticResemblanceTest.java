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
package info.interactivesystems.spade.semantic;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * The Class SemanticResemblanceTest.
 * 
 * @author Dennis Rippinger
 */
@Test(enabled=false)
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class SemanticResemblanceTest extends AbstractTestNGSpringContextTests {

    @Resource
    private SemanticResemblance semanticResemblance;

    private static final String WORD_ONE = "love";
    private static final String WORD_TWO = "feel";

    @Test
    public void getFirstOrderSimilarity() {
        Float firstOrderSimilarity = semanticResemblance
            .getFirstOrderSimilarity(WORD_ONE, WORD_TWO);
        System.out.println("First: " + firstOrderSimilarity);
    }

    @Test
    public void getSecondOrderSimilarity() {
        Float secondOrderSimilarity = semanticResemblance
            .getSecondOrderSimilarity(WORD_ONE, WORD_TWO);
        System.out.println("Second: " + secondOrderSimilarity);
    }

    @Test
    public void getgetSimilarWords() {
        Map<String, String> similarWords = semanticResemblance
            .getSimilarWords(WORD_ONE);

        for (Entry<String, String> word : similarWords.entrySet()) {
            System.out.println(word.getKey() + " " + word.getValue());
        }
    }

}
