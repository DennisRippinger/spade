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
 * Entity for an opinion spam rating.
 *
 * @author Dennis Rippinger
 */
@Setter
@Getter
@Entity
@Table(name = "votings", indexes = {@Index(columnList = "sessionID")})
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String sessionID;

	@OneToOne
	private Review review;

	@OneToOne
	private User user;

	private Double similarity;

    /*
	 * Options
     */

	private boolean unknownEditFunction = false;

	private boolean angryCustomer = false;

	private boolean lazyUser = false;

	private boolean ospam = false;

	private boolean advertising = false;

	private boolean reviewIsNotReason = false;

	private boolean somethingElse = false;

}