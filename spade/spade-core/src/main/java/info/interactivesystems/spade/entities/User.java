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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a UncategorizedReview.
 * 
 * @author Dennis Rippinger
 * 
 */
@Setter
@Getter
@Entity
@Table(name = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = 7916895135811744472L;

    @Id
    private String id;

    private String name;

    private Integer ranking;

    private Integer helpfulness = 0;

    private Integer helpfulVotes = 0;

    private Integer helpfulOverallVotes = 0;

    private Integer numberOfReviews = 0;

    @Enumerated(EnumType.ORDINAL)
    private Authority authority;

    private Double hIndex;

    private Double length;

    private Long randomID;

}