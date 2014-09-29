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
package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Class YelpCrawlingAggregator.
 *
 * @author Dennis Rippinger
 * @deprecated Entities not current state.
 */
@Slf4j
@Service
public class YelpCrawlingAggregator {

	@Resource
	private ProductDao productDao;

	@Resource
	private ObjectFactory<YelpReviewCrawler> crawlerFactory;

	/**
	 * Start crawling threads.
	 */
	public void startCrawlingThreads() {
		String threadCount = System.getProperty("crawler.threads");
		Integer threads = 2;
		if (threadCount != null && !threadCount.isEmpty()) {
			threads = Integer.parseInt(threadCount);
		}

		ExecutorService executor = Executors.newCachedThreadPool();
		for (Integer i = 1; i <= threads; i++) {
			log.info("Starting Thread No {}", i);
			executor.execute(new CrawlerThread());
		}

	}

	/**
	 * The Class CrawlerThread.
	 */
	private class CrawlerThread implements Runnable {

		private YelpReviewCrawler crawler = crawlerFactory.getObject();

		@Override
		public void run() {
			log.info("Thread runs with Crawler '{}'", crawler.toString());
			while (true) {
				// DEPRECATED

				Product randomProduct = new Product();

				crawler.setPage(0);
				crawler.crawlReviews(randomProduct);
			}
		}

	}

}
