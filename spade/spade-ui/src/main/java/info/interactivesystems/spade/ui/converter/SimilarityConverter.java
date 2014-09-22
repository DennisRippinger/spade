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

import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.ui.dto.DetailsRow;
import info.interactivesystems.spade.ui.util.CopyDirection;

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

    public String getCopyDirection(CopyDirection direction) {

        switch (direction) {
            case FIRST:
                return "fa-long-arrow-left";
            case LATER:
                return "fa-long-arrow-right";
            case SAME:
                return "fa-arrows-h";
            case NONE:
                return "";
            default:
                return "";
        }

    }

    public String getStylometry(DetailsRow detailsRow) {

		if(detailsRow.getWordLength() <= 20){
			return "grey";
		}

        if (detailsRow.getStylometry() > 0.97) {
            return "green";
        } else if (detailsRow.getStylometry()  > 0.80) {
            return "#D2CD00";
        } else {
            return "red";
        }

    }
}