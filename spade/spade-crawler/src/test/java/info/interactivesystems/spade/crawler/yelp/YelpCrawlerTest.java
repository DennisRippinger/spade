package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.util.ProductCategory;

import java.io.UnsupportedEncodingException;

import org.testng.annotations.Test;

public class YelpCrawlerTest {

    @Test
    public void YelpCrawler() throws UnsupportedEncodingException, InterruptedException {
        YelpCrawler crawler = new YelpCrawler("Detroit, MI, USA", 0, ProductCategory.RESTAURANT);

        crawler.crawlReviews();
    }
}