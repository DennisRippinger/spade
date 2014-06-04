package info.interactivesystems.spade.crawler.yelp;

import java.util.List;

import info.interactivesystems.spade.crawler.yelp.YelpReviewCrawler;
import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.entities.Product;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class YelpReviewCrawlerTest extends AbstractTestNGSpringContextTests {

    @Resource
    private YelpReviewCrawler crawler;

    @Resource
    private ProductDao productDao;
    Boolean found = false;

//    @Test
//    public void YelpCrawler() {
//        List<Product> yelpVenues = productDao.getYelpVenues();
//        for (Product yelpVenue : yelpVenues) {
//            crawler.crawlReviews(yelpVenue);
//            crawler.setPage(0);
//        }

//    }
}
