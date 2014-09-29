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
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.ui.dto.DetailsRow;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Action for the recommender view. View is currently not in use.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Named
@Deprecated
@Scope("session")
public class RecommenderAction implements Serializable {

	private static final long serialVersionUID = 7260210638792959399L;

	@Resource
	private transient ReviewContentService service;

	@Resource
	private transient NilsimsaSimilarityDao nilsimsaDao;

	@Getter
	private List<User> users;

	@Getter
	@Setter
	private List<Review> currentReviews;

	@Getter
	@Setter
	private DetailsRow selectedRow;

	@Getter
	private List<DetailsRow> currentRows;

	@Getter
	private User currentUser;

	@PostConstruct
	public void init() {
		users = service.findUsersWithHIndex(3.0, 30);

		currentReviews = users.get(0).getReviews();
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;

		List<DetailsRow> rows = new ArrayList<>();
		if (currentUser != null) {
			for (Review review : currentUser.getReviews()) {
				DetailsRow row = new DetailsRow();
				row.setProductId(review.getProduct().getId());
				row.setProductName(review.getProduct().getName());
				row.setCategory(review.getProduct().getCategory());
				row.setRating(review.getProduct().getRating());
				row.setReviewDate(review.getReviewDate());
				row.setReviewText(review.getContent());
				row.setReviewTitle(review.getTitle());
				row.setUserRating(review.getRating());

				NilsimsaSimilarity similar = nilsimsaDao.findSimilarityByReviewId(review.getId());
				if (similar != null) {
					row.setSimilar(true);
					row.setSimilarityId(similar.getId());
				} else {
					row.setSimilar(false);
				}

				rows.add(row);
			}
		}
		log.debug("Transferred '{}' items into Detail Row Elements", rows.size());

		currentRows = rows;
	}

}
