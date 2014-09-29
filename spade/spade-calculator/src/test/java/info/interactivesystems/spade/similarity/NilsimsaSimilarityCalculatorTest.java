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

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * The Class NilsimsaSimilarityCalculatorTest.
 *
 * @author Dennis Rippinger
 */
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class NilsimsaSimilarityCalculatorTest extends AbstractTestNGSpringContextTests {

	@Resource
	private NilsimsaSimilarityCalculator calculator;

	private String[] categories = {
			"Magazine Subscriptions"
//        "Furniture & Décor",
//        "Collectibles",
//        "Car Electronics",
//        "Purchase Circles",
//        "Camera & Photo",
//        "Office & School Supplies",
//        "GPS & Navigation",
//        "MP3 Players & Accessories",
//        "Computers",
//        "Home Improvement",
//        "Miscellaneous",
//        "Appliances",
//        "Kitchen & Dining",
//        "All Electronics",
//        "Magazine Subscriptions",
//        "Arts"
	};

	@Test
	public void calculateSimilarityBetweenUniqueReviews() {
		for (String category : categories) {
			calculator.calculateSimilarityBetweenUniqueReviews(category);
		}

	}
}
