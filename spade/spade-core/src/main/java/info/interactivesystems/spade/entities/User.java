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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * User Entity.
 *
 * @author Dennis Rippinger
 */
@Setter
@Getter
@Entity
@Table(name = "users", indexes = {@Index(columnList = "randomID"), @Index(columnList = "hIndex"), @Index(columnList = "meanDifference")})
public class User implements Serializable {

	private static final long serialVersionUID = 7916895135811744472L;

	@Id
	private String id;

	private Long randomID;

	@Lob
	private String name;

	private Integer numberOfReviews = 0;

	private Integer helpfulness = 0;

	private Integer helpfulVotes = 0;

	private Integer helpfulOverallVotes = 0;

	private Double hIndex;

	@Column(name = "meanDifference")
	private Double meanStylometry;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Review> reviews;

}