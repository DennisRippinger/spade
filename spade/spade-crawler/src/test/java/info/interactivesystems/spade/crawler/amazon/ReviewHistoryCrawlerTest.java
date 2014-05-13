package info.interactivesystems.spade.crawler.amazon;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ReviewHistoryCrawlerTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ReviewHistoryCrawler crawler;

    @Test
    public void f() {
        crawler.crawlTimeFrequency();
    }
}
