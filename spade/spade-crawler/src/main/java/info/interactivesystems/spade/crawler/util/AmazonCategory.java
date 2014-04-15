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

/**
 * Enum provides internal amazon category numbers.
 * 
 * @author Dennis Rippinger
 */
public enum AmazonCategory {

    DIGITALCAMERA("281052"),
    CAMCORDER("172421"),
    BLURAYPLAYER("352697011"),
    VIDEOPROJECTOR("300334"),
    MOBILEPHONE("2407749011"),
    PRINTER("172646"), // only inkjet printer
    PCSYSTEM("565098"),
    TV("172659");

    private String id;

    private AmazonCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
