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
package info.interactivesystems.spade.ui;

import java.util.List;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.dto.DiffContainer;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.util.DiffCreator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * The Class SimilaritiesAction.
 * 
 * @author Dennis Rippinger
 */
@Controller("similaritiesAction")
@Scope("session")
public class SimilaritiesAction {

    // Resources

    @Resource
    private ReviewContentService service;

    @Resource
    private NilsimsaSimilarityDao similarityDao;

    @Resource
    private DiffCreator diffCreator;

    // Variables

    @Getter
    private List<NilsimsaSimilarity> currentSimilarItem;

    @Getter
    private DiffContainer diffContainer;

    @Getter
    private NilsimsaSimilarity similarPair;

    private Integer counter = 0;

    @PostConstruct
    private void init() {
        currentSimilarItem = similarityDao.find(0.85, false, 20, 30);
        next();
    }

    public void next() {
        similarPair = currentSimilarItem.get(counter++);
        diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());
    }

}
