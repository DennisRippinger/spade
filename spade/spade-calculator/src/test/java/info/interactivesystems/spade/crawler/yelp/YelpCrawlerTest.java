package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.crawler.yelp.YelpVenueCrawler;
import info.interactivesystems.spade.util.ProductCategory;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class YelpCrawlerTest extends AbstractTestNGSpringContextTests {

    @Resource
    private YelpVenueCrawler crawler;

    @Test
    public void YelpCrawler() throws UnsupportedEncodingException, InterruptedException {
        crawler.setLocation("Jacksonville, FL, USA");
        crawler.setCategory(ProductCategory.RESTAURANT);
        
        crawler.setPageNo(63);
        crawler.crawlVenues();
    }
}