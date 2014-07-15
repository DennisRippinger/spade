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
package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.crawler.util.CrawlerUtil;
import info.interactivesystems.spade.crawler.util.PropertyUtil;
import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.dao.ReviewDao;
import info.interactivesystems.spade.dto.Tuple;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.exception.CrawlerException;
import info.interactivesystems.spade.util.Authority;
import info.interactivesystems.spade.util.PriceCategory;
import info.interactivesystems.spade.util.ProductCategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The Class YelpReverseUserCrawler.
 * 
 * @author Dennis Rippinger
 * @deprecated Entities not current state.
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class YelpReverseUserCrawler {

    private static final String YELP_URL = "http://www.yelp.com/user_details_reviews_self?userid=%s&rec_pagestart=%s";
    private static final String YELP_BASE = "http://www.yelp.com%s";
    private static final SimpleDateFormat YELP_DATE = new SimpleDateFormat("MM/dd/yyyy");

    private WebClient webClient = CrawlerUtil.getRandomDesktopWebClient(false, false);
    private final Pattern starPattern = Pattern.compile("\\d[.\\d]*");
    private final Pattern datePatter = Pattern.compile("\\d*[/]\\d*[/]\\d*");

    @Resource
    private ProductDao productDao;

    @Resource
    private ReviewDao reviewDao;

    @Setter
    private Integer pageNo = 0;

    public void crawlReverse(User user) {
        String yelpUserURL = getURL(user);
        HtmlPage yelpPage;
        try {
            yelpPage = CrawlerUtil.getWebPage(webClient, yelpUserURL, 2);

            crawlReviews(yelpPage, user);

            Double counterPage = pageNo * 1.0;
            Double counterMax = getMaximumPageNumber(user.getNumberOfReviews());

            for (; counterPage < counterMax; counterPage++) {
                pageNo++;

                try {
                    yelpUserURL = getURL(user);
                    Integer timeout = PropertyUtil.getIntegerProperty("crawler.timeout", 3);
                    yelpPage = CrawlerUtil.getWebPage(webClient, yelpUserURL, timeout);
                    crawlReviews(yelpPage, user);
                } catch (CrawlerException e) {
                    log.warn("Could not load a following page", e);
                }

            }

        } catch (CrawlerException e) {
            log.error("Could not load first page", e);
        }

    }

    private Double getMaximumPageNumber(Integer numberOfReviews) {
        Double counterMax = numberOfReviews * 1.0 / 10;
        return Math.floor(counterMax);
    }

    private void crawlReviews(HtmlPage yelpPage, User user) {
        @SuppressWarnings("unchecked")
        List<DomElement> divReviews = (List<DomElement>) yelpPage.getByXPath("//div[@class='review clearfix']");

        for (DomElement domReview : divReviews) {
            Product yelpVenue = null;
            Review yelpReview = null;

            try {
                Tuple<Product, Review> tmp = extractRestaurantAndID(domReview, yelpVenue, yelpReview);
                yelpVenue = tmp.x;
                yelpReview = tmp.y;

                extractReviewText(domReview, yelpReview);
                extractReviewRating(domReview, yelpReview);
                extractReviewDate(domReview, yelpReview);
                extractCheckins(domReview, yelpReview);

                /**
                 * Unfortunately there is no distinct attribute. Yelp itself seems to manage some kind of tag cloud to attribute
                 * a venue to a category.
                 */
                yelpVenue.setType(ProductCategory.UNKNOWN);
                yelpVenue.setAuthority(Authority.YELP);

                // Save
                if (!productDao.checkIfAlreadyExists(yelpVenue.getId())) {
                    productDao.save(yelpVenue);
                }
                if (!reviewDao.checkIfAlreadyExists(yelpReview.getId())) {
                    reviewDao.save(yelpReview);
                }
            } catch (CrawlerException e) {
                log.error("Error crawling review", e);
            }

        }

    }

    private void extractCheckins(DomElement domReview, Review yelpReview) {
        DomElement domChecking = domReview
            .getFirstByXPath(".//span[@class='i-wrap ig-wrap-common i-checkin-burst-blue-small-common-wrap badge checkin checkin-irregular']");
        if (domChecking != null) {
            String checkingString = domChecking.asText();
            Matcher matcher = starPattern.matcher(checkingString);
            if (matcher.find()) {
                String rating = matcher.group();
                Integer result = Integer.parseInt(rating);
                yelpReview.setCheckins(result);
            }
        }
    }

    private void extractReviewDate(DomElement domReview, Review yelpReview) {
        DomElement domDate = domReview.getFirstByXPath(".//span[@class='smaller date']");
        String stringDate = domDate.asText().trim();
        Matcher matcher = datePatter.matcher(stringDate);
        if (matcher.find()) {
            try {
                Date dateReview = YELP_DATE.parse(matcher.group());
                yelpReview.setReviewDate(dateReview);
            } catch (ParseException e) {
                log.warn("Could not parse date '{}' for review", stringDate);
            }
        }

    }

    private void extractReviewRating(DomElement domReview, Review yelpReview) {

        DomElement domRating = domReview.getFirstByXPath(".//div[@class='rating']/i");
        if (domRating != null) {
            String ratingString = domRating.getAttribute("title");
            Matcher matcher = starPattern.matcher(ratingString);
            if (matcher.find()) {
                String rating = matcher.group();
                Double result = Double.parseDouble(rating);
                yelpReview.setRating(result);
            }
        } else {
            // No Rating given
            yelpReview.setRating(0.0);
        }

    }

    private void extractReviewText(DomElement domReview, Review yelpReview) {
        DomElement domReviewText = domReview.getFirstByXPath(".//div[@class='review_comment']");
        String review = domReviewText.asText();

        yelpReview.setContent(review);
    }

    private Tuple<Product, Review> extractRestaurantAndID(DomElement domReview, Product yelpVenue, Review yelpReview) throws CrawlerException {
        DomElement domReviewLink = domReview
            .getFirstByXPath(".//a[@class='i-wrap ig-wrap-common i-orange-link-common-wrap linkToThis']");

        String href = domReviewLink.getAttribute("href");

        // i.e. /biz/tong-sam-gyup-goo-ee-korean-restaurant-flushing-2?hrid=-ct-3FVURVOtKoRs0WR1iw
        String[] split = href.split("[?]|[=]");
        String bizURL = split[0];
        String reviewID = split[split.length - 1];

        // yelpVenue = productDao.getProductByURL(bizURL);
        yelpReview = reviewDao.find(reviewID);

        if (yelpVenue == null) {
            yelpVenue = crawlVenue(bizURL);
        }
        if (yelpReview == null) {
            yelpReview = new Review();
            yelpReview.setId(reviewID);
        }

        yelpReview.setProduct(yelpVenue);

        return new Tuple<Product, Review>(yelpVenue, yelpReview);

    }

    private Product crawlVenue(String bizURL) throws CrawlerException {
        String yelpURL = String.format(YELP_BASE, bizURL);
        Integer timeout = PropertyUtil.getIntegerProperty("crawler.timeout", 1);
        HtmlPage yelpVenuePage = CrawlerUtil.getWebPage(webClient, yelpURL, timeout);
        Product product = new Product();

        product.setId(bizURL);
        extractVenueName(yelpVenuePage, product);
        extractVenueRating(yelpVenuePage, product);
        extractPriceRange(yelpVenuePage, product);
        extractLocation(yelpVenuePage, product);

        return product;
    }

    private void extractLocation(HtmlPage yelpVenuePage, Product product) {
        DomElement domCity = yelpVenuePage.getFirstByXPath("//span[@itemprop='addressLocality']");
        DomElement domState = yelpVenuePage.getFirstByXPath("//span[@itemprop='addressRegion']");

        // Services have 'areas'
        String city = "";
        if (domCity != null) {
            city = domCity.asText();
        }

        String state = "";
        if (domState != null) {
            // US notation
            state = domState.asText();
            String result = String.format("%s, %s", city, state);
            product.setLocation(result);
        } else {
            /*
             * Rest of the World. May be inaccurate, but not relevant.
             */
            product.setLocation(city);
        }

    }

    private void extractPriceRange(HtmlPage yelpVenuePage, Product product) {
        DomElement domPriceRange = yelpVenuePage
            .getFirstByXPath("//span[@itemprop='priceRange']");
        if (domPriceRange != null) {
            Integer priceRangeLength = domPriceRange.getTextContent().length();
            PriceCategory priceCategory;
            switch (priceRangeLength) {
                case 1:
                    priceCategory = PriceCategory.$;
                    break;
                case 2:
                    priceCategory = PriceCategory.$$;
                    break;
                case 3:
                    priceCategory = PriceCategory.$$$;
                    break;
                case 4:
                    priceCategory = PriceCategory.$$$$;
                    break;
                default:
                    priceCategory = PriceCategory.NON_GIVEN;
            }
            product.setPriceCategory(priceCategory);
        } else {
            product.setPriceCategory(PriceCategory.NON_GIVEN);
        }
    }

    private void extractVenueName(HtmlPage yelpVenuePage, Product product) {
        DomElement domName = yelpVenuePage.getFirstByXPath("//h1[@itemprop='name']");
        product.setName(domName.asText());
    }

    private void extractVenueRating(HtmlPage yelpVenuePage, Product product) {
        DomElement domRating = yelpVenuePage.getFirstByXPath("//div[@class='rating-very-large']/i");
        if (domRating != null) {
            String ratingString = domRating.getAttribute("title");
            Matcher matcher = starPattern.matcher(ratingString);
            if (matcher.find()) {
                String rating = matcher.group();
                Double result = Double.parseDouble(rating);
                product.setRating(result);
            }
        } else {
            // No Rating given
            product.setRating(0.0);
        }
    }

    private String getURL(User user) {
        Integer from = pageNo * 10;
        String url = String.format(YELP_URL, user.getId(), from);

        return url;
    }
}
