package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.util.ConcurrentBit;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YelpCrawlingAggregator {

    @Resource
    private ProductDao productDao;

    @Resource
    private ObjectFactory<YelpReviewCrawler> crawlerFactory;

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

    private class CrawlerThread implements Runnable {

        private YelpReviewCrawler crawler = crawlerFactory.getObject();

        @Override
        public void run() {
            log.info("Thread runs with Crawler '{}'", crawler.toString());
            while (true) {
                Product randomProduct = productDao.getRandomProduct(ProductCategory.RESTAURANT);

                if (randomProduct == null)
                    break;

                lockItem(randomProduct);
                crawler.setPage(0);
                crawler.crawlReviews(randomProduct);
                unlockItem(randomProduct);
            }
        }

        private void lockItem(Product venue) {
            log.info("Locking Venue '{}'", venue.getName());
            venue.setConcurrentBit(ConcurrentBit.IN_WORK);
            productDao.save(venue);
        }

        private void unlockItem(Product venue) {
            log.info("Unlocking Venue '{}'", venue.getName());
            venue.setConcurrentBit(ConcurrentBit.PROSSED);
            productDao.save(venue);
        }

    }

}
