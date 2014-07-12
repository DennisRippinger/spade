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

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;

@Slf4j
@Named
@Scope("session")
public class RecommenderAction {

    @Resource
    private ReviewContentService service;

    @Getter
    private List<User> users;

    @Getter
    @Setter
    private List<Review> currentReviews;

    @Getter
    @Setter
    private Review selectedReview;

    @Getter
    private User currentUser;

    @PostConstruct
    public void init() {
        users = service.findUsersWithHIndex(3.0, 30);

        currentReviews = users.get(0).getReviews();

        log.info("Size of Current Reviews = '{}'", currentReviews.size());
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        currentReviews = currentUser.getReviews();
    }

}
