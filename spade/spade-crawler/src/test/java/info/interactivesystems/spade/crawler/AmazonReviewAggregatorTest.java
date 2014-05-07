package info.interactivesystems.spade.crawler;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class AmazonReviewAggregatorTest extends AbstractTestNGSpringContextTests {

//    @Resource
//    private AmazonAggregator aggregator;
//
//    @Test
//    public void getAverageRating() {
//        aggregator.loadMissingReviews();
//    }
}
