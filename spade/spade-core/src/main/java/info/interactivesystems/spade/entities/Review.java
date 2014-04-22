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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a Review.
 * 
 * @author Dennis Rippinger
 */

@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review extends AbstractTimestampEntity implements Serializable {

    private static final long serialVersionUID = -2877180343613831483L;

    @Id
    private String id;

    private String authorId;

    private String content;

    private Double rating;

    private Date reviewDate;

    private String source;

    // Amazon Specific

    private String title;

    private Integer comments = 0;

    private Integer helpfulVotes = 0;

    private Integer totalVotes = 0;

    private Boolean verified = false;

    // Yelp specific
    private Integer checkins = 0;

    private Integer voteUseful = 0;

    private Integer voteFunny = 0;

    private Integer voteCool = 0;

    // Calculation Values

    private Double ari;

    private Double gfi;
    
    private Double density;

    // Key Mapping
    @ManyToOne
    @JoinColumn(name = "Product_fk")
    private Product product;

}
