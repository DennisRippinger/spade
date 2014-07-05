/**
 * 
 */
package info.interactivesystems.spade.importer;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Component
public class UniqueReviewCrystalizer {

    @Resource
    private ReviewContentService service;

    private Integer uniqueCounter = 0;

    public void tagUniqueReviews() {
        for (Long userID = 1l; userID <= 6643623; userID++) {
            User currentUser = service.findUserByID(userID);

            List<Review> uniqueReviews = new LinkedList<Review>();

            Set<Review> reviews = currentUser.getReviews();
            for (Review review : reviews) {
                if (isNew(review, uniqueReviews)) {
                    review.setUnique(true);
                    uniqueReviews.add(review);
                }
            }

            Integer helpFull = 0, votes = 0;

            for (Review review : uniqueReviews) {
                service.saveReview(review);
                helpFull += review.getHelpfulVotes();
                votes += review.getTotalVotes();
            }

            currentUser.setNumberOfReviews(uniqueReviews.size());
            currentUser.setHelpfulVotes(helpFull);
            currentUser.setHelpfulOverallVotes(votes);
            if (votes.equals(0)) {
                currentUser.setHelpfulness(0);
            } else {
                currentUser.setHelpfulness(helpFull / votes);
            }

            service.saveUser(currentUser);

            uniqueCounter += uniqueReviews.size();

            Integer rand = ThreadLocalRandom.current().nextInt(1, 500);
            if (rand == 50) {
                log.info("Current User No '{}', Unique Items so far '{}'", userID, uniqueCounter);
            }
        }
        log.info("Unique Reviews: '{}'", uniqueCounter);
    }

    private boolean isNew(Review review, List<Review> uniqueReviews) {
        for (Review knownReview : uniqueReviews) {
            if (isEqual(knownReview, review)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEqual(Review knownReview, Review review) {
        return knownReview.getTitle().equals(review.getTitle()) &&
            knownReview.getContent().equals(review.getContent()) &&
            knownReview.getReviewDate().equals(review.getReviewDate());
    }
}
