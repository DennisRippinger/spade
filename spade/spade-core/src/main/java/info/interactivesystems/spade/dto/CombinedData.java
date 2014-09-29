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
package info.interactivesystems.spade.dto;

import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Combined Data DTO is used to store all relevant Entities of the import
 * process to be piped within a LinkedBlockingQueue.
 *
 * @param user    the user
 * @param review  the review
 * @param product the product
 */
@Data
@AllArgsConstructor
public class CombinedData {

	private User user;
	private Review review;
	private Product product;

}
