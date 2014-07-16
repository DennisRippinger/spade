package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YelpReverseAggegator {

    @Resource
    private UserDao userDao;

    @Resource
    private ObjectFactory<YelpReverseUserCrawler> crawlerFactory;

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

        private YelpReverseUserCrawler crawler = crawlerFactory.getObject();

        @Override
        public void run() {
            log.info("Thread runs with Crawler '{}'", crawler.toString());
            while (true) {
                // Deprecated
                User randomUser = new User();
                try {
                    crawler.setPageNo(0);
                    crawler.crawlReverse(randomUser);
                } catch (Exception e) {
                    log.error("Unkown Error, marked user", e);
                }

            }
        }

    }

}
