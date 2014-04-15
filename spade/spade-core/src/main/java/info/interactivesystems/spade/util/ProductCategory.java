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
package info.interactivesystems.spade.util;

/**
 * Describes a product category with their respective database identifier.
 * 
 * @author Dennis Rippinger
 * 
 */
public enum ProductCategory {

    // AMAZON
    BLURAY_PLAYER(0),
    CAMCORDER(1),
    DIGITAL_CAMERA(2),
    MOBILEPHONE(3),
    PC_SYSTEM(4),
    PRINTER(5),
    TV(6),
    VIDEOPROJECTOR(7),

    // YELP
    RESTAURANT(8),
    FOOD(9),
    NIGHTLIFE(10),
    SHOPPING(11),
    UNKNOWN(99);

    private final Integer id;

    /**
     * Private instantiation of a new ProductCategory enum.
     * 
     * @param id the id
     */
    private ProductCategory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getURLString() {
        
        switch (id) {
            case 8:
                return "restaurants";
            case 9:
                return "food";
            case 10:
                return "nightlife";
            case 11:
                return "shopping";
            default:
                return "";
        }
    }

}
