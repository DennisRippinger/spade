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

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import de.linguatools.disco.DISCO;

/**
 * The Class SemanticResemblance. TODO Comment
 * 
 * @author Dennis Rippinger
 */
@Slf4j
public class SemanticResemblance {

	private DISCO disco;

	/**
	 * Instantiates a new semantic resemblance.
	 */
	public SemanticResemblance() {
		disco = DiscoFactory.getEnglishCoOcurenceData();
	}

	/**
	 * Gets the first order similarity.
	 * 
	 * @param wordOne
	 *            the word one
	 * @param wordTwo
	 *            the word two
	 * @return the first order similarity
	 */
	public Float getFirstOrderSimilarity(String wordOne, String wordTwo) {
		Float result = 0.0f;

		try {
			result = disco.firstOrderSimilarity(wordOne, wordTwo);
		} catch (IOException e) {
			log.error("Error with words '{}', and '{}', wordOne, wordTwo");
			log.error("Error reading the co-occurence file", e);
		}

		return result;
	}

	/**
	 * Gets the second order similarity.
	 * 
	 * @param wordOne
	 *            the word one
	 * @param wordTwo
	 *            the word two
	 * @return the second order similarity
	 */
	public Float getSecondOrderSimilarity(String wordOne, String wordTwo) {
		Float result = 0.0f;

		try {
			result = disco.secondOrderSimilarity(wordOne, wordTwo);
		} catch (IOException e) {
			log.error("Error with words '{}', and '{}', wordOne, wordTwo");
			log.error("Error reading the co-occurence file", e);
		}

		return result;
	}

}
