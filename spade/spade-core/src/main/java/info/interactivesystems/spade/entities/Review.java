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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity for a Review.
 * 
 * @author Dennis Rippinger
 */
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review implements Serializable {

    private static final long serialVersionUID = -2877180343613831483L;

    @Id
    private String id;

    @Lob
    private String content;

    private Double rating;

    private Date reviewDate;

    @Lob
    private String title;

    private Integer helpfulVotes;

    private Integer totalVotes;

    // Calculation Values

    private Integer wordCount;

    private String nilsimsa;

    private Double variance;

	private Double density;

    @Column(name = "meanDifference")
    private Double meanSimilarity;

    @OneToOne(cascade = CascadeType.ALL)
    private Stylometry stylometry;

    @Column(name = "uniquee")
    private boolean unique;

    // Key Mappings
    @ManyToOne
    @JoinColumn(name = "Product_fk")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "User_fk")
    private User user;
}
