package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.ui.dto.DetailsRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class UserAction implements Serializable {

    private static final long serialVersionUID = -503276225717002437L;
    
    @Resource
    private NilsimsaSimilarityDao nilsimsaDao;

    @Setter
    @Getter
    private User user;

    @Getter
    @Setter
    private DetailsRow selectedRow;

    @Getter
    private List<DetailsRow> currentRows;

    public void init() {
        List<DetailsRow> rows = new ArrayList<>();
        if (user != null) {
            for (Review review : user.getReviews()) {
                DetailsRow row = new DetailsRow();
                row.setProductId(review.getProduct().getId());
                row.setProductName(review.getProduct().getName());
                row.setCategory(review.getProduct().getCategory());
                row.setRating(review.getProduct().getRating());
                row.setReviewDate(review.getReviewDate());
                row.setReviewText(review.getContent());
                row.setReviewTitle(review.getTitle());
                row.setUserRating(review.getRating());

                NilsimsaSimilarity similar = nilsimsaDao.findSimilarityByReviewId(review.getId());
                if (similar != null) {
                    row.setSimilar(true);
                    row.setSimilarityId(similar.getId());
                } else {
                    row.setSimilar(false);
                }

                rows.add(row);
            }
        }

        currentRows = rows;
    }

}
