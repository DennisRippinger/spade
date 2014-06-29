package info.interactivesystems.spade;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import lombok.extern.slf4j.Slf4j;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class Denomalization extends AbstractTestNGSpringContextTests {

    @Resource
    private ReviewContentService service;

    @Test
    public void denomalization() {
        for (Integer count = 1; count <= 2441053; count++) {
            Product currentProduct = service.findByID(count);

            List<Review> reviews = service.findReviewByProductID(currentProduct.getId());

            for (Review review : reviews) {
                    review.setCategory(currentProduct.getCategory());
                    service.saveReview(review);
            }

            Integer rand = ThreadLocalRandom.current().nextInt(1, 20000);
            if (rand == 500) {
                log.info("Current Product Item '{}'", count);
            }

        }

    }
}
