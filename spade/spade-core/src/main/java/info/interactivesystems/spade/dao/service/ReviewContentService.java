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
package info.interactivesystems.spade.dao.service;

import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.dao.ReviewDao;
import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

/**
 * Service for Review, Product and User DAO layer.
 * 
 * @author Dennis Rippinger
 */
@Repository
@Transactional
public class ReviewContentService {

    @Resource
    private ProductDao productDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ReviewDao reviewDao;

    // Products

    /**
     * Saves a product.
     * 
     * @param product the product
     */
    public void saveProduct(Product product) {
        productDao.save(product);
    }

    /**
     * Finds a product.
     * 
     * @param id the id
     * @return the product
     */
    public Product findProduct(String id) {
        return productDao.find(id);
    }

    /**
     * Deletes a product.
     * 
     * @param product the product
     */
    public void deleteProduct(Product product) {
        productDao.delete(product);
    }

    /**
     * Selects a Product by a incremented number.
     * 
     * @param id the unique number.
     * @return a product.
     */
    public Product findByID(Long id) {
        return productDao.findByID(id);
    }

    public List<Review> findProductsByCategory(String category) {
        return productDao.findReviewsByCategory(category);
    }

    /**
     * Check if a product exists.
     * 
     * @param id the id of the product
     * @return the boolean
     */
    public Boolean checkIfProductExists(String id) {
        return productDao.checkIfAlreadyExists(id);
    }

    // Reviews

    /**
     * Saves the review.
     * 
     * @param review the review
     */
    public void saveReview(Review review) {
        reviewDao.update(review);
    }

    /**
     * Finds the review.
     * 
     * @param id the id
     * @return the review
     */
    public Review findReview(String id) {
        return reviewDao.find(id);
    }

    public List<Review> findReviewByProductID(String productID) {
        return reviewDao.findReviewByProductID(productID);
    }

    public List<Review> findReviewFromUser(String userID) {
        return reviewDao.findReviewFromUser(userID);
    }

    public List<Review> findReviewsByCategory(String category) {
        return reviewDao.findReviewsByCategory(category);
    }

    /**
     * Deletes a review.
     * 
     * @param review the review
     */
    public void deleteReview(Review review) {
        reviewDao.delete(review);
    }

    /**
     * Checks if review exists.
     * 
     * @param id the id
     * @return the boolean
     */
    public Boolean checkIfReviewExists(String id) {
        return reviewDao.checkIfAlreadyExists(id);
    }

    // User

    /**
     * Saves the user.
     * 
     * @param user the user
     */
    public void saveUser(User user) {
        userDao.update(user);
    }

    /**
     * Finds a user.
     * 
     * @param id the id
     * @return the user
     */
    public User findUser(String id) {
        return userDao.find(id);
    }

    /**
     * Deletes the user.
     * 
     * @param user the user
     */
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    /**
     * Checks if user exists.
     * 
     * @param id the id
     */
    public Boolean checkIfUserExists(String id) {
        return userDao.checkIfAlreadyExists(id);
    }

    public User findUserByID(Long id) {
        return userDao.findByRandomID(id);
    }

    /**
     * Lists all Users with an Hindex >= the maxIndex parameter.
     * 
     * @param minIndex
     * @return
     */
    public List<User> findUsersWithHIndex(Double minIndex) {
        return userDao.findUsersWithHIndex(minIndex);
    }

    /**
     * Lists all Users with an Hindex >= the maxIndex parameter.
     * 
     * @param minIndex the minimum index value.
     * @param limit maximum amount of users returned.
     * @return the User list
     */
    public List<User> findUsersWithHIndex(Double minIndex, Integer limit) {
        return userDao.findUsersWithHIndex(minIndex, limit);
    }

}
