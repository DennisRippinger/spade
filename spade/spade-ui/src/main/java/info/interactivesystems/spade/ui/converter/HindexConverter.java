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
 * Converter for h-index related UI Elements.
 * 
 * @author Dennis Rippinger
 */
@Named
public class HindexConverter {

    public String gaugeValue(Double hindex) {
        if (hindex > 3.5) {
            hindex = 3.5;
        }

        Double result = Math.round(100.0 * hindex) / 100.0;

        return result.toString();
    }

    public String colorValue(Double hindex) {
        if (hindex < 0.9) {
            return "green";
        } else if (hindex < 2.0) {
            return "#D2CD00";
        } else {
            return "red";
        }
    }

}
