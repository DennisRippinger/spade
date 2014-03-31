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
 * The Enum Authority to store the source and nature of the product. I.e. Amazon or Yelp.
 * 
 * @author Dennis Rippinger
 */
public enum Authority {

    AMAZON(1),
    YELP(2);

    private final Integer id;

    /**
     * Private instantiation of a new ProductCategory enum.
     * 
     * @param id the id
     */
    private Authority(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
