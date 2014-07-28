/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.dto.DiffContainer;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.util.DiffCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;

/**
 * The Class SimilaritiesAction.
 * 
 * @author Dennis Rippinger
 */
@Named
@Scope("session")
public class SimilaritiesAction {

    private static final Double SIMILARITY_LIMIT = 0.90;

    // Resources

    @Resource
    private ReviewContentService service;

    @Resource
    private NilsimsaSimilarityDao similarityDao;

    @Resource
    private DiffCreator diffCreator;

    @Resource
    private UserAction userAction;

    // Variables

    @Getter
    private List<NilsimsaSimilarity> currentSimilarItem;

    @Getter
    private DiffContainer diffContainer;

    @Getter
    private NilsimsaSimilarity similarPair;

    @Getter
    @Setter
    private String currentCategory;

    @Setter
    @Getter
    private boolean userView = false;

    @Getter
    private List<SelectItem> categories;

    private Integer counter = 0;

    private Integer window = 1;

    /**
     * Initializes with no certain category.
     */
    @PostConstruct
    public void init() {

        currentSimilarItem = similarityDao.findInWindow(SIMILARITY_LIMIT, false, window);

        categories = new ArrayList<>();
        for (String category : similarityDao.getCategories()) {
            SelectItem categoryItem = new SelectItem();
            categoryItem.setLabel(category);
            categoryItem.setValue(category);
            categories.add(categoryItem);
        }

        next();
    }

    /**
     * Switch to the selected author of a review and the user view.
     * 
     * @param user
     */
    public void switchUserView(User user) {
        userView = true;
        userAction.setUser(user);
        userAction.init();
    }

    /**
     * Switch back to review perspective.
     */
    public void switchUserViewBack() {
        userView = false;
    }

    /**
     * Updates the current category.
     */
    public void updateCategory() {

        if ("All".equals(currentCategory)) {
            currentSimilarItem = similarityDao.find(SIMILARITY_LIMIT, false, 30);
        } else {
            currentSimilarItem = similarityDao.find(SIMILARITY_LIMIT, currentCategory, 30);
        }

        counter = 0;
        next();
    }

    /**
     * Selects the next review in the category set.
     */
    public void next() {
        if (counter < currentSimilarItem.size()) {
            similarPair = currentSimilarItem.get(counter++);
            diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());
        } else {
            counter = 0;
            window++;

            currentSimilarItem = similarityDao.findInWindow(SIMILARITY_LIMIT, false, window);
            similarPair = currentSimilarItem.get(counter);
            diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());
        }
        switchUserViewBack();
    }

    /**
     * Selects the previous review in the category set.
     */
    public void previous() {
        if (counter > 1) {
            counter--;
            similarPair = currentSimilarItem.get(counter);
            diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());
        }
        switchUserViewBack();

    }

    /**
     * CSS for same Product
     * 
     * @return CSS Key
     */
    public String sameProduct() {
        String reviewIdA = similarPair.getReviewA().getProduct().getId();
        String reviewIdB = similarPair.getReviewB().getProduct().getId();

        return getCssForContentFlag(reviewIdA, reviewIdB);
    }

    /**
     * CSS for same author
     * 
     * @return CSS Key
     */
    public String sameAuthor() {
        String userIdA = similarPair.getUserA().getId();
        String userIdB = similarPair.getUserB().getId();

        return getCssForContentFlag(userIdA, userIdB);
    }

    /**
     * CSS for same rating
     * 
     * @return CSS Key
     */
    public String sameRating() {
        Double ratingA = similarPair.getReviewA().getRating();
        Double ratingB = similarPair.getReviewB().getRating();

        return getCssForContentFlag(ratingA, ratingB);
    }

    /**
     * CSS for same date
     * 
     * @return CSS Key
     */
    public String sameDate() {
        Date dateA = similarPair.getReviewA().getReviewDate();
        Date dateB = similarPair.getReviewB().getReviewDate();

        return getCssForContentFlag(dateA, dateB);
    }

    /**
     * CSS for same title
     * 
     * @return CSS Key
     */
    public String sameTitle() {
        String titleA = similarPair.getReviewA().getTitle();
        String titleB = similarPair.getReviewB().getTitle();

        return getCssForContentFlag(titleA, titleB);
    }

    private String getCssForContentFlag(Object idA, Object idB) {
        if (idA.equals(idB)) {
            return "sameContent";
        }
        return "differentContent";
    }
}
