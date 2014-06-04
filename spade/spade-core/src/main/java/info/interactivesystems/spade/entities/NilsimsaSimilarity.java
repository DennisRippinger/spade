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

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Class NilsimsaSimilarity.
 * 
 * @author Dennis Rippinger
 */
@Setter
@Getter
@Document
public class NilsimsaSimilarity {

    @Indexed
    private String productA;

    @Indexed
    private String productB;

    @Indexed
    private Double similarity;

    @Indexed
    private String category;

}
