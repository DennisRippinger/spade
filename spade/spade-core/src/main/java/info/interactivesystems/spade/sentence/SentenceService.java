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
package info.interactivesystems.spade.sentence;

import javax.annotation.Resource;

import lombok.NonNull;

import org.springframework.stereotype.Component;

/**
 * The Class SentenceService.
 * 
 * @author Dennis Rippinger
 */
@Component
public class SentenceService {

    @Resource
    private AutomatedReadabilityIndex ari;

    @Resource
    private GunningFogIndex gfi;

    @Resource
    private InfoDensityIndex idi;

    /**
     * Calculates the AR Index of a given review.
     * 
     * @param review a review.
     * @return the AR Index.
     */
    public Double calculateARIndex(@NonNull String review) {
        return ari.calculateReadability(review);
    }

    /**
     * Calculates the GF Index of a given review.
     * 
     * @param review a review.
     * @return the GF Index.
     */
    public Double calculateGFIndex(@NonNull String review) {
        return gfi.calculateReadability(review);
    }

    /**
     * Calculates the Information density of a review. <br/>
     * Returns values on an interval [0,1].
     * 
     * @param review a review.
     * @return the information density.
     */
    public Double calculateInformationDensity(@NonNull String review) {
        return idi.getInformationDensity(review);
    }
}
