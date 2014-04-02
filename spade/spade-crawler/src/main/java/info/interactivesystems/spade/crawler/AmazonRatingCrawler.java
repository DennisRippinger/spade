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
import java.util.List;

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

    private final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);

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

        List<ShadowReview> reviewsOfSameProduct = dao.findReviewFromUrl(url);

        String stringCategory = "";
        Double doubleAverageRating = 0.0;

        if (htmlPage != null) {
            // Get Category
            List<DomElement> listCategory = (List<DomElement>) htmlPage
                .getByXPath("//*[@id=\"nav-subnav\"]/li[1]/a");

            if (!listCategory.isEmpty()) {
                DomElement category = listCategory.get(0);
                stringCategory = category.getTextContent();
            }

            List<DomElement> listAverageCustomerRating = (List<DomElement>) htmlPage.getByXPath("//span[@title]");
            if (!listAverageCustomerRating.isEmpty()) {
                DomElement averageCustomerRating = listAverageCustomerRating
                    .get(0);
                String averageRating = averageCustomerRating
                    .getAttribute("title");
                String[] arrayAverageRating = averageRating.split(" ");
                doubleAverageRating = Double.parseDouble(arrayAverageRating[0]);
            }

            for (ShadowReview review : reviewsOfSameProduct) {
                review.setType(stringCategory);
                review.setAverageRating(doubleAverageRating);

                dao.save(review);
            }
        }

    }

    private HtmlPage loadPage(String currentProductPage) {
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
