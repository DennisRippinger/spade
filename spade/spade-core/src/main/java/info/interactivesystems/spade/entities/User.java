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

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * DTO for a UncategorizedReview.
 * 
 * @author Dennis Rippinger
 * 
 */
@Setter
@Getter
@Document
public class User extends AbstractTimestampEntity implements Serializable {

    private static final long serialVersionUID = 7916895135811744472L;

    @Id
    private String id;

    private String name;

    private Integer ranking;

    private Integer helpfulness = 0;

    private Integer helpfulVotes = 0;

    private Integer helpfulOverallVotes = 0;

    private Integer numberOfReviews = 0;

    private Authority authority;
    
    @Indexed
    private Double hIndex;
    
    @Indexed
    private Double recLength;

    @Indexed(unique = true)
    private Long randomID;

    private ConcurrentBit concurrentBit = ConcurrentBit.UNPROCESSED;

}