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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Named;

/**
 * The Class StarRatingConverter.
 * 
 * @author Dennis Rippinger
 */
@Named
public class StarRatingConverter {

    private Map<Double, String> ratings = new LinkedHashMap<>();

    @PostConstruct
    void init() {
        ratings.put(0.75, "a-star-0-5");
        ratings.put(1.25, "a-star-1");
        ratings.put(1.75, "a-star-1-5");
        ratings.put(2.25, "a-star-2");
        ratings.put(2.75, "a-star-2-5");
        ratings.put(3.25, "a-star-3");
        ratings.put(3.75, "a-star-3-5");
        ratings.put(4.25, "a-star-4");
        ratings.put(4.75, "a-star-4-5");
    }

    /**
     * Returns a CSS Class defining the star rating from the amazon sprite.
     */
    public String getAsString(Double rating) {

        for (Entry<Double, String> entry : ratings.entrySet()) {
            if (rating < entry.getKey()) {
                return entry.getValue();
            }
        }

        return "a-star-5";

    }

}