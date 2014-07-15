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
import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.exception.CrawlerException;
import info.interactivesystems.spade.util.Authority;
import info.interactivesystems.spade.util.PriceCategory;
import info.interactivesystems.spade.util.ProductCategory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
 * The Class YelpCrawler.
 * 
 * @author Dennis Rippinger
 * @deprecated Entities not current state.
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class YelpVenueCrawler {

    private static final String YELP_URL = "http://www.yelp.com/search?cflt=%s&find_loc=%s&start=%s";

    private final WebClient webClient = CrawlerUtil.getRandomDesktopWebClient(false, false);
    private final Pattern starPattern = Pattern.compile("\\d[.\\d]*");

    @Resource
    private ProductDao productDao;

    @Setter
    private String location;
    @Setter
    private Integer pageNo = 0;
    @Setter
    private ProductCategory category;

    /**
     * Crawl reviews.
     * 
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws InterruptedException the interrupted exception
     */
    public void crawlVenues() throws UnsupportedEncodingException, InterruptedException {
        String yelpOverviewURL = getURL(location, pageNo, category);
        HtmlPage yelpPage;
        try {
            yelpPage = CrawlerUtil.getWebPage(webClient, yelpOverviewURL, 0);

            Integer numberOfVenues = getMaximumItems(yelpPage);

            // First Page
            crawlVenues(yelpPage);

            // Iteration
            Double counterPage = pageNo * 1.0;
            Double counterMax = getMaximumPageNumber(numberOfVenues);

            for (; counterPage < counterMax; counterPage++) {
                pageNo++;

                try {
                    yelpOverviewURL = getURL(location, pageNo, category);
                    yelpPage = CrawlerUtil.getWebPage(webClient, yelpOverviewURL, 3);
                    crawlVenues(yelpPage);
                } catch (CrawlerException e) {
                    log.warn("Could not load a following page", e);
                }

            }
        } catch (CrawlerException e) {
            log.error("Could not load first page", e);
        }

    }

    /**
     * Crawl venues.
     * 
     * @param yelpPage the yelp page
     * @throws InterruptedException the interrupted exception
     */
    private void crawlVenues(HtmlPage yelpPage) throws InterruptedException {
        @SuppressWarnings("unchecked")
        List<DomElement> divVenue = (List<DomElement>) yelpPage
            .getByXPath("//*[@id='super-container']/div[3]/div[3]/div[1]/div/div[1]/ul/li[not(@class='add-search-result')]");

        for (DomElement venueContainer : divVenue) {
            Product product = new Product();

            product.setAuthority(Authority.YELP);
            product.setLocation(location);
            product.setType(category);

            extractIdAndName(venueContainer, product);
            extractRating(venueContainer, product);
            extractImageLocation(venueContainer, product);
            extractPriceRange(venueContainer, product);

            // Random ID eats update.
            if (!productDao.checkIfAlreadyExists(product.getId())) {
                productDao.save(product);
            }
        }

    }

    /**
     * Extract id name and source.
     * 
     * @param venueContainer the venue container
     * @param product the product
     */
    private void extractIdAndName(DomElement venueContainer, Product product) {
        DomElement domName = venueContainer.getFirstByXPath(".//a[@class='biz-name']");
        String venueName = domName.asText();
        String venueURL = domName.getAttribute("href");

        product.setName(venueName);
        product.setId(venueURL);
    }

    /**
     * Extract image location.
     * 
     * @param venueContainer the venue container
     * @param product the product
     */
    private void extractImageLocation(DomElement venueContainer, Product product) {
        DomElement domImage = venueContainer.getFirstByXPath(".//img[@height='90']");
        String imgLocation = domImage.getAttribute("src");
        product.setImageUrl(imgLocation);
    }

    /**
     * Extract price range.
     * 
     * @param venueContainer the venue container
     * @param product the product
     */
    private void extractPriceRange(DomElement venueContainer, Product product) {
        DomElement domPriceRange = venueContainer
            .getFirstByXPath(".//span[@class='business-attribute price-range']");
        if (domPriceRange != null) {
            String priceRange = domPriceRange.getTextContent();
            PriceCategory priceCategory = PriceCategory.valueOf(priceRange);
            product.setPriceCategory(priceCategory);
        } else {
            product.setPriceCategory(PriceCategory.NON_GIVEN);
        }

    }

    /**
     * Extract rating.
     * 
     * @param venueContainer the venue container
     * @param product the product
     */
    private void extractRating(DomElement venueContainer, Product product) {
        DomElement domRating = venueContainer.getFirstByXPath(".//div[@class='rating-large']/i");
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

        // log.info("Venue '{}' has a '{}' rating", product.getName(), product.getRating());

    }

    /**
     * Gets the maximum items.
     * 
     * @param yelpPage the yelp page
     * @return the maximum items
     */
    private Integer getMaximumItems(HtmlPage yelpPage) {
        DomElement paginationElement = yelpPage.getFirstByXPath("//span[@class='pagination-results-window']");
        String paginationString = paginationElement.getTextContent();

        String[] split = paginationString.split(" ");
        if (split.length > 3) { // Multiple Empty Entries
            String value = split[split.length - 1];
            Integer result = Integer.parseInt(value.trim());
            log.info("Category '{}' in '{}' has '{}' venues.", category.getURLString(), location, result);
            return result;
        }
        log.error("Could not find pagination info");
        return 0;
    }

    /**
     * Gets the maximum page number.
     * 
     * @param numberOfVenues the number of venues
     * @return the maximum page number
     */
    private Double getMaximumPageNumber(Integer numberOfVenues) {
        Double counterMax = numberOfVenues * 1.0 / 10;
        if (counterMax > 99) {
            // Yelp states it 'has' more than it gives.
            counterMax = 99.0;
        }
        return counterMax;
    }

    /**
     * Gets the url.
     * 
     * @param location the location
     * @param page the page
     * @param category the category
     * @return the url
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private String getURL(String location, Integer page, ProductCategory category) throws UnsupportedEncodingException {
        Integer from = page * 10;
        String encLocation = URLEncoder.encode(location, "UTF-8");

        String url = String.format(YELP_URL, category.getURLString(), encLocation, from);

        return url;
    }

}
