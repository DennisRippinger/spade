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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * The Class SentenceSimilarity.
 *
 * @author Dennis Rippinger
 */

/**
 * Instantiates a new sentence similarity.
 */
@Data
@Entity
@Table(name = "Sentence_Similarities")
public class SentenceSimilarity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String referenceReviewId;

    private String targetReviewId;

    @Column(columnDefinition = "TEXT")
    private String referenceSentence;

    @Column(columnDefinition = "TEXT")
    private String targetSentence;

    private Double similarity;

    private Double weightendSimilarity;

}