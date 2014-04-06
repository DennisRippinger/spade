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

import info.interactivesystems.spade.captcha.CaptchaService;
import info.interactivesystems.spade.captcha.FileNamer;
import info.interactivesystems.spade.dao.ShadowReviewDao;
import info.interactivesystems.spade.entities.ShadowReview;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

@Slf4j
@Service
public class AmazonRatingCrawler {

    @Resource
    private CaptchaService captchaService;

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
            Double doubleAverageRating = -1.0;

            // Get Category
            List<DomElement> listCategory = (List<DomElement>) htmlPage
                .getByXPath("//*[@id=\"nav-subnav\"]/li[1]/a");

            if (!listCategory.isEmpty()) {
                DomElement category = listCategory.get(0);
                stringCategory = category.getTextContent();
            }

            doubleAverageRating = checkAverageRating(htmlPage, doubleAverageRating);
            doubleAverageRating = checkAverageRatingAlternativeLayout(htmlPage, doubleAverageRating);

            if (doubleAverageRating != -1.0) {
                log.info("Average Rating: '{}'", doubleAverageRating);
                for (ShadowReview review : reviewsOfSameProduct) {
                    review.setType(stringCategory);
                    review.setAverageRating(doubleAverageRating);

                    dao.save(review);
                }
            } else {
                log.warn("Could not retrieve avg. Rating, skipping");
            }

        }
        waitInSeconds(5);

    }

    @SuppressWarnings("unchecked")
    private Double checkAverageRating(HtmlPage htmlPage, Double doubleAverageRating) {
        List<DomElement> listAverageCustomerRating;
        if (doubleAverageRating.equals(-1.0)) {
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
        }
        return doubleAverageRating;
    }

    @SuppressWarnings("unchecked")
    private Double checkAverageRatingAlternativeLayout(HtmlPage htmlPage, Double doubleAverageRating) {
        if (doubleAverageRating.equals(-1.0)) {
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
        if (page.getTitleText().contains("Robot Check")) {
            log.info("Detected as Robot, tring to break captcha");
            waitInSeconds(10);
            HtmlPage breakCaptchaResult = breakCaptcha(page);

            if (breakCaptchaResult.getTitleText().contains("Robot Check")) {
                log.info("Captcha break failed, reseting cookies");
                webClient.getCookieManager().clearCookies();
                randomWalk();
                return loadPageRaw(currentProductPage);
            } else {
                log.info("Captcha break succesfull");
                return breakCaptchaResult;
            }

        }
        return page;

    }

    private HtmlPage breakCaptcha(HtmlPage page) {
        HtmlForm form = page.getFormByName("");

        String captchaString = captchaService.getValueFromCaptcha(page);

        HtmlButton button = (HtmlButton) page.getByXPath("//button[@class]").get(0);
        HtmlTextInput textField = form.getInputByName("field-keywords");

        textField.setValueAttribute(captchaString);
        try {
            HtmlPage result = button.click();
            return result;
        } catch (IOException e) {
            log.error("Could not click on Captcha button", e);
        }
        return page;
    }

    @SuppressWarnings("unchecked")
    private void randomWalk() {
        HtmlPage page = loadPageRaw("http://www.amazon.com");
        List<DomElement> listOfLinks = (List<DomElement>) page.getByXPath("//a[@href]");
        Collections.shuffle(listOfLinks);
        for (Integer i = 0; i >= 10; i++) {
            waitInSeconds(5);
            DomElement domUrl = listOfLinks.get(i);
            log.info("Surfing to '{}'", domUrl);
            String relativeUrl = domUrl.getAttribute("href");
            loadPageRaw("http://www.amazon.com" + relativeUrl);
        }
    }

    private HtmlPage loadPageRaw(String currentProductPage) {
        HtmlPage page = null;
        try {
            page = webClient.getPage(currentProductPage);
            log.info("Page Title: '{}'", page.getTitleText());
        } catch (FailingHttpStatusCodeException e1) {
            log.error(e1.getMessage());
        } catch (MalformedURLException e1) {
            log.error("Malformed URL {}", currentProductPage, e1);
        } catch (IOException e1) {
            log.error("IOException {}", currentProductPage, e1);
        }

        return page;
    }

    private void waitInSeconds(Integer seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
