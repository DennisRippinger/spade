package info.interactivesystems.spade.importer;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AmazonCount {

    private static final Integer OBERSERVATIONS = 5;
    private static final Integer MAXSIZE = 2441053;
    private static final Double AMAZON_GLOBAL_AVG = 4.190279903844344;

    @Resource
    private ReviewContentService service;

    public void countNumberOfReviews() {
        for (Integer count = 1; count <= MAXSIZE; count++) {
            Product currentProduct = service.findByID(count);

            List<Review> reviews = service.findReviewByProductID(currentProduct.getId());
            currentProduct.setNoOfReviews(reviews.size());
            currentProduct.setRating(getAverage(reviews));

            service.saveProduct(currentProduct);

            Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
            if (rand == 500) {
                log.info("Current Item ID '{}'", count);
            }
        }
    }

    /**
     * Bayesian average calculation.
     * 
     * @param reviews
     * @return
     */
    public Double getAverage(List<Review> reviews) {
        Double numberOfStars = 0.0;

        for (Review review : reviews) {
            numberOfStars += review.getRating();
        }

        Double nominator = (OBERSERVATIONS * AMAZON_GLOBAL_AVG) + numberOfStars;
        Integer denominator = OBERSERVATIONS + reviews.size();
        Double baysianAverage = nominator / denominator;

        return baysianAverage;
    }

}