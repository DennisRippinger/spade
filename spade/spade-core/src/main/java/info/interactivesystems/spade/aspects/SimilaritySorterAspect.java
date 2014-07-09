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

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Aspect
public class SimilaritySorterAspect {

    @Before("execution(* info.interactivesystems.spade.dao.NilsimsaSimilarityDao.find(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before Nilsimsa Find: '{}'", joinPoint.getSignature().getName());
    }

    @SuppressWarnings("unchecked")
    @AfterReturning(pointcut = "execution(* info.interactivesystems.spade.dao.NilsimsaSimilarityDao.find(..))", returning = "result")
    public Object sortAfterReturn(JoinPoint joinPoint, Object result) {

        if (!joinPoint.getSignature().getName().equals("getCategories")) {
            List<NilsimsaSimilarity> similarities = (List<NilsimsaSimilarity>) result;

            return orderSimilarities(similarities);
        }

        return result;

    }

    /**
     * Order similarities.
     * 
     * @param list the list
     * @return the list
     */
    public List<NilsimsaSimilarity> orderSimilarities(List<NilsimsaSimilarity> list) {
        for (NilsimsaSimilarity nilsimsaSimilarity : list) {
            if (nilsimsaSimilarity.getReviewA().getReviewDate().after(nilsimsaSimilarity.getReviewB().getReviewDate())) {
                Review first = nilsimsaSimilarity.getReviewB();
                Review last = nilsimsaSimilarity.getReviewA();

                User firstUser = nilsimsaSimilarity.getUserB();
                User lastUser = nilsimsaSimilarity.getUserA();

                nilsimsaSimilarity.setUserA(firstUser);
                nilsimsaSimilarity.setUserB(lastUser);
                nilsimsaSimilarity.setReviewA(first);
                nilsimsaSimilarity.setReviewB(last);
            }

        }

        return list;
    }

}
