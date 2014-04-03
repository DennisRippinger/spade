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
package info.interactivesystems.spade.crawler;

import info.interactivesystems.spade.dao.ShadowReviewDao;
import info.interactivesystems.spade.entities.ShadowReview;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Slf4j
@Service
public class AmazonRatingCrawler {

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private final Pattern starPattern = Pattern.compile("\\d[-\\d]*");

    public AmazonRatingCrawler() {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }

    @Resource
    private ShadowReviewDao dao;

    /**
     * Crawls the avg. rating of a review.
     * 
     * @param url the url
     * @param review the review
     * @return the average rating
     */
    @SuppressWarnings("unchecked")
    public void getAverageRating(String url) throws Exception {
        HtmlPage htmlPage = loadPage(url);

        if (htmlPage != null) {

            List<ShadowReview> reviewsOfSameProduct = dao.findReviewFromUrl(url);

            String stringCategory = "";
            Double doubleAverageRating = 0.0;

            // Get Category
            List<DomElement> listCategory = (List<DomElement>) htmlPage
                .getByXPath("//*[@id=\"nav-subnav\"]/li[1]/a");

            if (!listCategory.isEmpty()) {
                DomElement category = listCategory.get(0);
                stringCategory = category.getTextContent();
            }

            doubleAverageRating = checkAverageRating(htmlPage, doubleAverageRating);
            doubleAverageRating = checkAverageRatingAlternativeLayout(htmlPage, doubleAverageRating);

            for (ShadowReview review : reviewsOfSameProduct) {
                review.setType(stringCategory);
                review.setAverageRating(doubleAverageRating);

                dao.save(review);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private Double checkAverageRating(HtmlPage htmlPage, Double doubleAverageRating) {
        List<DomElement> listAverageCustomerRating;
        if (doubleAverageRating.equals(0.0)) {
            listAverageCustomerRating = (List<DomElement>) htmlPage
                .getByXPath("//i[@class]");
            if (!listAverageCustomerRating.isEmpty()) {
                for (DomElement averageCustomerRating : listAverageCustomerRating) {
                    String averageRating = averageCustomerRating
                        .getAttribute("class");
                    if (averageRating.contains("a-icon a-icon-star a-star")) {
                        Matcher matcher = starPattern.matcher(averageRating);
                        while (matcher.find()) {
                            String rating = matcher.group();
                            // rating comes in 3-5 or 3 pattern.
                            rating = rating.replace("-", ".");
                            return Double.parseDouble(rating);
                        }
                    }
                }

            }
            return 0.0;
        }
        return doubleAverageRating;
    }

    @SuppressWarnings("unchecked")
    private Double checkAverageRatingAlternativeLayout(HtmlPage htmlPage, Double doubleAverageRating) {
        if (doubleAverageRating.equals(0.0)) {
            List<DomElement> listAverageCustomerRating = (List<DomElement>) htmlPage.getByXPath("//span[@title]");
            if (!listAverageCustomerRating.isEmpty()) {
                DomElement averageCustomerRating = listAverageCustomerRating
                    .get(0);
                String averageRating = averageCustomerRating
                    .getAttribute("title");
                String[] arrayAverageRating = averageRating.split(" ");
                doubleAverageRating = Double.parseDouble(arrayAverageRating[0]);
            }
        }
        return doubleAverageRating;
    }

    /**
     * Collect some cookies to appear as valid user.
     * 
     * @param currentProductPage
     * @return
     */
    private HtmlPage loadPage(String currentProductPage) {
        HtmlPage page = loadPageRaw(currentProductPage);
        if (page.getTitleText().equals("Robot Check")) {
            webClient.getCookieManager().clearCookies();
            randomWalk(page);
            return loadPageRaw(currentProductPage);
        }
        return page;

    }

    @SuppressWarnings("unchecked")
    private void randomWalk(HtmlPage page) {
        List<DomElement> listOfLinks = (List<DomElement>) page.getByXPath("//a[@href]");
        Collections.shuffle(listOfLinks);
        for (Integer i = 0; i >= 4; i++) {
            DomElement domUrl = listOfLinks.get(i);
            String relativeUrl = domUrl.getAttribute("href");
            loadPageRaw("http://www.amazon.com" + relativeUrl);
        }
    }

    private HtmlPage loadPageRaw(String currentProductPage) {
        HtmlPage page = null;
        try {
            page = webClient.getPage(currentProductPage);
        } catch (FailingHttpStatusCodeException e1) {
            log.error(e1.getMessage());
        } catch (MalformedURLException e1) {
            log.error("Malformed URL {}", currentProductPage, e1);
        } catch (IOException e1) {
            log.error("IOException {}", currentProductPage, e1);
        }

        return page;
    }
}
