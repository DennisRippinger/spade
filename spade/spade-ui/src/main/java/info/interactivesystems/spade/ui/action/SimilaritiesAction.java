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

    @PostConstruct
    private void init() {
        currentSimilarItem = similarityDao.find(SIMILARITY_LIMIT, false, 30);

        categories = new ArrayList<>();
        for (String category : similarityDao.getCategories()) {
            SelectItem categoryItem = new SelectItem();
            categoryItem.setLabel(category);
            categoryItem.setValue(category);
            categories.add(categoryItem);
        }

        next();
    }

    public void switchUserView(User user) {
        userView = true;
        userAction.setUser(user);
    }

    public void switchUserViewBack() {
        userView = false;
    }

    public void updateCategory() {

        if (currentCategory.equals("All")) {
            currentSimilarItem = similarityDao.find(SIMILARITY_LIMIT, 30);
        } else {
            currentSimilarItem = similarityDao.find(SIMILARITY_LIMIT, currentCategory, 30);
        }

        counter = 0;
        next();
    }

    public void next() {
        if (counter < currentSimilarItem.size()) {
            similarPair = currentSimilarItem.get(counter++);
            diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());
        }
    }

    public void previous() {
        if (counter > 1) {
            similarPair = currentSimilarItem.get(--counter);
            diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());
        }

    }

}
