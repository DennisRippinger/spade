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
package info.interactivesystems.spade.crawler.util;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class PropertyUtil.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
public final class PropertyUtil {

    /**
     * Gets the integer value of a system property.
     * 
     * @param property the system property
     * @param defaultValue the default value in case a property is not set.
     * @return default or property value as integer.
     */
    public static Integer getIntegerProperty(String property, Integer defaultValue) {
        String propertyValue = System.getProperty(property);
        if (propertyValue != null && !propertyValue.isEmpty()) {
            try {
                defaultValue = Integer.parseInt(propertyValue);
            } catch (NumberFormatException e) {
                log.warn("Could not parse property '{}'", property);
            }
        }
        return defaultValue;
    }

}
