package info.interactivesystems.spade.importer;

import java.util.concurrent.LinkedBlockingQueue;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.dto.CombinedData;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImportDataConsumer implements Runnable {

    private static final Boolean IMPORT_UNKOWN = false;
    private static final String UNKNOWN = "unknown";

    private Long productCounter = 1l;
    private Long userCounter = 1l;

    @Setter
    private Integer reviewCounter;

    @Setter
    private static LinkedBlockingQueue<CombinedData> queue;
    
    @Setter
    private Boolean hasMore;

    @Resource
    private ReviewContentService contentService;

    @Override
    public void run() {
        while(true){
            try {
                CombinedData combined = queue.take();

                persist(combined.getUser(), combined.getProduct(), combined.getReview());

            } catch (InterruptedException e) {

            } catch (Exception e) {
                log.error("Error Persisting Data '{}'", e);
            }
        }
    }

    private void persist(User user, Product product, Review review) {

        if (!contentService.checkIfProductExists(product.getId())) {
            product.setRandomID(productCounter++);
            contentService.saveProduct(product);
        }

        if (!contentService.checkIfUserExists(user.getId())) {
            if (!user.getId().equals(UNKNOWN)) {
                user.setRandomID(userCounter++);
                contentService.saveUser(user);
            } else if (IMPORT_UNKOWN) {
                contentService.saveUser(user);
            } else {
                log.trace("Skipping unknown user");
            }
        }

        if (!user.getId().equals(UNKNOWN)) {
            String id = String.format("R%010d", reviewCounter++);
            review.setId(id);
            contentService.saveReview(review);
        } else if (IMPORT_UNKOWN) {
            String id = String.format("R%010d", reviewCounter++);
            review.setId(id);
            contentService.saveReview(review);
        }
    }

}
