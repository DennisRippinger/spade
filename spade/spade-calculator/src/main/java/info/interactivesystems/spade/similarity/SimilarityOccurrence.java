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
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Calculates the occurrences of a distinct review within the set of all reviews.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class SimilarityOccurrence {

	@Resource
	private NilsimsaSimilarityDao nilsimsaSimilarityDao;

	public void calculateOccurenceValues() {
		for (Long count = 1L; count < 550481L; count++) {
			NilsimsaSimilarity similarity = nilsimsaSimilarityDao.find(count);
			if (similarity == null) {
				continue;
			}

			List<NilsimsaSimilarity> reviewId_A = nilsimsaSimilarityDao.findSimilaritiesByReviewId(similarity.getReviewA().getId());
			List<NilsimsaSimilarity> reviewId_B = nilsimsaSimilarityDao.findSimilaritiesByReviewId(similarity.getReviewB().getId());

			similarity.setOccurrencesA(reviewId_A.size());
			similarity.setOccurrencesB(reviewId_B.size());

			nilsimsaSimilarityDao.update(similarity);

			// Reduce output noise
			Integer rand = ThreadLocalRandom.current().nextInt(1, 1000);
			if (rand == 500) {
				log.info("Current Item ID '{}'", count);
			}
		}

	}
}
