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

/**
 * The Class NilsimsaSimilarity.
 *
 * @author Dennis Rippinger
 */
@Entity
@Setter
@Getter
@Table(
		name = "similarities",
		indexes = {
				@Index(columnList = "sameAuthor"),
				@Index(columnList = "similarity"),
				@Index(columnList = "category")})
public class NilsimsaSimilarity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private Review reviewA;

	@OneToOne
	private Review reviewB;

	private Double similarity;

	private String category;

	private boolean sameAuthor;

	private Integer dayDistance;

	private Integer wordDistance;

	@Column(name = "occurrences_a")
	private Integer occurrencesA;

	@Column(name = "occurrences_b")
	private Integer occurrencesB;

	@OneToOne
	private User userA;

	@OneToOne
	private User userB;

}