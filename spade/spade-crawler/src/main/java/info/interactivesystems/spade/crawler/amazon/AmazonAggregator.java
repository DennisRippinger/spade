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
package info.interactivesystems.spade.crawler.amazon;

import info.interactivesystems.spade.dao.ShadowReviewDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
public class AmazonAggregator {

    @Resource
    private ShadowReviewDao shadowReviewDao;

    @Resource
    private AmazonRatingCrawler crawler;

    /**
     * Load missing reviews.
     */
    public void loadMissingReviews() {
        String threadCount = System.getProperty("crawler.threads");
        Integer threads = 3;
        if (!threadCount.isEmpty()) {
            threads = Integer.parseInt(threadCount);
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        for (Integer i = 1; i <= threads; i++) {
            executor.execute(new CrawlingThread());
            waitInSeconds(20);
        }
    }

    private void waitInSeconds(Integer seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class CrawlingThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                List<String> reviewUrls = shadowReviewDao.getRandomDistinct(1000);
                if (reviewUrls.size() < 1000) {
                    log.info("Less than 1000 Reviews available, quit");
                    System.exit(0);
                }
                for (String url : reviewUrls) {
                    try {
                        log.info("Crawling '{}'", url);
                        crawler.getAverageRating(url);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        // log.error("Renewing IP");
                        // RenewIP.renewIpOnFritzBox();
                    }
                }
            }

        }

    }

}
