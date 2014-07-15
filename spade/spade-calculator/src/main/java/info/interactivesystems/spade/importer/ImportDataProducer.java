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
package info.interactivesystems.spade.importer;

import info.interactivesystems.spade.dto.CombinedData;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.sentence.SentenceService;
import info.interactivesystems.spade.similarity.NilsimsaHash;
import info.interactivesystems.spade.util.Authority;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * The Class ImportDataProducer.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class ImportDataProducer implements Callable<Boolean> {

    private static final String UNKNOWN = "unknown";
    private static final String PRODUCT_ID = "product/productId: ";
    private static final String PRODUCT_TITLE = "product/title: ";
    private static final String PRODUCT_PRICE = "product/price: ";
    private static final String REVIEW_USERID = "review/userId: ";
    private static final String REVIEW_PROFILENAME = "review/profileName: ";
    private static final String REVIEW_HELPFULLNESS = "review/helpfulness: ";
    private static final String REVIEW_SCORE = "review/score: ";
    private static final String REVIEW_TIME = "review/time: ";
    private static final String REVIEW_SUMMARY = "review/summary: ";
    private static final String REVIEW_TEXT = "review/text: ";

    @Resource
    private SentenceService sentenceService;

    @Resource
    private NilsimsaHash nilsimsa;

    @Setter
    private Integer reviewCounter;
    private Integer errorCounter = 0;

    @Setter
    private File amazonDataset;

    @Setter
    private Boolean hasMore;

    @Setter
    private static LinkedBlockingQueue<CombinedData> queue;

    @Override
    public Boolean call() throws Exception {

        User user = new User();
        Product product = new Product();
        Review review = new Review();

        initialize(user, product);

        try (BufferedReader br = new BufferedReader(new FileReader(amazonDataset))) {

            for (String line; (line = br.readLine()) != null;) {
                if (!line.isEmpty()) {
                    extractProductID(line, product);
                    extractProductTitle(line, product);
                    extractProductPrice(line, product);

                    extractUserID(line, user, review);
                    extractUserName(line, user);

                    extractReviewHelpfulness(line, review);
                    extractReviewScore(line, review);
                    extractReviewTime(line, review);
                    extractReviewSummary(line, review);
                    extractReviewText(line, review);
                } else {
                    review.setProduct(product);
                    calculateMetric(review);

                    queue.put(new CombinedData(user, review, product));

                    user = new User();
                    product = new Product();
                    review = new Review();

                    initialize(user, product);
                }
            }

        } catch (FileNotFoundException e) {
            log.error("Could not find file", e);
        } catch (IOException e) {
            log.error("Error reading input file", e);
        }

        log.info("Error counter = '{}'", errorCounter);
        hasMore = false;
        return hasMore;
    }

    private void calculateMetric(Review review) {
        Double calculatedARIndex = sentenceService.calculateARIndex(review.getContent());
        Double calculatedGFIndex = sentenceService.calculateGFIndex(review.getContent());
        Double calculatedInformationDensity = sentenceService.calculateInformationDensity(review.getContent());
        Integer wordCount = sentenceService.calculateWordCount(review.getContent());

        review.setAri(calculatedARIndex);
        review.setGfi(calculatedGFIndex);
        review.setDensity(calculatedInformationDensity);
        review.setWordCount(wordCount);

        String nilsimsaValue = nilsimsa.calculateNilsima(review.getContent());
        review.setNilsimsa(nilsimsaValue);

        Double densityRelation = calclateDensity(review);
        review.setDensityRelation(densityRelation);

    }

    private Double calclateDensity(Review review) {
        return review.getDensity() * Math.log(review.getWordCount());
    }

    private void extractReviewText(String line, Review review) {
        if (line.startsWith(REVIEW_TEXT)) {
            try {
                String text = line.replaceFirst(REVIEW_TEXT, "");
                text = text.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                text = text.replace("+", "%2B");
                text = URLDecoder.decode(text, "UTF-8");

                review.setContent(text);
            } catch (Exception e) {
                log.warn("Could not decode input String '{}'", review.getContent(), e);
            }

        }

    }

    private void extractReviewSummary(String line, Review review) {
        if (line.startsWith(REVIEW_SUMMARY)) {
            String summary = line.replaceFirst(REVIEW_SUMMARY, "");
            review.setTitle(summary);
        }
    }

    private void extractReviewTime(String line, Review review) {
        if (line.startsWith(REVIEW_TIME)) {
            String stringTime = line.replaceFirst(REVIEW_TIME, "");

            Long unixBaseTime = Long.parseLong(stringTime);
            Long unixTime = unixBaseTime * 1000;
            Date reviewDate = new Date(unixTime);

            review.setReviewDate(reviewDate);
        }

    }

    private void extractReviewScore(String line, Review review) {
        if (line.startsWith(REVIEW_SCORE)) {
            String stringScore = line.replaceFirst(REVIEW_SCORE, "");
            Double score = Double.parseDouble(stringScore);

            review.setRating(score);
        }

    }

    private void extractReviewHelpfulness(String line, Review review) {
        if (line.startsWith(REVIEW_HELPFULLNESS)) {
            String stringHelpfulness = line.replaceFirst(REVIEW_HELPFULLNESS, "");
            String[] arrayHelpfulness = stringHelpfulness.split("/");

            Integer helpfulness = Integer.parseInt(arrayHelpfulness[0]);
            Integer total = Integer.parseInt(arrayHelpfulness[1]);

            review.setHelpfulVotes(helpfulness);
            review.setTotalVotes(total);
        }

    }

    private void extractUserName(String line, User user) {
        if (line.startsWith(REVIEW_PROFILENAME)) {
            String userName = line.replaceFirst(REVIEW_PROFILENAME, "");

            user.setName(userName);
        }

    }

    private void extractUserID(String line, User user, Review review) {
        if (line.startsWith(REVIEW_USERID)) {
            String userID = line.replaceFirst(REVIEW_USERID, "");

            user.setId(userID);
            review.setUser(user);
        }
    }

    private void extractProductPrice(String line, Product product) {
        if (line.startsWith(PRODUCT_PRICE)) {
            String productPrice = line.replaceFirst(PRODUCT_PRICE, "");
            if (!productPrice.equals(UNKNOWN)) {
                // If 'unknown' price stays 'null'
                Double price = Double.parseDouble(productPrice);
                product.setPrice(price);
            }
        }
    }

    private void extractProductTitle(String line, Product product) {
        if (line.startsWith(PRODUCT_TITLE)) {
            String productTitle = line.replaceFirst(PRODUCT_TITLE, "");
            product.setName(productTitle);
        }

    }

    private void extractProductID(String line, Product product) {
        if (line.startsWith(PRODUCT_ID)) {
            String productID = line.replaceFirst(PRODUCT_ID, "");
            product.setId(productID);
        }

    }

    private void initialize(User user, Product product) {

        user.setAuthority(Authority.AMAZON);
        product.setAuthority(Authority.AMAZON);

        reviewCounter++;

        // Reduce output noise
        Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
        if (rand == 500) {
            log.info("Current Item ID '{}'", reviewCounter);
        }

    }

}
