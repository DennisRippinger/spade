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
package info.interactivesystems.spade.aspects;

import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

/**
 * Aspect to sort incoming {@link NilsimsaSimilarity} elements.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Aspect
public class SimilaritySorterAspect {

	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut = "execution(* info.interactivesystems.spade.dao.NilsimsaSimilarityDao.find(..))", returning = "result")
	public Object sortAfterReturn(Object result) {

		List<NilsimsaSimilarity> similarities = (List<NilsimsaSimilarity>) result;

		log.debug("Sorting '{}' similarities", similarities.size());

		return orderSimilarities(similarities);
	}

	/**
	 * Order similarities. This ensures that the First Review (Review A) is the youngest one.
	 *
	 * @param similarities the incoming similarities.
	 * @return the ordered similarities.
	 */
	private List<NilsimsaSimilarity> orderSimilarities(List<NilsimsaSimilarity> similarities) {
		Integer counter = 0;
		for (NilsimsaSimilarity nilsimsaSimilarity : similarities) {
			if (nilsimsaSimilarity.getReviewA().getReviewDate().after(nilsimsaSimilarity.getReviewB().getReviewDate())) {
				Review first = nilsimsaSimilarity.getReviewB();
				Review last = nilsimsaSimilarity.getReviewA();

				unescapeContent(first);
				unescapeContent(last);

				User firstUser = nilsimsaSimilarity.getUserB();
				User lastUser = nilsimsaSimilarity.getUserA();

				nilsimsaSimilarity.setUserA(firstUser);
				nilsimsaSimilarity.setUserB(lastUser);
				nilsimsaSimilarity.setReviewA(first);
				nilsimsaSimilarity.setReviewB(last);

				counter++;
			}

		}

		log.debug("Sorted '{}' items", counter);

		return similarities;
	}

	private void unescapeContent(Review first) {
		String content = StringEscapeUtils.unescapeHtml4(first.getContent());
		first.setContent(content);
	}

}
