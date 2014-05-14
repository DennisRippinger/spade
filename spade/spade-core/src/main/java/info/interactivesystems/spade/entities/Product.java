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
package info.interactivesystems.spade.entities;

import info.interactivesystems.spade.util.Authority;
import info.interactivesystems.spade.util.ConcurrentBit;
import info.interactivesystems.spade.util.PriceCategory;
import info.interactivesystems.spade.util.ProductCategory;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entitiy for a Product.
 * 
 * @author Dennis Rippinger
 */
@Getter
@Setter
@Document
public class Product extends AbstractTimestampEntity implements Serializable {

    private static final long serialVersionUID = 7936029047258589542L;

    @Id
    private String id;

    private String name;

    private ProductCategory type;

    @Indexed
    private String category;

    private Double rating;

    private String imageUrl;

    private String source;

    private Double price;

    private Integer noOfReviews;

    // Venues only
    private String location;

    private PriceCategory priceCategory;

    private Authority authority;

    // Temp
    @Indexed(unique = true)
    private Long randomID;

    private ConcurrentBit concurrentBit = ConcurrentBit.UNPROCESSED;

}
