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

/**
 * @author Dennis Rippinger
 * 
 */
public interface SimilarityCalculator {

    /**
     * Calculates a similarity measurement between to given Strings. It returns a {@link SimilartyMesurement} object which can
     * contain various forms of similarity, e.g. similar sentences, or a numeric value.
     * 
     * @param corpusFirst compared to corpusSecond.
     * @param corpusSecond compared to corpusOne.
     * @return A {@link SimilartyMesurement}.
     */
    SimilartyMesurement calculateSimilarity(String corpusFirst,
        String corpusSecond);

}
