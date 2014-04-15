package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.util.ConcurrentBit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
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
                User randomUser = userDao.getRandomUser();

                if (randomUser == null)
                    break;

                try {
                    lockUser(randomUser);
                    crawler.setPageNo(0);
                    crawler.crawlReverse(randomUser);
                    unlockItem(randomUser);
                } catch (Exception e) {
                    markUser(randomUser, e);
                    log.error("Unkown Error, marked user", e);
                }

            }
        }

        private void markUser(User user, Exception e) {
            log.info("Marking User '{}'", user.getName());
            user.setConcurrentBit(ConcurrentBit.MARKED);
            user.setError(e.getMessage());
            user.setTimestamp(new Date());
            userDao.save(user);
        }

        private void lockUser(User user) {
            log.info("Locking User '{}'", user.getName());
            user.setConcurrentBit(ConcurrentBit.IN_WORK);
            user.setTimestamp(new Date());
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                user.setCurrentWorker(hostName);
            } catch (UnknownHostException e1) {
                log.warn("Could not get Hostname");
            }
            userDao.save(user);
        }

        private void unlockItem(User user) {
            log.info("Unlocking User '{}'", user.getName());
            user.setConcurrentBit(ConcurrentBit.PROSSED);
            user.setTimestamp(new Date());
            userDao.save(user);
        }

    }

}
