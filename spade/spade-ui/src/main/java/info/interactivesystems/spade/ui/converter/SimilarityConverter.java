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
package info.interactivesystems.spade.ui.converter;

import javax.inject.Named;

/**
 * The Class SimilarityConverter.
 * 
 * @author Dennis Rippinger
 */
@Named
public class SimilarityConverter {

    /**
     * Returns a Font Awesome element defining the similarity.
     * 
     * @param similar the similar
     * @return the similarity
     */
    public String getSimilarity(Boolean similar) {

        if (similar) {
            return "sameTable";
        } else {
            return "differentTable";
        }

    }

    public String getStylometry(Double stylometry) {
        if (stylometry > 0.97) {
            return "green";
        } else if (stylometry > 0.80) {
            return "#D2CD00";
        } else {
            return "red";
        }

    }
}