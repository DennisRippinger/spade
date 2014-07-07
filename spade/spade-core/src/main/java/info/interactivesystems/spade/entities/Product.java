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
import info.interactivesystems.spade.util.PriceCategory;
import info.interactivesystems.spade.util.ProductCategory;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entitiy for a Product.
 * 
 * @author Dennis Rippinger
 */
@Getter
@Setter
@Entity
@Table(name = "products", indexes = { @Index(columnList = "randomID"), @Index(columnList = "category") })
public class Product implements Serializable {

    private static final long serialVersionUID = 7936029047258589542L;

    @Id
    private String id;

    @Lob
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private ProductCategory type;

    private String category;

    private Double rating;

    private String imageUrl;

    private String source;

    private Double price;

    private Integer noOfReviews;

    // Venues only
    private String location;

    @Enumerated(EnumType.ORDINAL)
    private PriceCategory priceCategory;

    @Enumerated(EnumType.ORDINAL)
    private Authority authority;

    // Temp
    private Long randomID;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Review> reviews;

}
