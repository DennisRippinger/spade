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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

/**
 * DTO for a ShadowReview. These reviews contain similar data like a {@link Review}, but do not exist in a huge amount in their
 * respective category, such that a text analysis is not suitable.
 * 
 * @author Dennis Rippinger
 * 
 */
@Data
@Entity
@Table(name = "UncategorizedReviews")
public class ShadowReview {

    @Id
    private String id;

    private String authorId;

    @Column(length = 1000)
    private String product;

    private Date date;

    private Double averageRating;

    private Double userRating;

    private Integer helpfulVotes;

    private Integer totalVotes;

    private String type;

    @Lob
    @Column(length = 1000)
    private String content;

}
