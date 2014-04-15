package info.interactivesystems.spade.crawler.amazon;

import info.interactivesystems.spade.crawler.util.AmazonCategory;
import info.interactivesystems.spade.util.ProductCategory;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class AmazonProductCrawlerTest extends AbstractTestNGSpringContextTests {

    @Resource
    private AmazonProductCrawler crawler;

    @Test
    public void crawlingTest() throws InterruptedException {
        crawler.setCategory(AmazonCategory.MOBILEPHONE);
        crawler.setProductCategory(ProductCategory.MOBILEPHONE);
        
        crawler.crawlProducts();
    }
}
