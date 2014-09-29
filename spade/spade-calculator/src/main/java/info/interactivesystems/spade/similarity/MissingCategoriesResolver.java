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
package info.interactivesystems.spade.similarity;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * The Class MissingCategoriesResolver.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class MissingCategoriesResolver {

	@Resource
	private ReviewContentService service;

	@Resource
	private NilsimsaSimilarityDao similartyDao;

	@Resource
	private NilsimsaSimilarityDao nisimsaDao;

	@Resource
	private NilsimsaHash hash;

	public void resolveMissingCategory(String category, List<String> suspiciousUsers, boolean partitialyScanned) {

		List<Review> categorryReviews = service.findReviewsByCategory(category);

		for (Integer counter = 0; counter < suspiciousUsers.size(); counter++) {
			log.info("Category: '{}'. User '{}': {} / {}", category, suspiciousUsers.get(counter), counter, suspiciousUsers.size());

			List<Review> userReviewsInCategory = service.findReviewFromUserInCategory(suspiciousUsers.get(counter), category);
			if (!userReviewsInCategory.isEmpty()) {
				calculateSimilarities(categorryReviews, userReviewsInCategory, category, partitialyScanned);
			}

		}

		categorryReviews = null;
		System.gc();
	}

	private void calculateSimilarities(List<Review> allCategoryReviews, List<Review> userReviewsInCategory, String category, boolean partitialyScanned) {

		for (Review userReview : userReviewsInCategory) {

			for (Review categoryReview : allCategoryReviews) {

				if (userReview.getId().equals(categoryReview.getId())) {
					continue;
				}

				Integer sameBits = hash.compare(userReview.getNilsimsa(), categoryReview.getNilsimsa());
				Double percentage = sameBits / 128.0;

				if (percentage >= 0.85) {

                    /*
					 * Check if pair already exists
                     */
					if (partitialyScanned) {
						NilsimsaSimilarity existingPair = nisimsaDao.findSimilarityByReviewId(userReview.getId(), categoryReview.getId());
						if (existingPair != null) {
							continue;
						}
					}

                    /*
                     * DAO for this resolver only returns a minimal projection of a review. Therefore more is loaded on success.
                     */

					Review realUserReview = service.findReview(userReview.getId());
					Review realCategoryReview = service.findReview(categoryReview.getId());

					NilsimsaSimilarity similarity = new NilsimsaSimilarity();

					similarity.setReviewA(realUserReview);
					similarity.setReviewB(realCategoryReview);
					similarity.setSimilarity(percentage);
					similarity.setCategory(category);

					Integer daysBetween = calculateDifference(realUserReview.getReviewDate(), realCategoryReview.getReviewDate());
					Boolean sameAuthor = calculateSameAuthorship(realUserReview, realCategoryReview);
					Integer wordDistance = calculateWordDistance(realUserReview, realCategoryReview);

					similarity.setDayDistance(daysBetween);
					similarity.setSameAuthor(sameAuthor);
					similarity.setWordDistance(wordDistance);

					similarity.setUserA(realUserReview.getUser());
					similarity.setUserB(realCategoryReview.getUser());

					nisimsaDao.save(similarity);
				}
			}
		}
	}

	private Integer calculateWordDistance(Review reviewA, Review reviewB) {
		Integer difference = reviewA.getWordCount() - reviewB.getWordCount();
		if (difference < 0) {
			difference = difference * -1;
		}
		return difference;
	}

	private Boolean calculateSameAuthorship(Review reviewA, Review reviewB) {
		return reviewA.getUser().getId().equals(reviewB.getUser().getId());
	}

	private Integer calculateDifference(Date reviewDate, Date reviewDate2) {
		Integer days = Days.daysBetween(new DateTime(reviewDate), new DateTime(reviewDate2)).getDays();
		if (days < 0) {
			days = days * -1;
		}
		return days;
	}

}
