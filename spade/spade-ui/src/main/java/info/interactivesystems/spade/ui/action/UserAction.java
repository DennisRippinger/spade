package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;

@Named
@Scope("session")
public class UserAction {

    @Setter
    @Getter
    private User user;

    @Getter
    @Setter
    private Review selectedReview;

    public List<Review> getReviews() {
        if (user != null) {
            return user.getReviews();
        }
        List<Review> reviews = new ArrayList<>();
        Review tempReview = new Review();
        reviews.add(tempReview);

        return reviews;
    }

    public String getReviewForDialog() {
        if (selectedReview != null) {
            return selectedReview.getContent();
        }
        return "";
    }
}
