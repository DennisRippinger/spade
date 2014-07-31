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
package info.interactivesystems.spade.ui.dto;

import info.interactivesystems.spade.ui.util.CopyDirection;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Simple bean to store row content.
 * 
 * @author Dennis Rippinger
 */
@Data
public class DetailsRow implements Serializable {

    private static final long serialVersionUID = 8677667436570421318L;

    private String productId;

    private String productName;

    private String category;

    private Date reviewDate;

    private Double userRating;

    private Double rating;

    private boolean similar;
    
    private Double stylometry;

    private String reviewTitle;

    private String reviewText;

    private Long similarityId;
    
    private CopyDirection direction;

}
