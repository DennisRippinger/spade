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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import info.interactivesystems.spade.crawler.util.AmazonCategory;
import info.interactivesystems.spade.crawler.util.CrawlerUtil;
import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.exception.CrawlerException;
import info.interactivesystems.spade.util.ProductCategory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class YelpCrawler.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
@Deprecated
@Scope(value = "prototype")
public class AmazonProductCrawler {

	private static final String AMAZON_URL = "http://www.amazon.com/gp/search/?node=%s&page=%s&sort=review-rank";

	private static final Integer AMAZON_MAX = 400;

	private final WebClient webClient = CrawlerUtil.getChrome(false, false);
	private final Pattern starPattern = Pattern.compile("\\d[.\\d]*");

	@Resource
	private ProductDao productDao;

	@Setter
	private Integer pageNo = 1;

	@Setter
	private AmazonCategory category;

	@Setter
	private ProductCategory productCategory;

	/**
	 * Crawl reviews.
	 *
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws InterruptedException         the interrupted exception
	 */
	public void crawlProducts() throws InterruptedException {
		String amazonOverviewURL = getURL(pageNo, category);
		HtmlPage amazonPage;
		try {
			amazonPage = CrawlerUtil.getWebPage(webClient, amazonOverviewURL, 0);

			// First Page
			crawlProducts(amazonPage);
			pageNo++;

			for (; pageNo < AMAZON_MAX; pageNo++) {

				try {
					amazonOverviewURL = getURL(pageNo, category);
					amazonPage = CrawlerUtil.getWebPage(webClient, amazonOverviewURL, 0);
					crawlProducts(amazonPage);
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
	private void crawlProducts(HtmlPage yelpPage) throws InterruptedException {
		@SuppressWarnings("unchecked")
		List<DomElement> divItems = (List<DomElement>) yelpPage
				.getByXPath("//div[starts-with(@id, 'result_')]");

		if (divItems.isEmpty()) {
			log.warn("No Items found");
		}

		for (DomElement productContainer : divItems) {
			Product product = new Product();

			String asin = getASIN(productContainer);
			product.setId(asin);

			if (hasReviews(productContainer, product) && !productDao.checkIfAlreadyExists(asin)) {


				extractName(productContainer, product);
				extractRating(productContainer, product);
				extractImageLocation(productContainer, product);
				extractURL(productContainer, product);
				extractPrice(productContainer, product);

				productDao.save(product);
			}

		}

	}

	private Boolean hasReviews(DomElement productContainer, Product product) {
		DomElement domNoOfReviews = productContainer.getFirstByXPath(".//span[@class='rvwCnt']/a");
		if (domNoOfReviews != null) {
			String stringNoOfReviews = domNoOfReviews.asText().replace(",", "");
			Integer noOfReviews = Integer.parseInt(stringNoOfReviews);
			product.setNoOfReviews(noOfReviews);

			log.info("Product '{}' has {}' Reviews", product.getId(), noOfReviews);
			return true;
		}
		return false;

	}

	private void extractPrice(DomElement productContainer, Product product) {
		DomElement domPrice = productContainer.getFirstByXPath(".//span[@class='bld lrg red']");

		if (domPrice != null) {
			// e.g. '$3,399.00'
			String stringPrice = domPrice.asText();
			// Can be empty for more price options
			if (!stringPrice.isEmpty()) {
				stringPrice = stringPrice.replace("$", "").replace(",", "").trim();
				try {
					Double price = Double.parseDouble(stringPrice);

					product.setPrice(price);
					return;
				} catch (NumberFormatException e) {
					log.warn("Could not parse price '{}'", stringPrice);
				}

			}
		}
		product.setPrice(0.0);
		log.warn("Price not found for '{}'", product.getId());

	}

	private void extractURL(DomElement productContainer, Product product) {
		DomElement domURL = productContainer.getFirstByXPath(".//h3[@class='newaps']/a");
		String productURL = domURL.getAttribute("href");

		// Cut refer
		productURL = productURL.substring(0, productURL.lastIndexOf('/'));

//        product.setSource(productURL);
	}

	private String getASIN(DomElement productContainer) {
		return productContainer.getAttribute("name");
	}

	private void extractName(DomElement productContainer, Product product) {
		DomElement domName = productContainer.getFirstByXPath(".//span[@class='lrg bold']");
		String productName = domName.asText();

		product.setName(productName);
	}

	private void extractImageLocation(DomElement productContainer, Product product) {
		DomElement domImage = productContainer.getFirstByXPath(".//img[@class='productImage cfMarker'|@class='placeholder cfMarker']");
		if (domImage != null) {
			String imgLocation = domImage.getAttribute("src");
//            product.setImageUrl(imgLocation);
		}

	}

	private void extractRating(DomElement productContainer, Product product) {
		DomElement domRating = productContainer.getFirstByXPath(".//span[@name='" + product.getId() + "']/a");
		if (domRating != null) {
			String ratingString = domRating.getAttribute("alt");
			Matcher matcher = starPattern.matcher(ratingString);
			while (matcher.find()) {
				String stringRating = matcher.group();
				// rating comes in '3.5' or '3' pattern.
				Double rating = Double.parseDouble(stringRating);
				product.setRating(rating);
				return;
			}

			// No Rating given
			product.setRating(0.0);
			log.warn("No rating given for ASIN '{}'", product.getId());
		}
	}

	private String getURL(Integer page, AmazonCategory category) {
		return String.format(AMAZON_URL, category.getId(), page);
	}

}
