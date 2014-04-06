package info.interactivesystems.spade.crawler;

import info.interactivesystems.spade.crawler.amazon.AmazonReviewAggregator;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class AmazonReviewAggregatorTest extends AbstractTestNGSpringContextTests {

    @Resource
    private AmazonReviewAggregator aggregator;

    @Test
    public void getAverageRating() {
        aggregator.loadMissingReviews();
    }
}
