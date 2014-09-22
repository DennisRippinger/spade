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
package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.ui.dto.DetailsRow;
import info.interactivesystems.spade.ui.util.CopyDirection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UserAction.
 * 
 * @author Dennis Rippinger
 */
@Named
@ViewScoped
public class UserAction implements Serializable {

    private static final long serialVersionUID = -503276225717002437L;

    @Resource
    private transient NilsimsaSimilarityDao nilsimsaDao;

    @Setter
    @Getter
    private User user;

    @Getter
    @Setter
    private DetailsRow selectedRow;

    @Getter
    private List<DetailsRow> currentRows;

    public void init() {

		// Wrapper needed to be sortable within the UI.

        List<DetailsRow> rows = new ArrayList<>();
        if (user != null) {
            for (Review review : user.getReviews()) {
                DetailsRow row = new DetailsRow();
                row.setProductId(review.getProduct().getId());
                row.setProductName(review.getProduct().getName());
                row.setCategory(review.getProduct().getCategory());
                row.setRating(review.getProduct().getRating());
                row.setReviewDate(review.getReviewDate());
                row.setReviewText(review.getContent());
                row.setReviewTitle(review.getTitle());
                row.setUserRating(review.getRating());
				row.setWordLength(review.getWordCount());
                row.setStylometry(review.getMeanSimilarity() != null ? review.getMeanSimilarity() : 1.0);

                NilsimsaSimilarity similar = nilsimsaDao.findSimilarityByReviewId(review.getId());
                if (similar != null) {
                    row.setSimilar(true);
                    row.setSimilarityId(similar.getId());

                    getCopyDirection(review, row, similar);

                } else {
                    row.setSimilar(false);
                    row.setDirection(CopyDirection.NONE);
                }

                rows.add(row);
            }
        }

        currentRows = rows;
    }

    private void getCopyDirection(Review review, DetailsRow row, NilsimsaSimilarity similar) {
        Review otherReview;
        if (similar.getReviewA().getId().equals(review.getId())) {
            otherReview = similar.getReviewB();
        } else {
            otherReview = similar.getReviewA();
        }

        Integer compare = review.getReviewDate().compareTo(otherReview.getReviewDate());

        if (compare.equals(0)) {
            row.setDirection(CopyDirection.SAME);
        } else if (compare > 0) {
            row.setDirection(CopyDirection.LATER);
        } else if (compare < 0) {
            row.setDirection(CopyDirection.FIRST);
        }
    }
}
