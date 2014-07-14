package info.interactivesystems.spade.ui.converter;

import javax.inject.Named;

@Named
public class StarRatingConverter {

    /**
     * Returns a CSS Class defining the star rating from the amazon sprite.
     */
    public String getAsString(Double rating) {

        if (rating >= 0 && rating < 0.75) {
            return "a-star-0-5";
        } else if (rating >= 0.75 && rating < 1.25) {
            return "a-star-1";
        } else if (rating >= 1.25 && rating < 1.75) {
            return "a-star-1-5";
        } else if (rating >= 1.75 && rating < 2.25) {
            return "a-star-2";
        } else if (rating >= 2.25 && rating < 2.75) {
            return "a-star-2-5";
        } else if (rating >= 2.75 && rating < 3.25) {
            return "a-star-3";
        } else if (rating >= 3.25 && rating < 3.75) {
            return "a-star-3-5";
        } else if (rating >= 3.75 && rating < 4.25) {
            return "a-star-4";
        } else if (rating >= 4.25 && rating < 4.75) {
            return "a-star-4-5";
        } else {
            return "a-star-5";
        }

    }

}