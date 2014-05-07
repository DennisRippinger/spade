package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.util.ConcurrentBit;

import java.util.Date;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class YelpReverseCrawlerTest extends AbstractTestNGSpringContextTests {

//    @Resource
//    private UserDao userDao;
//
//    @Resource
//    private YelpReverseUserCrawler crawler;
//
//    @Test
//    public void testReverseCrawling() {
//        User user = userDao.find("--jSxZSrjvyIvR88zldm1A");
//
//        try {
//            lockUser(user);
//            crawler.setPageNo(0);
//            crawler.crawlReverse(user);
//            unlockItem(user);
//        } catch (Exception e) {
//            markUser(user, e);
//            log.error("Unkown Error, marked user", e);
//        }
//
//    }
//
//    private void markUser(User user, Exception e) {
//        log.info("Marking User '{}'", user.getName());
//        user.setConcurrentBit(ConcurrentBit.MARKED);
//        user.setError(e.getMessage());
//        user.setTimestamp(new Date());
//        userDao.save(user);
//    }
//
//    private void lockUser(User user) {
//        log.info("Locking User '{}'", user.getName());
//        user.setConcurrentBit(ConcurrentBit.IN_WORK);
//        user.setTimestamp(new Date());
//        userDao.save(user);
//    }
//
//    private void unlockItem(User user) {
//        log.info("Unlocking User '{}'", user.getName());
//        user.setConcurrentBit(ConcurrentBit.PROSSED);
//        user.setTimestamp(new Date());
//        userDao.save(user);
//    }

}
