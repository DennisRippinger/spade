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
package info.interactivesystems.spade.crawler;

import info.interactivesystems.spade.crawler.util.RenewIP;
import info.interactivesystems.spade.dao.ShadowReviewDao;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * The Class AmazonReviewAggregator.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Service
public class AmazonReviewAggregator {

    @Resource
    private ShadowReviewDao shadowReviewDao;

    @Resource
    private AmazonRatingCrawler crawler;

    /**
     * Load missing reviews.
     */
    public void loadMissingReviews() {
        List<String> reviewUrls = shadowReviewDao.getDistinctUrls();
        for (String url : reviewUrls) {
            try {
                crawler.getAverageRating(url);
            } catch (Exception e) {
                log.error("Renewing IP");
                RenewIP.renewIpOnFritzBox();
            }

        }

    }

}
