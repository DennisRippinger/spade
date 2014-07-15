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

import java.util.concurrent.ThreadLocalRandom;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * The Class NilsimsaHashTest.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class NilsimsaHashTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ReviewContentService service;

    @Resource
    private NilsimsaHash hash;

    @Test
    public void calculateNilsima() {
        String hashOne =
            hash.calculateNilsima("I did extensive research before selecting the SD600, and I am thrilled with my purchase. This camera is tiny (smaller than my iPod) and lightweight, but still takes incredible pictures. The screen is much larger than my friends' cameras, and it has all the extra settings that the average person needs to take great photos is all kinds of conditions. I have not had any bad or blurry pictures with it yet. I am thrilled with this camera and would recommend it to everyone.");
        String hashTwo =
            hash.calculateNilsima("I did extensive research before selecting the Kodak EasyShare C875, and I am thrilled with my purchase. This camera takes incredible pictures. The screen is much larger than my friends' cameras, and it has all the extra settings that the average person needs to take great photos in all kinds of conditions. I have not had any bad or blurry pictures with it yet. I am thrilled with this camera and would recommend it to everyone.");

        log.info("Hash One '{}'", hashOne);
        log.info("Hash Two '{}'", hashTwo);

        Integer compared = hash.compare(hashOne, hashTwo);
        log.info("Compared: '{}'", compared);
    }

    @Test
    public void applyHashOnDataSet() {
        for (Integer reviewCounter = 1; reviewCounter <= 34686770; reviewCounter++) {
            String reviewID = String.format("R%010d", reviewCounter);
            Review currentReview = service.findReview(reviewID);
            if (currentReview != null) {
                String nilsimsa = hash.calculateNilsima(currentReview.getContent());
                currentReview.setNilsimsa(nilsimsa);

                service.saveReview(currentReview);
            }

            // Reduce output noise
            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID '{}'", reviewCounter);
            }

        }

    }
}
