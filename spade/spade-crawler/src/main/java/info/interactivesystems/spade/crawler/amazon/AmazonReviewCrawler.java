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
package info.interactivesystems.spade.crawler.amazon;

import info.interactivesystems.spade.crawler.util.CrawlerUtil;
import info.interactivesystems.spade.dao.ReviewDao;
import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.exception.CrawlerException;
import info.interactivesystems.spade.sentence.AutomatedReadabilityIndex;
import info.interactivesystems.spade.sentence.GunningFogIndex;
import info.interactivesystems.spade.util.Authority;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The Class AmazonReviewCrawler.
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class AmazonReviewCrawler {

    private static final String AMAZON_URL = "http://www.amazon.com/product-reviews/%s?pageNumber=%s";
    private static final SimpleDateFormat AMAZON_DATE = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

    private final Pattern starPattern = Pattern.compile("\\d[.\\d]*");
    private final Pattern reviewPattern = Pattern.compile("review/.*/");
    private final Pattern userPattern = Pattern.compile("profile/.*/");

    private WebClient webClient = CrawlerUtil.getChrome(false, false);

    @Resource
    private ReviewDao reviewDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AutomatedReadabilityIndex ari;

    @Resource
    private GunningFogIndex gfi;

    @Setter
    private Integer page = 1;

    @Setter
    private Integer noReviews = 10;

    public void crawlReviews(Product amazonProduct) {
        log.info("Crawling Product '{}'", amazonProduct.getName());

        // If started with a later page
        noReviews = noReviews * page;

        String amazonURL = getURL(amazonProduct.getId());
        HtmlPage amazonPage;

        try {
            amazonPage = CrawlerUtil.getWebPage(webClient, amazonURL, 0);
            Integer noOfItems = getMaximumItems(amazonPage, amazonProduct);

            crawlReviews(amazonPage, amazonProduct);
            page++;

            for (; noReviews < noOfItems; page++) {
                try {
                    amazonURL = getURL(amazonProduct.getId());
                    amazonPage = CrawlerUtil.getWebPage(webClient, amazonURL, 1);
                    crawlReviews(amazonPage, amazonProduct);
                } catch (CrawlerException e) {
                    log.warn("Could not load a following page", e);
                }
                noReviews += 10;
            }
        } catch (CrawlerException e1) {
            log.error("Could not load first page", e1);
        }

    }

    private void crawlReviews(HtmlPage amazonPage, Product amazonProduct) {
        @SuppressWarnings("unchecked")
        List<DomElement> reviews = (List<DomElement>) amazonPage
            .getByXPath("//div[@style='margin-left:0.5em;']");

        for (DomElement domReview : reviews) {
            try {
                if (!isGenuineReview(domReview, amazonProduct)) {
                    log.info("Review is not genuine to Product");
                    continue;
                }
                Review review = new Review();

                extractReviewID(review, domReview);

                extractReview(review, domReview);
                extractReviewDate(review, domReview);
                extractRating(review, domReview);
                extractTitle(review, domReview);
                extractUser(review, domReview);
                extractVotings(review, domReview);
                extractComments(review, domReview);
                extractVerified(review, domReview);

                calculateReadability(review, domReview);

                review.setProduct(amazonProduct);

                if (!reviewDao.checkIfAlreadyExists(review.getId())) {
                    reviewDao.save(review);
                }

            } catch (ParseException e) {
                log.warn("Could not parse the date", e);
            } catch (NullPointerException e) {
                log.warn("NullPointer thrown, possible captcha", e);
            } catch (CrawlerException e) {
                log.warn(e.getMessage());
            }
        }
    }

    private Boolean isGenuineReview(DomElement domReview, Product amazonProduct) {
        DomElement domGeniuine = domReview.getFirstByXPath(".//div[@class='tiny']/b/span[@class='h3color tiny']");
        if (domGeniuine != null) {
            DomElement domProductName = (DomElement) domGeniuine.getParentNode();
            String productName = domProductName.asText();
            if (productName.contains(amazonProduct.getName())) {
                return true;
            }
            return false;
        }
        return true;
    }

    private void calculateReadability(Review review, DomElement domReview) {
        Double ariValue = ari.calculateReadability(review.getContent());
        Double gfiValue = gfi.calculateReadability(review.getContent());

        review.setAri(ariValue);
        review.setGfi(gfiValue);
    }

    private void extractTitle(Review review, DomElement domReview) {
        DomElement domTitle = domReview.getFirstByXPath(".//span[@style='vertical-align:middle;']/b");
        if (domTitle != null) {
            String title = domTitle.asText();
            review.setTitle(title);
            return;
        }
        log.warn("Title not given");

    }

    private void extractVerified(Review review, DomElement domReview) {

        DomElement domVerified = domReview.getFirstByXPath(".//span[@class='crVerifiedStripe']/b");
        if (domVerified != null) {
            review.setVerified(true);
        }

    }

    private void extractComments(Review review, DomElement domReview) {
        DomElement domComments = domReview
            .getFirstByXPath(".//div[@style='white-space:nowrap;padding-left:-5px;padding-top:5px;']/a[2]");
        if (domComments != null) {
            String stringComment = domComments.asText();
            Matcher matcher = starPattern.matcher(stringComment);
            if (matcher.find()) {
                String stringNoComments = matcher.group();
                Integer noComments = Integer.parseInt(stringNoComments);

                review.setComments(noComments);
            }
        }

    }

    private void extractVotings(Review review, DomElement domReview) {
        DomNode domEvtlUsefull = domReview.getFirstChild();
        if (domEvtlUsefull.asText().contains("found the following review helpful")) {
            String stringUseFull = domEvtlUsefull.asText();
            Matcher matcher = starPattern.matcher(stringUseFull);
            if (matcher.find()) {

                String actual = matcher.group();
                matcher.find();
                String all = matcher.group();

                Integer intActual = Integer.parseInt(actual);
                Integer intAll = Integer.parseInt(all);

                review.setHelpfulVotes(intActual);
                review.setTotalVotes(intAll);
            }
        }

    }

    private void extractRating(Review review, DomElement domReview) {
        DomElement domRating = domReview.getFirstByXPath(".//span[@style='margin-right:5px;']/span");
        String ratingString = domRating.getAttribute("title");
        Matcher matcher = starPattern.matcher(ratingString);
        if (matcher.find()) {
            String rating = matcher.group();
            Double result = Double.parseDouble(rating);
            review.setRating(result);
        }
    }

    private void extractReview(Review review, DomElement domReview) {
        DomElement domReviewText = domReview.getFirstByXPath(".//div[@class='reviewText']");
        if (domReviewText != null) {
            String reviewText = domReviewText.asText();

            review.setContent(reviewText);
            return;
        }
        log.warn("Empty Review for ID '{}'", review.getId());

    }

    private void extractReviewDate(Review review, DomElement domReview) throws ParseException {
        DomElement domPublished = domReview.getFirstByXPath(".//span[@style='vertical-align:middle;']/nobr");
        if (domPublished != null) {
            String dateString = domPublished.asText();
            Date published = AMAZON_DATE.parse(dateString);
            review.setReviewDate(published);
            return;
        }
        log.warn("Published on Date not given for '{}'", review.getId());

    }

    private void extractReviewID(Review review, DomElement domReview) throws CrawlerException {
        DomElement domID = domReview.getFirstByXPath(".//div[@style='white-space:nowrap;padding-left:-5px;padding-top:5px;']/a[1]");

        // http://www.amazon.com/review/R19C6FP5HZ8TS1/ref=cm_cr_pr_cmt?ie=UTF8&ASIN=B00A9S3OOC#wasThisHelpful
        String reviewURL = domID.getAttribute("href");
        Matcher matcher = reviewPattern.matcher(reviewURL);
        if (matcher.find()) {
            String reviewID = matcher.group().replace("review/", "");
            reviewID = reviewID.substring(0, reviewID.indexOf('/'));
            review.setId(reviewID);
            return;
        }
        throw new CrawlerException("Could not extract Review ID");

    }

    private void extractUser(Review review, DomElement domReview) throws CrawlerException {
        User user = new User();

        DomElement domUser = domReview.getFirstByXPath(".//span[@style='font-weight: bold;']");
        if (domUser == null) {
            throw new CrawlerException("Anonymous user");
        }

        String userName = domUser.getTextContent();
        DomElement domProfileURL = (DomElement) domUser.getParentNode();
        String userURL = domProfileURL.getAttribute("href");
        Matcher matcher = userPattern.matcher(userURL);
        if (matcher.find()) {
            String userId = matcher.group().replace("profile/", "");
            userId = userId.substring(0, userId.indexOf('/'));

            user.setId(userId);
            user.setName(userName);
            user.setAuthority(Authority.AMAZON);

            review.setAuthorId(userId);

            // Random ID eats Update.
            if (!userDao.checkIfAlreadyExists(userId)) {
                userDao.save(user);
            }
            return;
        }
        throw new CrawlerException("Could not extract User");
    }

    private Integer getMaximumItems(HtmlPage amazonPage, Product amazonProduct) {
        DomElement domNoReviews = amazonPage
            .getFirstByXPath("//div[@style='display:block; text-align:center; padding-bottom: 5px;']/b");
        Integer result = 0;
        if (domNoReviews != null) {

            String noReviews = domNoReviews.getTextContent();
            Matcher matcher = starPattern.matcher(noReviews);
            if (matcher.find()) {
                noReviews = matcher.group();
                result = Integer.parseInt(noReviews);

            }

            log.info("Product '{}' has '{}' reviews.", amazonProduct.getName(), result);
            return result;
        }
        log.error("Could not find pagination info");
        return 0;
    }

    private String getURL(String product) {
        String url = String.format(AMAZON_URL, product, page);

        return url;
    }

}
