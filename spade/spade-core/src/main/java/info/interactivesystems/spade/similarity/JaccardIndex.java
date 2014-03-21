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

import info.interactivesystems.spade.entities.SimilartyMesurement;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * Calculates a String based Jaccard Index for two Strings. The result ranges
 * between 0 and 1, where 1 is equality.
 * 
 * @author Dennis Rippinger
 * 
 */
public class JaccardIndex implements SimilarityCalculator {

	/**
	 * Calculates the Jaccard Index for two String. The result ranges between 0
	 * and 1, where 1 is equality.
	 */
	@Override
	public SimilartyMesurement calculateSimilarity(String corpusFirst,
			String corpusSecond) {
		Set<String> setCurpusFirst = getSetOfString(corpusFirst);
		Set<String> setCurpusSecound = getSetOfString(corpusSecond);

		Set<String> intersection = new HashSet<String>(setCurpusFirst);
		intersection.retainAll(setCurpusSecound);

		Set<String> merged = new HashSet<String>(setCurpusFirst);
		merged.addAll(setCurpusSecound);

		Double conclusion = intersection.size() * 1.0 / merged.size() * 1.0;

		SimilartyMesurement result = new SimilartyMesurement();

		result.setSimilarty(conclusion);

		return result;
	}

	private Set<String> getSetOfString(String corpus) {
		StringTokenizer tokenizer = new StringTokenizer(corpus);
		Set<String> result = new HashSet<String>();

		while (tokenizer.hasMoreElements()) {
			result.add(tokenizer.nextToken());
		}

		return result;
	}
}
