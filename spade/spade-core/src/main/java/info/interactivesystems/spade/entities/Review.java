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
@Table(name = "Reviews")
public class Review implements Serializable {

    private static final long serialVersionUID = -2877180343613831483L;

    @Id
    private String id; //a

    private String authorId; //a

    private String product; 

    private String content; //a

    private Double rating; //a

    private Date reviewDate; //a

    private String source; //a
    
    private String category;

    // Amazon Specific

    private String title; //a

    private Integer comments;//a

    private Integer helpfulVotes; //a

    private Integer totalVotes; //a

    private Boolean verified = false;  //a

    // private String category;

    // Yelp specific
    private Integer checkins; //a

    private Integer voteUseful; //a

    private Integer voteFunny; //a

    private Integer voteCool; //a

    // Calculation Values

    private Double ari; //a

    private Double gfi; //a

    private Double density; //a
    
    private Double densityRelation;

    private Integer wordCount; //a

    private String nilsimsa; //a

    private Double variance; //a
}
